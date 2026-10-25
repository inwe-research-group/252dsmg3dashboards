package com.dsm.a252dsmg3dashboards.data.model

data class EstadisticasResponse(
    val success: Boolean,
    val data: List<NPersonasXTipoDocumento>? = null,
    val error: String? = null
)