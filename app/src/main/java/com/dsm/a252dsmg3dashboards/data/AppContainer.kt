package com.dsm.a252dsmg3dashboards.data

import com.dsm.a252dsmg3dashboards.data.repository.AccountRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

interface AppContainer {
    val dashboardRepository: AccountRepository
}
class DefaultAppContainer(): AppContainer{
    //declarar instancia unica de firebase
    private val firestore: FirebaseFirestore by lazy{
        FirebaseFirestore.getInstance()
    }
    //Instancia única de Firebase Functions (región: us-central1)
    private val functions: FirebaseFunctions by lazy {
        Firebase.functions("us-central1")
    }
    override val dashboardRepository: AccountRepository by lazy{
        AccountRepository(firestore,functions)
    }
}