plugins {
    id("com.android.application")

    //FIREBASE IMPLEMENTATION
    //Add Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.grimpeurscyclingclub"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.grimpeurscyclingclub"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {


    // Required -- JUnit 4 framework
    testImplementation("junit:junit:4.12")
    // Optional -- Mockito framework
    testImplementation("org.mockito:mockito-core:2.5.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")



    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //FIREBASE IMPLEMENTATION
    implementation("com.google.firebase:firebase-firestore:24.8.1")
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.google.android.gms:play-services-tasks:18.0.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //TODO: REMOVE UNUSED IMPLEMENTATIONS
    //FIREBASE IMPLEMENTATION
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.3.1")) //Required
    
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics-ktx") //Required

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, we don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database")

    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.google.firebase:firebase-auth:18.0.0")
    implementation("com.google.android.gms:play-services-gcm:17.0.0")
}