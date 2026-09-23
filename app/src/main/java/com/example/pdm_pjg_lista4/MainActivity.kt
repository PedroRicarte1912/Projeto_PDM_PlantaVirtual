package com.example.pdm_pjg_lista4

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pdm_pjg_lista4.ui.theme.PDM_PJG_LISTA4Theme
import kotlin.math.sqrt

// === PALETA DE CORES DA INTERFACE ===
val CorFundoVerde = Color(0xFF0A3235)
val CorCardPlanta = Color(0xFF14474B)
val CorTextoSecundario = Color(0xFFA0C3C5)
val CorVerdeClaro = Color(0xFF63D9A2)
val CorBarraFundo = Color(0xFF1F4145)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PDM_PJG_LISTA4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaJardimVirtual(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TelaJardimVirtual(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    // --- ESTADOS DO APLICATIVO ---
    var nomeJardim by remember { mutableStateOf("Meu jardim") }
    var luxValue by remember { mutableFloatStateOf(0f) }
    var felicidade by remember { mutableFloatStateOf(0.5f) } // 0.0 a 1.0 (50% inicial)
    var pontuacaoCrescimento by remember { mutableIntStateOf(0) }

    // Verificação de sensores
    var temSensorLuz by remember { mutableStateOf(true) }
    var temAcelerometro by remember { mutableStateOf(true) }

    // --- GERENCIAMENTO DOS SENSORES (CICLO DE VIDA) ---
    DisposableEffect(Unit) {
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        temSensorLuz = lightSensor != null
        temAcelerometro = accelSensor != null

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    when (it.sensor.type) {
                        // 1. SENSOR DE LUZ
                        Sensor.TYPE_LIGHT -> {
                            luxValue = it.values[0]
                        }
                        // 2. ACELERÔMETRO (REGAR)
                        Sensor.TYPE_ACCELEROMETER -> {
                            val x = it.values[0]
                            val y = it.values[1]
                            val z = it.values[2]
                            val aceleracao = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

                            if (aceleracao > 4.5f) {
                                felicidade = (felicidade + 0.05f).coerceAtMost(1.0f)
                                pontuacaoCrescimento += 2
                            }
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        lightSensor?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
        accelSensor?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // --- LÓGICA DO HUMOR DA PLANTA ---
    val estadoHumor = remember(luxValue, felicidade) {
        when {
            luxValue < 50f -> "Com sono (Pouca Luz)"
            felicidade < 0.3f -> "Murcha (Precisa de cuidado!)"
            else -> "Feliz e Saudável!"
        }
    }

    // --- SELEÇÃO DA IMAGEM DA PLANTA CONFORME CRESCIMENTO E ESTADO ---
    val imagemPlantaRes = remember(pontuacaoCrescimento, estadoHumor) {
        when {
            estadoHumor.contains("Murcha") -> R.drawable.planta_murcha
            pontuacaoCrescimento < 15 -> R.drawable.planta_broto
            pontuacaoCrescimento < 40 -> R.drawable.planta_media
            else -> R.drawable.planta_grande
        }
    }

    // --- INTERFACE COMPOSE ---
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CorFundoVerde)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // 1. TÍTULO EDITÁVEL E LEITURA DE LUZ
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nomeJardim,
                    onValueChange = { nomeJardim = it },
                    label = { Text("Nome do Jardim", color = CorTextoSecundario) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = CorVerdeClaro,
                        unfocusedBorderColor = CorTextoSecundario
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Luz detectada",
                    color = CorTextoSecundario,
                    fontSize = 14.sp
                )
                Text(
                    text = if (temSensorLuz) "${luxValue.toInt()} lux" else "Sensor indisponível",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // AVISO CASO NÃO HAJA SENSOR
            if (!temSensorLuz || !temAcelerometro) {
                Surface(
                    color = Color(0xFF661111),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Aviso: Algum sensor necessário não foi detectado no seu aparelho.",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // 2. CARD DA PLANTA COM A IMAGEM EVOLUTIVA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(CorCardPlanta, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = imagemPlantaRes),
                        contentDescription = "Planta do Jardim",
                        modifier = Modifier.size(110.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = estadoHumor,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Pontos de Crescimento: $pontuacaoCrescimento",
                        color = CorTextoSecundario,
                        fontSize = 12.sp
                    )
                }
            }

            // 3. BARRA DE FELICIDADE E BOTÃO CUIDAR
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Felicidade da planta",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${(felicidade * 100).toInt()}%",
                            color = CorTextoSecundario,
                            fontSize = 14.sp
                        )
                    }

                    // Barra customizada
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(CorBarraFundo, shape = RoundedCornerShape(6.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(felicidade)
                                .background(CorVerdeClaro, shape = RoundedCornerShape(6.dp))
                        )
                    }
                    Text(
                        text = "💡 Dica: Incline o celular para regar!",
                        color = CorTextoSecundario,
                        fontSize = 11.sp
                    )
                }

                // Botão "CUIDAR"
                Button(
                    onClick = {
                        felicidade = (felicidade + 0.1f).coerceAtMost(1.0f)
                        pontuacaoCrescimento += 5
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CorVerdeClaro),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "CUIDAR",
                        color = CorFundoVerde,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaJardimVirtualPreview() {
    PDM_PJG_LISTA4Theme {
        TelaJardimVirtual()
    }
}