package me.nxsyed.pam

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.access.Grant
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import java.util.*
import android.os.Build
import android.widget.TextView
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val subscribeText = findViewById<TextView>(R.id.textViewSubscribe)

        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = "YOUR PUBNUB SUBSCRIBE KEY HERE"
        pnConfiguration.publishKey = "YOUR PUBNUB PUBLISH KEY HERE"
        pnConfiguration.secretKey = "YOUR PUBNUB SECRET KEY HERE"
        pnConfiguration.isSecure = true
        pnConfiguration.authKey = "auth-key"
        val pubNub = PubNub(pnConfiguration)

        pubNub.run {
            grant()
                    .channels(Arrays.asList("whiteboard"))
                    .authKeys(Arrays.asList("auth-key"))
                    .write(isEmulator()) // allow those keys to write (false by default)
                    .manage(isEmulator()) // allow those keys to manage channel groups (false by default)
                    .read(isEmulator()) // allow keys to read the subscribe feed (false by default)
                    .ttl(12337) // how long those keys will remain valid (0 for eternity)
                    .async(object: PNCallback<PNAccessManagerGrantResult>() {
                        override fun onResponse(result: PNAccessManagerGrantResult?, status: PNStatus) {
                            // PNAccessManagerGrantResult is a parsed and abstracted response from server
                        }
                    })
            addListener(object: SubscribeCallback(){
                override fun status(pubnub: PubNub, status: PNStatus) {

                }
                override fun message(pubnub: PubNub, message: PNMessageResult) {
                    Log.d("tag", message.toString())
                    runOnUiThread {
                        subscribeText.text = message.message.asString
                    }
                }
                override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
                }
            })
            subscribe()
                    .channels(Arrays.asList("whiteboard")) // subscribe to channels
                    .execute()
            publish()
                    .message("I have permissions Granted!")
                    .channel("whiteboard")
                    .async(object : PNCallback<PNPublishResult>() {
                        override fun onResponse(result: PNPublishResult, status: PNStatus) {
                            Log.d("response", status.toString())
                        }
                    })
        }

    }
    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }
}

