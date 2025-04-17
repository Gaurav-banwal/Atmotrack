package com.example.matproject

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    private lateinit var defaultCityEditText: EditText
    private lateinit var saveButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        
        // Setup edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Initialize views
        defaultCityEditText = findViewById(R.id.default_city_edit_text)
        saveButton = findViewById(R.id.save_button)
        
        // Get current default city from SharedPreferences
        val sharedPreferences = getSharedPreferences("WeatherAppPrefs", MODE_PRIVATE)
        val currentDefaultCity = sharedPreferences.getString("default_city", "")
        
        // Set the current default city in the EditText
        defaultCityEditText.setText(currentDefaultCity)
        
        // Set up the save button click listener
        saveButton.setOnClickListener {
            val newDefaultCity = defaultCityEditText.text.toString().trim()
            
            if (newDefaultCity.isNotEmpty()) {
                // Save the new default city to SharedPreferences
                sharedPreferences.edit().putString("default_city", newDefaultCity).apply()
                
                // Show success message
                Toast.makeText(this, "Default city saved: $newDefaultCity", Toast.LENGTH_SHORT).show()
                
                // Close the activity
                finish()
            } else {
                // Show error message if no city is entered
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Navigate back to parent activity (MainActivity)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 