# 🪴 Jardim Virtual Inteligente

> **Projeto 01** - Laboratório Criativo de Sensores em Kotlin e Jetpack Compose.

Um aplicativo Android interativo que simula um mascote-planta digital. O bem-estar e o crescimento da planta reagem em tempo real aos dados do mundo físico capturados pelos sensores do smartphone.

---

## 🚀 Funcionalidades

- **Sensor de Luz (`Sensor.TYPE_LIGHT`)**: Captura a iluminação do ambiente em *lux*. A planta ajusta o seu estado/humor dependendo da quantidade de luz recebida.
- **Acelerómetro (`Sensor.TYPE_ACCELEROMETER`)**: Deteta a inclinação/movimento do dispositivo para simular a ação de regar a planta[cite: 2].
- **Evolução Visual e Humor**: A imagem da planta evolui através de fases de crescimento (broto, média, grande) consoante a pontuação e altera o humor para murcha ou com sono conforme as condições físicas[cite: 2].
- **Edição Personalizada**: Caixa de texto interativa que permite ao utilizador renomear o seu jardim em tempo real.
- **Tratamento de Exceções**: Exibe alertas visuais limpos caso o dispositivo físico ou emulador não possua algum dos sensores necessários[cite: 2].

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **UI Framework**: Jetpack Compose (100% declarativo, sem layouts XML)[cite: 2]
- **Componentes**: `Material3`, `Scaffold`, `OutlinedTextField`, `DisposableEffect`, `State/remember`[cite: 2]
- **APIs Android**: `SensorManager`, `SensorEventListener`[cite: 2]

---

## 📋 Pré-requisitos

- **Android Studio**: Jellyfish / Hedgehog ou superior
- **Versão do SDK**: Min SDK 24+ (Android 7.0)
- **Dispositivo**: Smartphone físico Android recomendado para teste real dos sensores[cite: 2] (ou Emulador configurado com Virtual Sensors).

---

## 🖼️ Estágios de Crescimento

Para o correto funcionamento visual, certifica-te de que os seguintes ficheiros PNG estão presentes no diretório `app/src/main/res/drawable/`:

- `planta_broto.png`
- `planta_media.png`
- `planta_grande.png`
- `planta_murcha.png`

---

## 📱 Como Executar

1. Clona este repositório:
   ```bash
   git clone[ [https://github.com/teu-usuario/pdm_pjg_lista4.git](https://github.com/teu-usuario/pdm_pjg_lista4.git)
](https://github.com/PedroRicarte1912/Projeto_PDM_PlantaVirtual.git)
