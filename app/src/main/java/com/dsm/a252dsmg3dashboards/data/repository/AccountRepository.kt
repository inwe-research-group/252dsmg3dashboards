package com.dsm.a252dsmg3dashboards.data.repository

import android.util.Log
import android.view.PixelCopy
import com.dsm.a252dsmg3dashboards.data.model.EstadisticasResponse
import com.dsm.a252dsmg3dashboards.data.model.NPersonasXTipoDocumento
import com.dsm.a252dsmg3dashboards.data.model.UserProfileData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await

class AccountRepository(private val firestore: FirebaseFirestore,
                        private val functions: FirebaseFunctions
) {
    companion object{
        private const val TAG="AccountRepository"
        private const val USER_COLLECTION="usuarios"
    }
    suspend fun getAllUsers(): Result<List<UserProfileData>>{
        return try {
            val snapshot= firestore.collection(USER_COLLECTION).get().await()
            val users=snapshot.documents.mapNotNull{doc->
                val uid=doc.getString("uid")?: doc.id
                val tipoDocumento=doc.getString("tipo_documento")
                UserProfileData(
                    uid=uid,
                    tipoDocumento=tipoDocumento
                )
                }
                Result.success(users)
            }catch (e: Exception){
                Log.e(TAG,"Error al obtener la lista de usuarios")
                Result.failure(e)
            }
    }

    suspend fun getNPersonasXTipoDocumento(): Result<EstadisticasResponse> {
        return try {
            val result = functions
                .getHttpsCallable("getNPersonasXTipoDocumento")
                .call()
                .await()

            val responseMap = result.data as Map<*, *>

            val success = responseMap["success"] as? Boolean ?: false
            val dataMap = responseMap["data"] as? Map<*, *>
            val error = responseMap["error"] as? String

            // Convertir el mapa tipoDocumento -> cantidad
            val lista = dataMap?.mapNotNull { (key, value) ->
                val descripcion = key as? String
                val cantidad = (value as? Number)?.toInt()
                if (descripcion != null && cantidad != null) {
                    NPersonasXTipoDocumento(descripcion, cantidad)
                } else null
            } ?: emptyList()

            val response = EstadisticasResponse(
                success = success,
                data = lista,
                error = error
            )

            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error al invocar la función Firebase", e)
            Result.failure(e)
        }
    }
}