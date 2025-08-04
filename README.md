# 🌱 Plant Disease Detection Android App

A comprehensive Android application that uses machine learning to detect plant diseases from leaf images. The app combines TensorFlow Lite for image classification with AI-powered disease information retrieval.

## ScreenShots

<img src="https://github.com/user-attachments/assets/df11a8ca-0cbc-4644-8b87-c6b4391adba7" alt="Image 1" width="300">
<img src="https://github.com/user-attachments/assets/2ef9af63-8384-4ea7-9751-3bec4420ae95" alt="Image 2" width="300">
<img src="https://github.com/user-attachments/assets/41a706a5-6b1f-4063-806f-aa350c52a167" alt="Image 3" width="300">
<img src="https://github.com/user-attachments/assets/49100780-d04b-43e3-a1e8-cb5748a11634" alt="Image 4" width="300">
<img src="https://github.com/user-attachments/assets/e9452541-e9f9-4d1e-94a7-8d53e180bc7e" alt="Image 5" width="300">

## 📱 Features

### Core Functionality
- **Image Capture**: Take photos directly using the device camera
- **Gallery Selection**: Choose images from device gallery
- **Disease Detection**: Analyze plant leaves for 38 different disease categories
- **AI-Powered Information**: Get detailed symptoms and management advice for detected diseases
- **Real-time Processing**: Fast inference using TensorFlow Lite


### Supported Plant Diseases
The app can detect diseases in the following plant categories:
- **Apple**: Scab, Black Rot, Cedar Apple Rust, Healthy
- **Blueberry**: Healthy
- **Cherry**: Powdery Mildew, Healthy
- **Corn/Maize**: Cercospora Leaf Spot, Common Rust, Northern Leaf Blight, Healthy
- **Grape**: Black Rot, Esca Black Measles, Leaf Blight, Healthy
- **Orange**: Huanglongbing (Citrus Greening)
- **Peach**: Bacterial Spot, Healthy
- **Pepper**: Bacterial Spot, Healthy
- **Potato**: Early Blight, Late Blight, Healthy
- **Raspberry**: Healthy
- **Soybean**: Healthy
- **Squash**: Powdery Mildew
- **Strawberry**: Leaf Scorch, Healthy
- **Tomato**: Bacterial Spot, Early Blight, Late Blight, Leaf Mold, Septoria Leaf Spot, Spider Mites, Target Spot, Yellow Leaf Curl Virus, Mosaic Virus, Healthy

## 🛠️ Technical Stack

### Core Technologies
- **Language**: Kotlin
- **UI Framework**: Android Views with Material Design 3
- **ML Framework**: TensorFlow Lite 2.13.0
- **AI Integration**: Together AI API (Mistral-7B-Instruct-v0.2)
- **Networking**: Retrofit2 + OkHttp3
- **Architecture**: MVVM with Coroutines

### Dependencies
```kotlin
// Core Android
implementation("androidx.core:core-ktx")
implementation("androidx.lifecycle:lifecycle-runtime-ktx")
implementation("androidx.activity:activity-compose")

// UI Components
implementation("com.google.android.material:material:1.11.0")
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("androidx.constraintlayout:constraintlayout:2.1.4")

// TensorFlow Lite
implementation("org.tensorflow:tensorflow-lite:2.13.0")
implementation("org.tensorflow:tensorflow-lite-support:0.3.1")
implementation("org.tensorflow:tensorflow-lite-gpu:2.13.0")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.10.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
```

## 📁 Project Structure

```
PlantDiseasedetection/
├── app/
│   ├── build.gradle.kts                 # App-level build configuration
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml      # App permissions and activities
│   │   │   ├── assets/                  # ML models and data
│   │   │   │   ├── plant_disease_model.tflite  # Main TensorFlow model
│   │   │   │   ├── labels.txt           # Disease class labels
│   │   │   │   ├── data.json            # Disease information database
│   │   │   │   └── sample_img.jpg       # Sample image for testing
│   │   │   ├── java/com/example/plantdiseasedetection/
│   │   │   │   ├── MainActivity.kt      # Main UI controller
│   │   │   │   ├── Classifier.kt        # TensorFlow Lite inference
│   │   │   │   ├── AIService.kt         # AI API integration
│   │   │   │   └── AIConfig.kt         # AI configuration
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── main_activity.xml        # Main UI layout
│   │   │   │   │   └── detail_dialog_act.xml    # Disease info dialog
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml              # String resources
│   │   │   │   │   ├── colors.xml               # Color definitions
│   │   │   │   │   └── themes.xml               # App themes
│   │   │   │   └── drawable/                    # Icons and graphics
│   │   │   └── xml/                     # Backup and data extraction rules
│   │   ├── androidTest/                 # Instrumented tests
│   │   └── test/                        # Unit tests
│   └── proguard-rules.pro              # Code obfuscation rules
├── build.gradle.kts                     # Project-level build configuration
├── gradle/                              # Gradle wrapper and dependencies
├── gradlew                              # Gradle wrapper script (Unix)
├── gradlew.bat                          # Gradle wrapper script (Windows)
├── gradle.properties                    # Gradle properties
└── settings.gradle.kts                  # Project settings
```

## 🚀 Setup and Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 24+ (Android 7.0+)
- Minimum 2GB RAM
- Internet connection for AI features

### Build Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd PlantDiseasedetection
   ```

2. **Configure AI API Key**
   - Open `app/src/main/java/com/example/plantdiseasedetection/AIConfig.kt`
   - Replace the placeholder API key with your Together AI API key:
   ```kotlin
   const val AI_API_KEY = "your-actual-api-key-here"
   ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Install on device**
   ```bash
   ./gradlew installDebug
   ```

### API Configuration
The app uses Together AI for disease information. To set up:

1. Sign up at [Together AI](https://together.ai)
2. Get your API key from the dashboard
3. Update `AIConfig.kt` with your key
4. The app will use Mistral-7B-Instruct-v0.2 model for generating disease information

## 📱 Usage Guide

### Basic Workflow
1. **Launch the app** - The main screen shows a sample image
2. **Select an image**:
   - Tap "Take Photo" to capture a new image
   - Tap "Select Photo" to choose from gallery
3. **Detect disease** - Tap "Detect Image" to analyze the plant
4. **View results** - The detected disease appears in the results section
5. **Get detailed info** - Tap "Disease Info" for symptoms and management advice

### Features in Detail

#### Image Processing
- **Input Size**: 224x224 pixels (automatically resized)
- **Format**: RGB color space
- **Preprocessing**: Normalization (0-255 range)
- **Model**: Custom TensorFlow Lite model trained on plant disease dataset

#### Disease Detection
- **Confidence Threshold**: 0.5 (50%)
- **Max Results**: Top 3 predictions
- **Processing Time**: <2 seconds on modern devices
- **Accuracy**: Varies by disease type and image quality

#### AI Information Retrieval
- **Model**: Mistral-7B-Instruct-v0.2
- **Response Format**: JSON with symptoms and management
- **Fallback**: Regex parsing if JSON fails
- **Timeout**: 30 seconds per request

## 🔧 Technical Implementation

### Machine Learning Pipeline

#### Classifier.kt
```kotlin
class Classifier(assetManager: AssetManager, modelPath: String, labelPath: String, inputSize: Int) {
    // Loads TensorFlow Lite model and labels
    // Processes images to 224x224 RGB format
    // Returns top predictions with confidence scores
}
```

#### Image Processing
1. **Resize**: Scale to 224x224 pixels
2. **Normalize**: Convert to float values (0-1 range)
3. **Inference**: Run through TensorFlow Lite model
4. **Post-process**: Sort by confidence, filter by threshold

### AI Integration

#### AIService.kt
```kotlin
class AIService {
    suspend fun getPlantDiseaseInfo(diseaseName: String): DiseaseInfo {
        // Sends prompt to Together AI API
        // Parses JSON response for symptoms and management
        // Handles errors and timeouts gracefully
    }
}
```

### UI Architecture

#### MainActivity.kt
- **View Binding**: Type-safe view access
- **Coroutines**: Async operations for AI calls
- **Error Handling**: Comprehensive exception management
- **State Management**: Loading states and result display

## 🎨 UI/UX Design

### Material Design 3
- **Color Scheme**: Custom primary/secondary colors
- **Typography**: Consistent text hierarchy
- **Components**: Material buttons, cards, and progress indicators
- **Responsive**: Adapts to different screen sizes

### Layout Structure
1. **App Bar**: Title and branding
2. **Header Card**: App description and instructions
3. **Image Display**: Photo preview with placeholder
4. **Action Buttons**: Camera, gallery, detect, and info buttons
5. **Results Card**: Detection results display
6. **Progress Indicator**: Loading animation during processing

## 📊 Performance Considerations

### Optimization Features
- **GPU Acceleration**: TensorFlow Lite GPU delegate
- **Memory Management**: Efficient bitmap handling
- **Async Processing**: Non-blocking UI operations
- **Error Recovery**: Graceful failure handling

### Resource Usage
- **Model Size**: ~11MB (plant_disease_model.tflite)
- **Memory**: ~50MB peak usage
- **Storage**: ~15MB total app size
- **Network**: Minimal usage (only for AI features)

## 🧪 Testing

### Test Structure
```
src/
├── androidTest/          # Instrumented tests
│   └── ExampleInstrumentedTest.kt
└── test/                 # Unit tests
    └── ExampleUnitTest.kt
```

### Testing Areas
- **Unit Tests**: Classifier logic, AI service
- **Integration Tests**: End-to-end workflows
- **UI Tests**: Button interactions, image processing
- **Performance Tests**: Inference speed, memory usage

## 🔒 Permissions

### Required Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Optional Permissions
- **Camera**: For taking photos (handled by system intent)
- **Storage**: For gallery access (handled by system intent)

## 🚨 Troubleshooting

### Common Issues

#### Model Loading Errors
- **Symptom**: "Error initializing classifier"
- **Solution**: Check if model files are in assets folder
- **Prevention**: Verify build process includes assets

#### AI API Errors
- **Symptom**: "Failed to fetch disease info"
- **Solution**: Verify API key in AIConfig.kt
- **Prevention**: Test API connectivity before release

#### Image Processing Errors
- **Symptom**: "Error processing image"
- **Solution**: Check image format and size
- **Prevention**: Add image validation

### Debug Information
- **Log Tag**: "MainActivity", "AIService", "Classifier"
- **Error Handling**: Comprehensive try-catch blocks
- **User Feedback**: Toast messages for all errors

## 🔄 Future Enhancements

### Planned Features
- **Offline Mode**: Local disease database
- **Batch Processing**: Multiple image analysis
- **History**: Save previous detections
- **Export**: Share results and recommendations
- **Custom Models**: User-specific training

### Technical Improvements
- **Model Optimization**: Quantization for smaller size
- **Caching**: Local AI response storage
- **Analytics**: Usage tracking and insights
- **Accessibility**: Screen reader support

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📞 Support

For issues and questions:
- **GitHub Issues**: Report bugs and feature requests
- **Documentation**: Check inline code comments
- **Community**: Join our developer community

---

**Note**: This app requires an active internet connection for AI-powered disease information. The core disease detection works offline using the TensorFlow Lite model. 
