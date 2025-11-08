package com.dsm.a252dsmg3dashboards.data.model
import com.google.firebase.firestore.PropertyName

data class UserProfileData(
    val uid: String="",
    @get:PropertyName("tipo_documento") @set:PropertyName("tipo_documento")
    var tipoDocumento: String?=""
)
