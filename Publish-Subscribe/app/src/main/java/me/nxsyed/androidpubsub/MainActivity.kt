package me.nxsyed.androidpubsub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import java.util.*
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val publishText = findViewById<EditText>(R.id.editTextPublish)
        val subscribeText = findViewById<TextView>(R.id.textViewSubscribe)

        var subscribeCallback: SubscribeCallback = object : SubscribeCallback()  {
            override fun status(pubnub: PubNub, status: PNStatus) {

            }
            override fun message(pubnub: PubNub, message: PNMessageResult) {
                runOnUiThread {
                    subscribeText.text = message.message.toString()
                }
            }
            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
            }
        }

        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = "sub-c-87dbd99c-e470-11e8-8d80-3ee0fe19ec50"
        pnConfiguration.publishKey = "pub-c-09557b6c-9513-400f-a915-658c0789e264"
        pnConfiguration.secretKey = "true"
        val pubNub = PubNub(pnConfiguration)

        pubNub.run {
            addListener(subscribeCallback)
            subscribe()
                    .channels(Arrays.asList("whiteboard")) // subscribe to channels
                    .execute()
        }

        publishText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                pubNub.run {
                    publish()
                            .message(publishText.text.toString())
                            .channel("whiteboard")
                            .async(object : PNCallback<PNPublishResult>() {
                                override fun onResponse(result: PNPublishResult, status: PNStatus) {
                                    if (!status.isError) {
                                        println("Message was published")
                                    }else {
                                        println("Could not publish")
                                    }
                                }
                            })
                }
                return@OnKeyListener true
            }
            false
        })


    }

}
