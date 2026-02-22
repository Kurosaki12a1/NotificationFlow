package com.kuro.notiflow.domain.models.export

data class ExportResult(
    val uriString: String,
    val fileName: String,
    val totalCount: Int
)
