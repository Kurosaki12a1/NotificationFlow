package com.kuro.notiflow.domain.models.notifications


data class PackageStats(
    val packageName: String,
    val count: Int,
    val percentage: Double
)

