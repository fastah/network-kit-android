<!-- MarkdownTOC -->
- [Examples with Fastah API](#list-of-examples)
- [Quick Start Guide](#quick-start-guide)
    - [Installation](#installation)
    - [Integration](#integration)
- [I want to know more!](#i-want-to-know-more)
- [Help](#want-to-report-a-problem)
- [License](#license)
<!-- /MarkdownTOC -->

<a name="list-of-examples"></a>
# Examples with Fastah API
This repository provides sample code to build network-aware Android apps using Fastah Network Kit for Android. 

Availabe examples are:
* Media pre-loader: An implementation of network-aware audio or video pre-fetching, which can improve UX or views when network conditions are flaky. See [DemoAudioVideoPreloadActivity.java](examples/app/src/main/java/com/getfastah/exampleswithfastahnetworkkit/DemoAudioVideoPreloadActivity.java)


<a name="quick-start-guide"></a>
# Quick Start Guide

Check out our **[official documentation](https://getfastah.com/docs/api)** for more in depth information on installing and using Fastah on Android.

<a name="installation"></a>
## Installation

### Dependencies in *app/build.gradle*

Add Fastah's Maven repository to the `repositories` section in *app/build.gradle*
```gradle
maven { url 'http://maven.getfastah.com/libs-release' }
```

Next, add Fastah to the `dependencies` section in *app/build.gradle*
```gradle
compile 'com.getfastah.networkkit:networkkit-android-core:1.8.+'
```

### Permissions in *app/src/main/AndroidManifest.xml*
These are also merged automatically via the library's own Manifest file via [Android's manifest merging](https://developer.android.com/studio/build/manifest-merge.html).
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

<a name="integration"></a>
## Integration

### Configuration

Before using Fastah's `MeasureManager` interface, configure the app-specific key in the [app's AndroidManifest.xml](examples/app/src/main/AndroidManifest.xml). Replace `YOUR_APPLICATION_ID` and `YOUR_APPLICATION_KEY` with the application ID (e.g "com.mycompany") and the key (e.g `fnk.XYZ`), obtained from the welcome email sent from [support@getfastah.com](mailto:support@getfastah.com). Now, build your app. 

```xml
<meta-data android:name="com.getfastah.networkkit.MeasureConfig.ApplicationName" android:value="YOUR_APPLICATION_ID" />
<meta-data android:name="com.getfastah.networkkit.MeasureConfig.ApplicationKey" android:value="YOUR_APPLICATION_KEY" />
```

You can debug initialization state for Fastah by using Android Studio's Logcat window: look for message prefix `FastahNetworkKit`.

To start using `MeasureManager`, use the `getInstance` accessor below, such as from within your [Activity](examples/app/src/main/java/com/getfastah/exampleswithfastahnetworkkit/DemoAudioVideoPreloadActivity.java).

```java
MeasureManager.getInstance(getApplicationContext()).measureOnce();
```

### Network sensing & Network watching

With the `MeasureManager` object created in [the previous step](#integration) a call to `measureOnce` is all you need to start measuring network conditions using Fastah's geo-distributed server endpoints.

```java
MeasureManager.MeasurementCompletedListener mListener;
mListener = new MeasureManager.MeasurementCompletedListener() {
    @Override
    public void onMeasurementComplete(MeasureSample sample) {
        // The sample object holds the measurement results.
    }
};

MeasureManager.getInstance().register(mListener);

// Call this as often as you like to get real-time network conditions
MeasureManager.getInstance(ctx).measureOnce();
```

<a name="i-want-to-know-more"></a>
# I want to know more!

No worries, here are some links that you will find useful:
* **[Sample app](https://github.com/fastah/network-kit-android/blob/master/examples/app/src/main/java/com/getfastah/exampleswithfastahnetworkkit/DemoAudioVideoPreloadActivity.java)**
* **[Full API Reference](https://getfastah.com/docs/api)**

Have any questions? Reach out to [support@getfastah.com](mailto:support@getfastah.com) to speak to someone smart, quickly.

<a name="want-to-report-a-problem"></a>
# Help

Please report any problems in this Github project's Issues section, or via email to [support@getfastah.com](mailto:support@getfastah.com). 
On Twitter, ping [@getfastah](https://twitter.com/getfastah)]

<a name="license"></a>
# License

```
See LICENSE File for details.
```
