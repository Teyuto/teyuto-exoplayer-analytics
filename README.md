[![badge](https://img.shields.io/twitter/follow/teyuto?style=social)](https://twitter.com/intent/follow?screen_name=teyuto) &nbsp; [![badge](https://img.shields.io/github/stars/Teyuto/teyuto-player-sdk?style=social)](https://github.com/Teyuto/teyuto-player-sdk)
![](https://github.com/Teyuto/.github/blob/production/assets/img/banner.png?raw=true)
<h1 align="center">Teyuto ExoPlayer Analytics SDK</h1>

[Teyuto](https://teyuto.com) provides a seamless solution for managing all your video distribution needs. Whether you require video distribution in the cloud, on OTT platforms, storage, public OTT platform distribution, or secure intranet distribution, Teyuto puts everything at your fingertips, making the management of your video content effortless.

## Overview

This SDK provides analytical monitoring for Teyuto with ExoPlayer in Android applications.

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

The Teyuto ExoPlayer Analytics SDK now supports optional authentication tokens. You can initialize the SDK with or without a token, depending on your requirements.

### Initializing with a Token

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

        analytics = TeyutoPlayerAnalyticsAdapter("channel-public", "user-auth-token")
        analytics.init(player, "video-id")

        player.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        analytics.destroy()
        player.release()
    }
}
```

### Initializing without a Token

If you don't need to use an authentication token, you can initialize the SDK with just the channel:

```kotlin
analytics = TeyutoPlayerAnalyticsAdapter("your-channel-public")
analytics.init(player, "your-video-id-here")
```

## Note on Authentication

The token is optional when initializing the TeyutoPlayerAnalyticsAdapter. If you provide a token, it will be included in the API requests. If you don't provide a token, the requests will be made without authentication.

## Additional Information

For more details on how to use the Teyuto ExoPlayer Analytics SDK, including advanced features and configurations, please refer to our [official documentation](https://docs.teyuto.com/developer).

## Support

If you encounter any issues or have questions about using the SDK, please open an issue on our GitHub repository or contact our support team at support@teyuto.com.

## License

This SDK is released under the [MIT License](LICENSE).