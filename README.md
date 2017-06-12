<!-- MarkdownTOC -->

- [Examples](#list-of-examples)
- [Quick Start Guide](#quick-start-guide)
    - [Installation](#installation)
    - [Integration](#integration)
- [I want to know more!](#i-want-to-know-more)
- [Want to Contribute?](#want-to-report-a-problem)
- [License](#license)

<!-- /MarkdownTOC -->

<a name="list-of-examples"></a>
# Examples with Fastah Network Kit
This repository provides sample code to builds solutions and behaviours using Fastah Network Kit for Android. Availabe examples are:
* Video pre-loader: tells an app when it's a good time to pre-fetch audio or video media to the device, thereby improving UX or engagement when network conditions are flaky. See [DemoAudioVideoPreloadActivity.java](examples/app/src/main/java/com/getfastah/exampleswithfastahnetworkkit/DemoAudioVideoPreloadActivity.java)

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
compile 'com.getfastah.networkkit:networkkit-android-core:0.11.+'
```

### Permissions in *app/src/main/AndroidManifest.xml*
These are also merged automatically via the library's own Manifest file via [Android's manifest merging](https://developer.android.com/studio/build/manifest-merge.html).
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

<a name="integration"></a>
## Integration

### Initialization

Initialize Fastah in your [main activity](examples/app/src/main/java/com/getfastah/exampleswithfastahnetworkkit/DemoAudioVideoPreloadActivity.java). Usually this should be done in [onCreate](https://developer.android.com/reference/android/app/Activity.html#onCreate(android.os.Bundle)).

```java
MeasureManager.getInstance().init(this);
```
Before the call the above initialization methodthough, you have to configure your app-specific key in your app's *app/src/main/AndroidManifest.xml*. Replace `YOUR_APPLICATION_KEY` with the key provided to you by support@getfastah.com, and the corresponding Google Play Store application ID (e.g com.organization.myapp) in place of YOUR_APPLICATION_ID below. 

```xml
<meta-data android:name="com.getfastah.networkkit.MeasureConfig.ApplicationName" android:value="YOUR_APPLICATION_ID" />
<meta-data android:name="com.getfastah.networkkit.MeasureConfig.ApplicationKey" android:value="YOUR_APPLICATION_KEY" />
```

### Tracking

With the `MeasureManager` object created in [the last step](#integration) a call to `measureOnce` is all you need to start measuring network conditions using Fastah's geo-distributed server endpoints.

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
MeasureManager.getInstance().measureOnce(ctx);
```

<a name="i-want-to-know-more"></a>
# I want to know more!

No worries, here are some links that you will find useful:
* **[Sample app](https://github.com/fastah/network-kit-android/blob/master/examples/app/src/main/java/com/getfastah/exampleswithfastahnetworkkit/DemoAudioVideoPreloadActivity.java)**
* **[Full API Reference](https://getfastah.com/docs/api)**

Have any questions? Reach out to [support@getfastah.com](mailto:support@getfastah.com) to speak to someone smart, quickly.

<a name="want-to-report-a-problem"></a>
# Help!

roblem descriptions in the Issue tracker, or via [support@getfastah.com](mailto:support@getfastah.com)

<a name="license"></a>
# License

```
See LICENSE File for details.
```
