package com.example.matproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.matproject.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//  Weather API :- ddde54083cec202fb9e24f2f7427a6db
// Air Quality API key: 00f47329e1baf40566d9c0c3d2c8bd71
class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    
    // Store current city's coordinates for air quality API
    private var currentLat: Double? = null
    private var currentLon: Double? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        // Set up search functionality
        searchcity()
        
        // Set up settings button click listener
        val settingsButton = findViewById<ImageButton>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Load default city if available
        loadDefaultCity()
    }
    
    override fun onResume() {
        super.onResume()
        // Check if we need to reload the default city (e.g., after returning from settings)
        loadDefaultCity()
    }
    
    /**
     * Loads the default city from SharedPreferences and fetches its weather data
     */
    private fun loadDefaultCity() {
        val sharedPreferences = getSharedPreferences("WeatherAppPrefs", MODE_PRIVATE)
        val defaultCity = sharedPreferences.getString("default_city", "")
        
        if (!defaultCity.isNullOrEmpty()) {
            fetchweatherdata(defaultCity)
        }
    }
    
    private fun searchcity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null)
                    fetchweatherdata(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
    }
    
    private fun fetchweatherdata(cityName:String){
        val retrofit= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response =
            retrofit.getweatherdata(cityName, appid = "ddde54083cec202fb9e24f2f7427a6db", units = "metric")

        response.enqueue(object : Callback<weatherapp> {
            override fun onResponse(call: Call<weatherapp>, response: Response<weatherapp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    // Store the coordinates for air quality API
                    currentLat = responseBody.coord.lat
                    currentLon = responseBody.coord.lon
                    
                    val temperature = responseBody.main.temp
                    binding.weather.text = "$temperature°C"
                    val humidity = responseBody.main.humidity.toString()
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure.toString()
                    val condition = responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxTemp = responseBody.main.temp_max

                    binding.weather1.text = condition
                    binding.max.text = "Max Temp: $maxTemp °C"
                    binding.humidity.text= "$humidity %"
                    binding.WindSpeed.text = "$windSpeed m/s"
                    binding.condition.text = condition
                    binding.sea.text = "$seaLevel hPa"
                    binding.cityname.text = "$cityName"
                    binding.day.text= dayName(System.currentTimeMillis())
                    binding.date.text= date()
                    binding.sunrise.text = convertUnixToTime(sunRise)
                    binding.sunset.text = convertUnixToTime(sunset)
                    
                    // Apply UI changes based on condition
                    changeimg(condition)
                    
                    // Fetch air quality data now that we have coordinates
                    fetchAirQualityData(currentLat!!, currentLon!!)
                }
            }

            private fun changeimg(condition: String) {
                when(condition) {
                    "Clear Sky","Sunny","Clear"->{
                        binding.root.setBackgroundResource(R.drawable.sunny_background)
                        binding.lottieAnimationView.setAnimation(R.raw.sun)
                    }

                    "Partly Clouds","Clouds","Mist","Foggy","Cloudy","Overcast",
                    "Cloud","Few Clouds","Scattered Clouds","Broken Clouds","Haze"->{
                        binding.root.setBackgroundResource(R.drawable.colud_background)
                        binding.lottieAnimationView.setAnimation(R.raw.cloud)
                    }

                    "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain","Rain"->{
                        binding.root.setBackgroundResource(R.drawable.rain_background)
                        binding.lottieAnimationView.setAnimation(R.raw.rain)
                    }

                    "Light Snow","Moderate Snow","Heavy Snow","Blizzard","Snow"->{
                        binding.root.setBackgroundResource(R.drawable.snow_background)
                        binding.lottieAnimationView.setAnimation(R.raw.snow)
                    }
                    else->{
                        binding.root.setBackgroundResource(R.drawable.sunny_background)
                        binding.lottieAnimationView.setAnimation(R.raw.sun)
                    }
                }
                binding.lottieAnimationView.playAnimation()
            }

            override fun onFailure(call: Call<weatherapp>, t: Throwable) {
                Log.e("Weather", "Error fetching weather data", t)
            }
        })
    }
    
    /**
     * Fetch air quality data using coordinates
     */
    private fun fetchAirQualityData(lat: Double, lon: Double) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
            
        val response = retrofit.getAirQualityData(
            latitude = lat, 
            longitude = lon, 
            appid = "00f47329e1baf40566d9c0c3d2c8bd71"
        )
        
        response.enqueue(object : Callback<AirQualityResponse> {
            override fun onResponse(call: Call<AirQualityResponse>, response: Response<AirQualityResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null && responseBody.list.isNotEmpty()) {
                    val airQualityData = responseBody.list[0]
                    val aqi = airQualityData.main.aqi
                    val pm25 = airQualityData.components.pm2_5
                    val pm10 = airQualityData.components.pm10
                    
                    // Update UI with air quality data
                    updateAirQualityUI(aqi, pm25, pm10)
                    
                    // Get current weather details
                    val temperature = binding.weather.text.toString().replace("°C", "").toDoubleOrNull() ?: 0.0
                    val windSpeed = binding.WindSpeed.text.toString().replace("m/s", "").trim().toDoubleOrNull() ?: 0.0
                    val condition = binding.condition.text.toString()
                    
                    // Check for weather warnings including air quality
                    checkWeatherWarnings(temperature, windSpeed, condition, aqi)
                }
            }
            
            override fun onFailure(call: Call<AirQualityResponse>, t: Throwable) {
                Log.e("AirQuality", "Error fetching air quality data", t)
            }
        })
    }
    
    /**
     * Update the UI elements related to air quality
     */
    private fun updateAirQualityUI(aqi: Int, pm25: Double, pm10: Double) {
        // Update AQI display
        binding.airQualityIndex.text = aqi.toString()
        binding.airQualityIndex.setBackgroundColor(WeatherWarningUtil.getAqiColorResource(aqi))
        
        // Update status text with descriptive information
        val aqiDescription = WeatherWarningUtil.getAqiDescription(aqi)
        val statusDescription = when (aqi) {
            1 -> "Good (1/5)"
            2 -> "Fair (2/5)"
            3 -> "Moderate (3/5)"
            4 -> "Poor (4/5)"
            5 -> "Very Poor (5/5)"
            else -> "Unknown"
        }
        binding.airQualityStatus.text = statusDescription
        
        // Update PM values
        binding.pm25Value.text = String.format("%.1f μg/m³", pm25)
        binding.pm10Value.text = String.format("%.1f μg/m³", pm10)
    }
    
    /**
     * Checks weather conditions and displays warnings if necessary.
     * Will show multiple warnings simultaneously (temperature + weather condition + air quality)
     */
    private fun checkWeatherWarnings(temperature: Double, windSpeed: Double, condition: String, aqiValue: Int? = null) {
        // Reset warning card visibility
        binding.warningCardView.visibility = View.GONE
        
        // Check if weather conditions warrant a warning
        val warningInfo = WeatherWarningUtil.evaluateWeatherConditions(temperature, windSpeed, condition, aqiValue)
        
        if (warningInfo != null) {
            val (warningText, healthRisks) = warningInfo
            
            // Count how many warnings we have
            val warningCount = warningText.split("\n\n").size
            
            // Update title based on number of warnings
            val titleView = (binding.warningCardView.getChildAt(0) as ScrollView)
                .getChildAt(0) as LinearLayout
            val headerLayout = titleView.getChildAt(0) as LinearLayout
            val titleTextView = headerLayout.getChildAt(1) as TextView
            
            if (warningCount > 1) {
                // Multiple warnings
                titleTextView.text = "Multiple Weather Warnings (${warningCount})"
            } else {
                // Single warning
                titleTextView.text = "Weather Warning"
            }
            
            // Get precautions based on the current conditions
            val precautions = WeatherWarningUtil.getPrecautions(temperature, windSpeed, condition, aqiValue)
            
            // Update UI with warning information
            binding.warningText.text = warningText
            binding.healthRiskText.text = healthRisks
            binding.precautionsText.text = precautions.joinToString("\n")
            
            // Show the warning card
            binding.warningCardView.visibility = View.VISIBLE
        }
    }

    private fun date():String?{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }

    fun convertUnixToTime(unixTime: Long): String {
        val date = Date(unixTime * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(date)
    }
}






