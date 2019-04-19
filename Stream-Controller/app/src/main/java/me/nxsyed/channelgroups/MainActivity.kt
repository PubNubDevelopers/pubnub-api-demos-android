package me.nxsyed.channelgroups

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import java.util.*
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val subscribeText = findViewById<TextView>(R.id.textViewSubscribe)
        val ch1Button = findViewById<Button>(R.id.buttonCh1)
        val ch2Button = findViewById<Button>(R.id.buttonCh2)



        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = "YOUR PUBNUB SUBSCRIBE KEY HERE"
        pnConfiguration.publishKey = "YOUR PUBNUB PUBLISH KEY HERE"
        val pubNub = PubNub(pnConfiguration)

        pubNub.addChannelsToChannelGroup()
                .channelGroup("ExampleGroup")
                .channels(Arrays.asList("ch1", "ch2"))
                .async(object: PNCallback<PNChannelGroupsAddChannelResult>(){
                    override fun onResponse(result: PNChannelGroupsAddChannelResult?, status: PNStatus){

                    }
                })


        pubNub.run {
            addListener(object : SubscribeCallback()  {
                override fun status(pubnub: PubNub, status: PNStatus) {

                }
                override fun message(pubnub: PubNub, message: PNMessageResult) {
                    println(message)
                    runOnUiThread {
                        subscribeText.text = "Channel: ${message.channel} \n Message: ${message.message.toString()}"
                    }
                }
                override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
                }
            })
            subscribe()
                    .channels(Arrays.asList("whiteboard")) // subscribe to channels
                    .channelGroups(Arrays.asList("ExampleGroup"))
                    .execute()
        }

        ch1Button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                pubNub.run {
                    publish()
                            .message("Message from Ch1")
                            .channel("ch1")
                            .async(object : PNCallback<PNPublishResult>() {
                                override fun onResponse(result: PNPublishResult?, status: PNStatus) {
                                    if (!status.isError) {
                                        println("Message was published")
                                    }else {
                                        println("Could not publish")
                                    }
                                }
                            })
                }

            }
        })

        ch2Button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                pubNub.run {
                    publish()
                            .message("Message from Ch2")
                            .channel("ch2")
                            .async(object : PNCallback<PNPublishResult>() {
                                override fun onResponse(result: PNPublishResult?, status: PNStatus) {
                                    if (!status.isError) {
                                        println("Message was published")
                                    }else {
                                        println("Could not publish")
                                    }
                                }
                            })
                }

            }
        })



    }

}
