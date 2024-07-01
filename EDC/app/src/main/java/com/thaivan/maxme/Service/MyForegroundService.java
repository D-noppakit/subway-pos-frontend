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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.thaivan.maxme.MainActivity;
import com.thaivan.maxme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MyForegroundService extends Service {
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private Handler handler = new Handler();
    private Runnable runnable;
    public MediaPlayer mediaPlayerNewOrder;
    public static boolean callAudio = false;

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

        runnable = new Runnable() {
            @Override
            public void run() {

                String stringValue = String.valueOf(MyForegroundService.callAudio);
                Log.d("IntervalTask", "Task executed "+ stringValue);
                FindOrder();
                if (MyForegroundService.callAudio){
                    MyForegroundService.this.runAudio();
                }
                handler.postDelayed(this, 3000);
            }
        };

        handler.post(runnable);
        return START_NOT_STICKY;
    }

    public void FindOrder() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImZlNmJlM2E5MjMxY2M4MWJkODc2NzkwMDRiNDVmMWFlIiwibG9jYXRpb24iOiJmZTZiZTNhOTIzMWNjODFiZDg3Njc5MDA0YjQ1ZjFhZSIsImJyYW5jaF9uYW1lIjoiU3RvcmUwMSIsImJyYW5jaF9pZCI6MSwiYWRkcmVzcyI6IkNXIFRvd2VyIDAxIFRlc3QiLCJpYXQiOjE3MTk4MzMyNzMsImV4cCI6MTcxOTkxOTY3M30.GcuTSEDTXqNw1FU-NzlVEcuI4nS10YCNZzj_Mc4ohEE") // เพิ่ม token ใน body
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.11.43:3000/api/pos/get/order")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FindOrder Error", "Network Error", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.e("FindOrder Error", "Response Code: " + response.code());
                    return;
                }

                final String result = response.body().string();
                Log.d("webview_bb", result);

                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray orders = jsonResponse.getJSONArray("result");

                    for (int i = 0; i < orders.length(); i++) {
                        JSONObject order = orders.getJSONObject(i);
                        String status = order.getString("status");
                        boolean alert = order.getBoolean("alert");

                        Log.d("FilteredOrder", String.valueOf(alert));

                        if ("start-order".equals(status) && alert) {
                            MyForegroundService.callAudio = true;
                            Log.d("FilteredOrder", String.valueOf(MyForegroundService.callAudio));

//                            Log.d("FilteredOrder", order.toString());
                            return;
                        }
                    }
                } catch (JSONException e) {
                    Log.e("JSONParseError", "Error parsing JSON", e);
                }
            }

        });

    }
    public void runAudio() {
        if (mediaPlayerNewOrder != null) {
            mediaPlayerNewOrder.release();
        }

        mediaPlayerNewOrder = MediaPlayer.create(MyForegroundService.this, R.raw.thereaorder);
        mediaPlayerNewOrder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mediaPlayerNewOrder != null) {
                    mediaPlayerNewOrder.release();
                    mediaPlayerNewOrder = null;
                }
            }
        });

        mediaPlayerNewOrder.start();
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
