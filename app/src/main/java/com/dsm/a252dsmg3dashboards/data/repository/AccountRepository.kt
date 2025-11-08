package com.dsm.a252dsmg3dashboards.data.repository

import android.util.Log
import android.view.PixelCopy
import com.dsm.a252dsmg3dashboards.data.model.UserProfileData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AccountRepository(private val firestore: FirebaseFirestore) {
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
}