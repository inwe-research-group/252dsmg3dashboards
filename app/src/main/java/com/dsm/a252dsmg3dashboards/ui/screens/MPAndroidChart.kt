package com.dsm.a252dsmg3dashboards.ui.screens

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.dsm.a252dsmg3dashboards.data.model.NPersonasXTipoDocumento
import com.dsm.a252dsmg3dashboards.utils.Utils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

// ──────────────────────────────────────────────
// PIE CHART  (MPAndroidChart)
// ──────────────────────────────────────────────

@Composable
fun MPPieScreen(data: List<NPersonasXTipoDocumento>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Gráfico Pie (MPAndroidChart)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        )
        MPPie(data)
    }
}

@Composable
fun MPPie(data: List<NPersonasXTipoDocumento>) {
    val total = data.sumOf { it.cantidad.toDouble() }.toFloat()

    // Generar colores una sola vez por recomposición
    val colors: List<Color> = remember(data) {
        data.map { Utils().colorAleatorio() }
    }
    val androidColors: List<Int> = remember(colors) {
        colors.map { it.toArgb() }
    }

    // PieEntries
    val entries = remember(data) {
        data.map { PieEntry(it.cantidad.toFloat(), it.descripcion) }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                PieChart(context).apply {
                    description.isEnabled = false
                    isRotationEnabled = true
                    isHighlightPerTapEnabled = true
                    setUsePercentValues(true)
                    setDrawEntryLabels(false)
                    setHoleColor(AndroidColor.TRANSPARENT)
                    holeRadius = 40f
                    transparentCircleRadius = 45f
                    legend.apply {
                        isEnabled = false          // usamos leyenda propia abajo
                    }
                }
            },
            update = { chart ->
                val dataSet = PieDataSet(entries, "").apply {
                    setColors(ArrayList(androidColors))
                    sliceSpace = 2f
                    valueTextSize = 12f
                    valueTextColor = AndroidColor.WHITE
                }
                chart.data = PieData(dataSet)
                chart.invalidate()
            }
        )//end AndroidView

        Spacer(modifier = Modifier.height(8.dp))

        // Leyenda manual (igual que DashboardPieScreen)
        data.forEachIndexed { index, item ->
            val porcentaje = if (total > 0) (item.cantidad / total * 100).toInt() else 0
            val color = colors.getOrElse(index) { Color.Gray }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(color, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("${item.descripcion}: ${item.cantidad} ($porcentaje%)")
            }
        }
    }
}

// ──────────────────────────────────────────────
// BAR CHART  (MPAndroidChart)
// ──────────────────────────────────────────────

@Composable
fun MPBarrasScreen(data: List<NPersonasXTipoDocumento>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Gráfico de Barras (MPAndroidChart)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 4.dp)
        )
        MPBarras(data)
    }
}

@Composable
fun MPBarras(data: List<NPersonasXTipoDocumento>) {
    val colors: List<Color> = remember(data) {
        data.map { Utils().colorAleatorio() }
    }
    val androidColors: List<Int> = remember(colors) {
        colors.map { it.toArgb() }
    }

    val entries = remember(data) {
        data.mapIndexed { index, item -> BarEntry(index.toFloat(), item.cantidad.toFloat()) }
    }
    val labels = remember(data) { data.map { it.descripcion } }

    AndroidView(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(320.dp),
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setFitBars(true)
                setDrawGridBackground(false)
                setPinchZoom(false)
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                }
                axisLeft.apply {
                    axisMinimum = 0f
                    setDrawGridLines(true)
                }
                axisRight.isEnabled = false
                legend.apply {
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    setDrawInside(false)
                }
            }
        },
        update = { chart ->
            val dataSet = BarDataSet(entries, "Cantidad").apply {
                setColors(ArrayList(androidColors))
                valueTextSize = 12f
                valueTextColor = AndroidColor.DKGRAY
            }
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chart.data = BarData(dataSet)
            chart.invalidate()
        }
    )
}
