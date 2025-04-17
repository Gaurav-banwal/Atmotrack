package com.example.matproject

/**
 * Utility class for providing weather warnings and health risk information
 */
object WeatherWarningUtil {
    
    // Temperature thresholds in Celsius
    private const val EXTREME_HEAT_THRESHOLD = 35.0
    private const val HEAT_WARNING_THRESHOLD = 30.0
    private const val COLD_WARNING_THRESHOLD = 5.0
    private const val EXTREME_COLD_THRESHOLD = 0.0
    
    // Wind speed thresholds in m/s
    private const val HIGH_WIND_THRESHOLD = 10.0
    private const val EXTREME_WIND_THRESHOLD = 17.0
    
    // Air Quality Index (AQI) descriptions
    private val aqiDescriptions = mapOf(
        1 to "Good",
        2 to "Fair",
        3 to "Moderate",
        4 to "Poor",
        5 to "Very Poor"
    )
    
    /**
     * Evaluates weather conditions and returns warnings if conditions are unfavorable
     * @param temperature Current temperature in Celsius
     * @param windSpeed Wind speed in m/s
     * @param condition Weather condition (e.g., "Rain", "Clear", "Snow")
     * @param aqiValue Optional AQI value (1-5 scale)
     * @return Pair of (warning message, health risks) or null if conditions are favorable
     */
    fun evaluateWeatherConditions(
        temperature: Double, 
        windSpeed: Double, 
        condition: String,
        aqiValue: Int? = null
    ): Pair<String, String>? {
        val warnings = mutableListOf<String>()
        val healthRisks = mutableListOf<String>()
        
        // IMPORTANT: We're using non-exclusive when blocks to allow multiple warnings
        // to be collected at the same time (temperature + weather condition)
        
        // Check temperature
        if (temperature >= EXTREME_HEAT_THRESHOLD) {
            warnings.add("EXTREME HEAT ALERT: Temperature is dangerously high.")
            healthRisks.add("Heat stroke, dehydration, and heat exhaustion risk.")
        } else if (temperature >= HEAT_WARNING_THRESHOLD) {
            warnings.add("HEAT WARNING: High temperatures may cause discomfort.")
            healthRisks.add("Dehydration and heat exhaustion risk for vulnerable individuals.")
        } else if (temperature <= EXTREME_COLD_THRESHOLD) {
            warnings.add("EXTREME COLD ALERT: Temperature is dangerously low.")
            healthRisks.add("Hypothermia and frostbite risk. Respiratory issues may worsen.")
        } else if (temperature <= COLD_WARNING_THRESHOLD) {
            warnings.add("COLD WARNING: Low temperatures may cause discomfort.")
            healthRisks.add("Cold-related illnesses and increased risk of respiratory infections.")
        }
        
        // Check wind speed - also independent of temperature
        if (windSpeed >= EXTREME_WIND_THRESHOLD) {
            warnings.add("EXTREME WIND ALERT: Strong winds may cause damage and pose danger.")
            healthRisks.add("Physical danger from flying debris, strain on cardiovascular system.")
        } else if (windSpeed >= HIGH_WIND_THRESHOLD) {
            warnings.add("WIND WARNING: High winds may affect outdoor activities.")
            healthRisks.add("Difficulty breathing for those with respiratory conditions.")
        }
        
        // Check air quality - independent of other conditions
        aqiValue?.let {
            when (it) {
                3 -> {
                    warnings.add("MODERATE AIR QUALITY ALERT: Air quality is acceptable but may cause concern for sensitive individuals.")
                    healthRisks.add("Possible respiratory symptoms for sensitive individuals, especially those with asthma or other respiratory conditions.")
                }
                4 -> {
                    warnings.add("POOR AIR QUALITY ALERT: Air pollution levels are high and may affect the general population.")
                    healthRisks.add("Increased likelihood of respiratory symptoms in general population. More serious effects for sensitive groups.")
                }
                5 -> {
                    warnings.add("VERY POOR AIR QUALITY ALERT: Air pollution levels are very high and pose health risks to everyone.")
                    healthRisks.add("Health warnings of emergency conditions. Entire population is likely to be affected with serious health effects.")
                }
                else->{ }

            }
        }
        
        // Check weather conditions - completely independent of temperature and wind checks
        when (condition.lowercase()) {
            "thunderstorm", "thunder" -> {
                warnings.add("THUNDERSTORM WARNING: Lightning and heavy rain expected.")
                healthRisks.add("Risk of lightning strikes and flash flooding.")
            }
            "rain", "light rain", "moderate rain", "heavy rain", "showers", "drizzle" -> {
                if (!warnings.any { it.contains("THUNDERSTORM") }) {
                    warnings.add("RAIN WARNING: Wet conditions may affect visibility and traction.")
                    healthRisks.add("Increased risk of accidents and respiratory issues in humid conditions.")
                }
            }
            "clouds", "partly clouds", "cloudy", "overcast", "cloud", "few clouds", "scattered clouds", "broken clouds" -> {
                warnings.add("CLOUDY WEATHER ALERT: Potential rainfall may occur.")
                healthRisks.add("Humidity changes may affect those with respiratory conditions.")
            }
            "snow", "light snow", "moderate snow", "heavy snow", "blizzard" -> {
                warnings.add("SNOW WARNING: Slippery conditions and reduced visibility.")
                healthRisks.add("Risk of hypothermia, falls, and travel hazards.")
            }
            "fog", "mist", "foggy", "haze" -> {
                warnings.add("FOG WARNING: Severely reduced visibility.")
                healthRisks.add("Travel hazards and breathing difficulties for asthmatics.")
            }
            "dust", "sandstorm", "sand", "ash" -> {
                warnings.add("DUST/SAND WARNING: Poor air quality.")
                healthRisks.add("Respiratory issues, eye irritation, and asthma exacerbation.")
            }
        }
        
        return if (warnings.isNotEmpty() && healthRisks.isNotEmpty()) {
            Pair(warnings.joinToString("\n\n"), healthRisks.joinToString("\n\n"))
        } else {
            null
        }
    }
    
    /**
     * Provides precautions based on weather conditions
     * @param temperature Current temperature in Celsius
     * @param windSpeed Wind speed in m/s
     * @param condition Weather condition
     * @param aqiValue Optional AQI value (1-5 scale)
     * @return List of precautions to take
     */
    fun getPrecautions(
        temperature: Double, 
        windSpeed: Double, 
        condition: String,
        aqiValue: Int? = null
    ): List<String> {
        val precautions = mutableListOf<String>()
        
        // Temperature precautions - independent of weather conditions
        if (temperature >= HEAT_WARNING_THRESHOLD) {
            precautions.add("• Stay hydrated by drinking plenty of water")
            precautions.add("• Wear lightweight, light-colored clothing")
            precautions.add("• Limit outdoor activities, especially during peak heat")
            precautions.add("• Use sunscreen and seek shade when outdoors")
            precautions.add("• Check on vulnerable individuals (elderly, children)")
        }
        
        if (temperature <= COLD_WARNING_THRESHOLD) {
            precautions.add("• Wear layers of warm clothing, including hat and gloves")
            precautions.add("• Limit time outdoors in extreme cold")
            precautions.add("• Keep indoor heating at safe levels")
            precautions.add("• Protect exposed skin from frostbite")
            precautions.add("• Check on vulnerable individuals (elderly, children)")
        }
        
        // Wind precautions - can apply with any temperature or condition
        if (windSpeed >= HIGH_WIND_THRESHOLD) {
            precautions.add("• Secure loose objects outdoors")
            precautions.add("• Avoid areas with trees or power lines")
            precautions.add("• Take caution when driving, especially high-profile vehicles")
        }
        
        // Air quality precautions
        aqiValue?.let {
            when (it) {
                3 -> {
                    precautions.add("• Sensitive individuals should reduce prolonged outdoor activities")
                    precautions.add("• Keep windows closed during peak pollution hours")
                    precautions.add("• Monitor symptoms if you have respiratory conditions")
                }
                4 -> {
                    precautions.add("• Everyone should reduce outdoor activities")
                    precautions.add("• Sensitive groups should avoid outdoor activities")
                    precautions.add("• Use air purifiers indoors if available")
                    precautions.add("• Wear a mask when outdoors if you have respiratory issues")
                }
                5 -> {
                    precautions.add("• Everyone should avoid outdoor activities")
                    precautions.add("• Keep all windows and doors closed")
                    precautions.add("• Use air purifiers indoors")
                    precautions.add("• Wear masks outdoors (N95 recommended)")
                    precautions.add("• Seek medical attention if experiencing breathing difficulties")
                }
                1, 2 -> {
                    // Good to Fair air quality - no specific precautions needed
                }
                else -> {
                    // Unknown AQI value - add general precaution
                    precautions.add("• Monitor air quality updates")
                }
            }
        }
        
        // Condition-specific precautions - completely independent of temperature
        when (condition.lowercase()) {
            "thunderstorm", "thunder" -> {
                precautions.add("• Stay indoors and away from windows")
                precautions.add("• Avoid using electrical appliances")
                precautions.add("• If outdoors, avoid open spaces and tall objects")
            }
            "rain", "light rain", "moderate rain", "heavy rain", "showers", "drizzle" -> {
                precautions.add("• Use umbrella or raincoat when going outdoors")
                precautions.add("• Drive slowly and maintain safe distance")
                precautions.add("• Be cautious of slippery surfaces")
            }
            "clouds", "partly clouds", "cloudy", "overcast", "cloud", "few clouds", "scattered clouds", "broken clouds" -> {
                precautions.add("• Carry an umbrella or raincoat as rainfall may occur")
                precautions.add("• Check weather updates regularly")
                precautions.add("• Plan indoor alternatives for outdoor activities")
                precautions.add("• Wear water-resistant footwear")
            }
            "snow", "light snow", "moderate snow", "heavy snow", "blizzard", "snow" -> {
                precautions.add("• Dress warmly in layers")
                precautions.add("• Drive cautiously and allow extra time for travel")
                precautions.add("• Clear snow from walkways to prevent falls")
                precautions.add("• Keep emergency supplies in your vehicle")
            }
            "fog", "mist", "foggy", "haze" -> {
                precautions.add("• Use fog lights when driving")
                precautions.add("• Reduce speed and increase following distance")
                precautions.add("• Use face masks if you have respiratory conditions")
            }
            "dust", "sandstorm", "sand", "ash" -> {
                precautions.add("• Stay indoors with windows and doors closed")
                precautions.add("• Use air purifiers if available")
                precautions.add("• Wear mask and eye protection if going outdoors")
            }
        }
        
        return precautions
    }
    
    /**
     * Get a description of the Air Quality Index
     * @param aqiValue AQI value (1-5)
     * @return User-friendly description of the AQI
     */
    fun getAqiDescription(aqiValue: Int): String {
        return aqiDescriptions[aqiValue] ?: "Unknown"
    }
    
    /**
     * Get a color for the Air Quality Index
     * @param aqiValue AQI value (1-5)
     * @return Color resource ID for the AQI
     */
    fun getAqiColorResource(aqiValue: Int): Int {
        return when (aqiValue) {
            1 -> android.graphics.Color.parseColor("#4CAF50") // Good - Green
            2 -> android.graphics.Color.parseColor("#8BC34A") // Fair - Light Green
            3 -> android.graphics.Color.parseColor("#FFEB3B") // Moderate - Yellow
            4 -> android.graphics.Color.parseColor("#FF9800") // Poor - Orange
            5 -> android.graphics.Color.parseColor("#F44336") // Very Poor - Red
            else -> android.graphics.Color.parseColor("#9E9E9E") // Unknown - Gray
        }
    }
} 