# ☀️ Weatherly - Real-Time Weather & AQI App

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat-square)](https://developer.android.com/)
[![Language](https://img.shields.io/badge/Language-Kotlin-7F52FF.svg?style=flat-square)](https://kotlinlang.org/)
[![UI](https://img.shields.io/badge/UI-Android_XML-4285F4.svg?style=flat-square)](https://developer.android.com/develop/ui/views/layout/xml)
[![API](https://img.shields.io/badge/API-OpenWeather_3.0-FFA500.svg?style=flat-square)](https://openweathermap.org/api)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](LICENSE)

## ✨ Project Summary

**Weatherly** is a native Android application designed to deliver detailed, real-time weather information and air quality data for any location. Built using **Kotlin** for robust logic and **XML** for a clean, traditional UI, the app seamlessly integrates with the **OpenWeather One Call 3.0 API** to provide comprehensive atmospheric and environmental metrics.

![image](https://github.com/user-attachments/assets/06dd127b-8d1c-4b93-afe5-b91e353d0bde)

## 🌟 Key Features

Weatherly provides users with a deep look into local conditions:

* **Comprehensive Data Display:** Shows all essential weather metrics on one screen.
* **Atmospheric Metrics:** Tracks and displays **Humidity**, **Wind Speed/Direction**, and **Pressure**.
* **Detailed Conditions:** Reports current weather conditions like **Fog**, **Rain**, **Clear Sky**, etc.
* **Solar Tracking:** Accurate display of **Sunrise** and **Sunset** times.
* **Air Quality Index (AQI):** Integrates AQI data to inform users about air pollution levels.
* **Location-Based Lookups:** Fetches data based on user location or manual city search.

## 💻 Technology Stack

| Category | Technology | Purpose |
| :--- | :--- | :--- |
| **Primary Language** | **Kotlin** | Used for all application logic, network communication, and data handling. |
| **UI Framework** | **Android XML** | Defines the structure and layout of the user-friendly interface. |
| **Weather API** | **OpenWeather One Call 3.0** | Provides the centralized data stream for current weather, forecast, and AQI. |
| **Build System** | **Gradle** | Manages dependencies, build configurations, and API key security. |

## 🚀 Getting Started

To run Weatherly locally, you must obtain an API key from OpenWeather.

### Prerequisites

* Android Studio (Latest Stable Version)
* An **OpenWeather API Key** (v3.0 or later, available from [openweathermap.org](https://openweathermap.org/))

### Installation

1.  **Clone the repository:**
    ```bash
    git clone [YOUR REPO URL HERE]
    cd Weatherly
    ```

2.  **Configure API Key:**
    * Open your project's local `local.properties` file (or a similar configuration file).
    * Add your OpenWeather API key:
        ```properties
        openWeatherApiKey="YOUR_API_KEY_HERE"
        ```
    * *Note: Ensure your `build.gradle` file securely reads this key and passes it to the application.*

3.  **Run the application:**
    * Open the project in Android Studio.
    * Select an emulator or a physical device and click the **Run** button.

## 🤝 Contributing

We highly value contributions to improve Weatherly! Whether you are fixing a bug, suggesting a new feature (like hourly forecasts), or improving the UI, your input is welcome.

1.  Fork the Project.
2.  Create your Feature Branch (`git checkout -b feature/hourly-forecast`).
3.  Commit your Changes (`git commit -m 'Feat: Implement 24-hour forecast view'`).
4.  Push to the Branch (`git push origin feature/hourly-forecast`).
5.  Open a Pull Request.

## 📄 License

This project is licensed under the **MIT License**. This highly permissive license allows for use, copying, modification, merging, publishing, distribution, and selling of the software, provided the original copyright and license notice are included.

See the [LICENSE](LICENSE) file in the repository root for the full text.
