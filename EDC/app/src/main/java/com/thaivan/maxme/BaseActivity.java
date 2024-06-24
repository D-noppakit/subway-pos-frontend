package com.thaivan.maxme;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {
    private TextView logTv = null;
    private String className = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Exception e = new Exception();
        StackTraceElement callElement = e.getStackTrace()[1];
        className = callElement.getFileName().substring(0, callElement.getFileName().indexOf("."));
        Log.i("NewPosDemo", className + " onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("NewPosDemo", className + " onResume");
    }

    @Override
    protected void onPause() {
        Log.i("NewPosDemo", className + " onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("NewPosDemo", className + " onDestroy");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("NewPosDemo", className + " onBackPressed");
        finish();
    }

    protected void setLogTv(TextView tv) {
        this.logTv = tv;
    }

    protected void log(String msg) {
        Exception e = new Exception();
        StackTraceElement callElement = e.getStackTrace()[1];
        String classMethodName = callElement.getFileName().substring(0, callElement.getFileName().indexOf(".")) + "." + callElement.getMethodName();
        String lineNum = "(" + callElement.getFileName() + ":" + callElement.getLineNumber() + ")";

        String logMsg = classMethodName + lineNum;
        if (msg != null && !msg.isEmpty()) {
            logMsg += " : " + msg;
        }

        Log.d("NewPosDemo", logMsg);

        if (logTv != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    logTv.append(msg + "\n");
                }
            });
        }
    }
}
