package com.dsm.a252dsmg3dashboards

import android.app.Application
import com.dsm.a252dsmg3dashboards.data.AppContainer
import com.dsm.a252dsmg3dashboards.data.DefaultAppContainer

class DashboardApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate(){
        super.onCreate()
        container= DefaultAppContainer()
    }
}