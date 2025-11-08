package com.dsm.a252dsmg3dashboards.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dsm.a252dsmg3dashboards.data.model.NPersonasXTipoDocumento

@Composable
fun DashboardScreen(){
    val viewModel : DashboardViewModel = viewModel(factory = DashboardViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarDashboard()
    }
    val datos: List<NPersonasXTipoDocumento> = uiState.datosDashboard
    //llamar a los screen con deseño estadistico
    PieScreen(datos)
    BarrasScreen(datos)

}