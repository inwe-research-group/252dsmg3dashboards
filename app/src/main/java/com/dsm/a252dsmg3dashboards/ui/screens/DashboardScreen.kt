package com.dsm.a252dsmg3dashboards.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dsm.a252dsmg3dashboards.data.model.NPersonasXTipoDocumento

@Composable
fun DashboardScreen(){
    val viewModel : DashboardViewModel = viewModel(factory = DashboardViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarDashboard()
        //viewModel.cargarDashboardDesdeFunction()
    }
    val datos: List<NPersonasXTipoDocumento> = uiState.datosDashboard
    val tabs = listOf("Theras","MPAndroidChart")
    var selectedTab by remember {mutableIntStateOf(0)}
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                //Theras
                PieScreen(datos)
                BarrasScreen(datos)
                }
            1 -> {
                // MPAndroidChart
                MPPieScreen(datos)
                MPBarrasScreen(datos)
            }
        }
    }
}