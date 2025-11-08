package com.dsm.a252dsmg3dashboards.data

import com.dsm.a252dsmg3dashboards.data.repository.AccountRepository
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer {
    val dashboardRepository: AccountRepository
}
class DefaultAppContainer(): AppContainer{
    //declarar instancia unica de firebase
    private val firestore: FirebaseFirestore by lazy{
        FirebaseFirestore.getInstance()
    }
    override val dashboardRepository: AccountRepository by lazy{
        AccountRepository(firestore)
    }
}