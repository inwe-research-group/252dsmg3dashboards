package com.dsm.a252dsmg3dashboards.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dsm.a252dsmg3dashboards.DashboardApplication
import com.dsm.a252dsmg3dashboards.data.model.NPersonasXTipoDocumento
import com.dsm.a252dsmg3dashboards.data.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val datosDashboard: List<NPersonasXTipoDocumento> = listOf(),
    val flag_error_dashboard: Boolean = false,
)
class DashboardViewModel(private val dashboardRepository: AccountRepository): ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun cargarDashboard(){
        viewModelScope.launch {
            val result = dashboardRepository.getAllUsers()
            result.onSuccess { users->
                //agrupar usuarios por tipo de documento
                val resumen = users
                    .filter { !it.tipoDocumento.isNullOrBlank() }//excluir nulos o vacios
                    .groupBy { it.tipoDocumento!! }
                    .map {(tipo,lista)->
                        NPersonasXTipoDocumento(tipo,lista.size)
                    }
                //Actualizar el estado de IU
                _uiState.value=_uiState.value.copy(
                    datosDashboard=resumen,
                    flag_error_dashboard=false
                )
            }.onFailure {
                //Actualizar el estado de IU
                _uiState.value=_uiState.value.copy(
                    flag_error_dashboard=true
                )
            }
        }

    }

    fun resetFlags(){
        _uiState.value=_uiState.value.copy(
            flag_error_dashboard=false
        )
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as DashboardApplication)

                val dashboardRepository = application.container.dashboardRepository
                DashboardViewModel(dashboardRepository= dashboardRepository)
            }
        }

    }



}