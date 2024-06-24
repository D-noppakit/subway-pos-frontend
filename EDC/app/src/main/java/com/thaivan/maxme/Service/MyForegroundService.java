package com.thaivan.maxme.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.thaivan.maxme.MainActivity;
import com.thaivan.maxme.R;

import java.io.IOException;


public class MyForegroundService extends Service {
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private Handler handler = new Handler();
    private Runnable runnable;
    public  MediaPlayer mediaPlayerNewOrder;


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
//        FindOrder();

        runnable = new Runnable() {
            @Override
            public void run() {
                // ใส่โค้ดที่ต้องการให้ทำงานทุก 3 วินาที
                Log.d("IntervalTask", "Task executed");

//                FindOrder();
                // ตั้งเวลาให้ทำงานซ้ำทุก 3 วินาที (3000 มิลลิวินาที)
                handler.postDelayed(this, 3000);
            }
        };

        // เริ่มการทำงานครั้งแรก
        handler.post(runnable);
        return START_NOT_STICKY;
    }

    public void FindOrder() {
        if (mediaPlayerNewOrder != null) {
            mediaPlayerNewOrder.release();
        }
        mediaPlayerNewOrder = MediaPlayer.create(this, R.raw.thereaorder);
        mediaPlayerNewOrder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // ตรวจสอบว่า MediaPlayer กำลังเล่นอยู่หรือไม่ก่อนปล่อยทรัพยากร
                if (mediaPlayerNewOrder != null && mediaPlayerNewOrder.isPlaying()) {
                    mediaPlayerNewOrder.release();
                    mediaPlayerNewOrder = null;
                }
            }
        });
        mediaPlayerNewOrder.start();

        // สร้าง Logging Interceptor
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        // สร้าง OkHttpClient พร้อม Logging Interceptor
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(logging)
//                .build();
//
//        // สร้างคำขอ GET
//        Request request = new Request.Builder()
//                .url("https://pokeapi.co/api/v2/pokemon/44/")
//                .build();
//
//        // ส่งคำขอ
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // จัดการกับข้อผิดพลาด
//                Log.e("FindOrder Error", "Network Error", e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    // จัดการกับข้อผิดพลาดของ response
//                    Log.e("FindOrder Error", "Response Code: " + response.code());
//                    return;
//                }
//
//                // จัดการกับ response ที่สำเร็จ
//                final String result = response.body().string();
//                Log.d("webview_bb", result);
//            }
//        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        if (mediaPlayerNewOrder != null) {
            mediaPlayerNewOrder.release();
            mediaPlayerNewOrder = null;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }



}
