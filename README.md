[![badge](https://img.shields.io/twitter/follow/teyuto?style=social)](https://twitter.com/intent/follow?screen_name=teyuto) &nbsp; [![badge](https://img.shields.io/github/stars/Teyuto/teyuto-player-sdk?style=social)](https://github.com/Teyuto/teyuto-player-sdk)
![](https://github.com/Teyuto/.github/blob/production/assets/img/banner.png?raw=true)
<h1 align="center">Teyuto ExoPlayer Analytics SDK</h1>

[Teyuto](https://teyuto.com) provides a seamless solution for managing all your video distribution needs. Whether you require video distribution in the cloud, on OTT platforms, storage, public OTT platform distribution, or secure intranet distribution, Teyuto puts everything at your fingertips, making the management of your video content effortless.

## Overview

This SDK provides analytics tracking for ExoPlayer in Android applications.

## Installation

Add the following to your project's `build.gradle`:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Then, add the dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.teyuto:teyuto-exoplayer-analytics:1.0.0'
}
```

## Usage

```kotlin
import com.teyuto.exoplayeranalytics.TeyutoPlayerAnalyticsAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class MainActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private lateinit var analytics: TeyutoPlayerAnalyticsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        player = ExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.fromUri("https://example.com/video.mp4")
        player.setMediaItem(mediaItem)

        analytics = TeyutoPlayerAnalyticsAdapter("your-auth-token-here")
        analytics.init(player, "your-video-id-here")

        player.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        analytics.destroy()
        player.release()
    }
}

```