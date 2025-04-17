package com.example.matproject

data class AirQualityResponse(
    val coord: Coord,
    val list: List<AirQualityData>
)

data class AirQualityData(
    val main: AirQualityMain,
    val components: AirQualityComponents,
    val dt: Long
)

data class AirQualityMain(
    val aqi: Int // Air Quality Index: 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor
)

data class AirQualityComponents(
    val co: Double,    // Carbon monoxide, μg/m3
    val no: Double,    // Nitrogen monoxide, μg/m3
    val no2: Double,   // Nitrogen dioxide, μg/m3
    val o3: Double,    // Ozone, μg/m3
    val so2: Double,   // Sulphur dioxide, μg/m3
    val pm2_5: Double, // Fine particles matter, μg/m3
    val pm10: Double,  // Coarse particulate matter, μg/m3
    val nh3: Double    // Ammonia, μg/m3
) 