package com.example.exit_saferoute

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG:String = "TestTag"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        // 앱이 foreground 상태에 있을 때 FCM 알림을 받았다면 onMessageReceived() 콜백 메소드가 호출됨으로써 FCM 알림이 대신된다.
        Log.d("onMessageReceived콜백호출됨", "From: ${remoteMessage.from}")

        // 메시지 유형이 데이터 메시지일 경우
        // Check if message contains a data payload.
        var fcmDataTitle: String = ""
        var fcmDataMessage: String = ""
        var fcmDataClickAction: String = ""
        var dataInfo: Map<String,String> = mapOf()
        if (remoteMessage.data.isNotEmpty()) {
            //fcmBody = remoteMessage.data.get("Body").toString()
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            fcmDataTitle = remoteMessage.data.get("title").toString()
            fcmDataMessage = remoteMessage.data.get("message").toString()
            fcmDataClickAction = remoteMessage.data.get("clickAction").toString()
            dataInfo = mapOf("title" to fcmDataTitle, "body" to fcmDataMessage, "clickaction" to fcmDataClickAction)
            sendNotification(dataInfo)
        }

        // 메시지 유형이 알림 메시지일 경우
        // Check if message contains a notification payload.
        // Set FCM title, body to android notification
        var notificationInfo: Map<String, String> = mapOf()
        remoteMessage.notification?.let {
            notificationInfo = mapOf(
                "title" to it.title.toString(),
                "body" to it.body.toString()
            )
            sendNotification(notificationInfo)
        }
    }


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        /** 변경된 토큰 가져오기 및 확인하기 */
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("newFCMToken", token.toString())
        })
    }

    /**
     * 푸시 메시지의 세부 설정을 하고, 안드로이드 앱에 푸시 메시지를 보내는 메소드
     *
     * onMessagedReceived() 콜백 메소드에서 FCM이 보낸 메시지의 title, body 등을 알아와 sendNotification()의 매개변수로 넘기면 됨
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: Map<String, String>) {
        var intent: Intent
        if(messageBody["clickaction"] == "EmergencyMap") {
            intent = Intent(this, EmergencyMap::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("position", "광주광역시 북구")

        }
        else{
            intent = Intent(this, InitialActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // icon, color는 메타 데이터에서 설정한 것으로 설정해주면 된다.
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(messageBody["title"])
            .setContentText(messageBody["body"])
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}