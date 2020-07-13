# droplet
Droplet Development Task

## Requirements
*   [Android Studio 3.0](https://developer.android.com/studio) and above.
*   [Android buildToolsVersion 29.0.3](https://developer.android.com/studio/releases/build-tools) and above.
*   Android minSdkVersion 19.
*   Kotlin version 1.3.*.

## Features
*   Implementation of android [MVVM Architecture Pattern](https://developer.android.com/jetpack/guide).
*   Functional programming implementation by observing to data changes with [LiveData](https://codelabs.developers.google.com/codelabs/android-training-livedata-viewmodel/index.html#5).
*   Using [ViewModel](https://codelabs.developers.google.com/codelabs/android-training-livedata-viewmodel/index.html#8) to serve as the bridge between datasource and UI. ViewModel implementation also makes sure data is not lost during configurations changes as its lifecycle aware.
*   FireBase Realtime database implementation
*   Dependency injection [Koin](https://start.insert-koin.io/#/introduction)

## Testing
*   Android UI tests with [Espresso](https://developer.android.com/training/testing/espresso)


## DropBox
Find below dropBox link to release apk file to install since the SHA-1 on firebase is tagged to my release keystore
[Droplet App](https://www.dropbox.com/s/a9mkuist6orut0l/app-release.apk?dl=0)

