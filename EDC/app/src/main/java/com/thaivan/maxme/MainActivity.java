package com.thaivan.maxme;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.koushikdutta.ion.Ion;
import com.thaivan.maxme.Service.MyForegroundService;

public class MainActivity extends AppCompatActivity {
    public static MediaPlayer mediaPlayerNewOrder;
    private ProgressBar progressBar;
    private ImageView progressImage;
    private View backgroundView ;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subway);
        frameLayout = findViewById(R.id.frame_layout_loading);
        frameLayout.setVisibility(View.VISIBLE);
        progressImage = findViewById(R.id.progress_image);
        progressImage.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(R.drawable.loading_gif) // แทนที่ R.drawable.loading_gif ด้วยพาธของไฟล์ GIF ของคุณ
                .into(progressImage);

        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        // Start playing the audio
        mediaPlayerNewOrder = MediaPlayer.create(this, R.raw.thereaorder);
        // ค้นหา WebView ในเลย์เอาต์
        WebView myWebView = findViewById(R.id.webview);
        // เปิดการตั้งค่า JavaScript
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);


        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Hide ProgressBar when page is fully loaded
                progressImage.setVisibility(ProgressBar.GONE);
                frameLayout.setVisibility(View.GONE);
            }
        });

        myWebView.addJavascriptInterface(new WebAppInterface(this), "AndroidFunction");

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(android.webkit.ConsoleMessage consoleMessage) {
                Log.d("webview", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressImage.getVisibility() == View.GONE) {
                    progressImage.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                } else if (progress == 100) {
                    progressImage.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.GONE);
                }
            }
        });
        // โหลด URL ที่ต้องการ rr
//         myWebView.loadUrl("file:///android_asset/pos/index.html");
        myWebView.loadUrl("http://192.168.11.43:5500/EDC/app/src/main/assets/pos/order.html");

    }
    public class WebAppInterface {
        private final Context mContext;

        WebAppInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void showToast(String message) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void printSlipNative(String qrcode) {
           PrintActivity.printTempDom("try_print",qrcode,MainActivity.this);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed
        if (mediaPlayerNewOrder != null) {
            mediaPlayerNewOrder.release();
            mediaPlayerNewOrder = null;
        }
    }


}