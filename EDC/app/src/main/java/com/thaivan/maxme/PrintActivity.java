package com.thaivan.maxme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pos.device.printer.PrintCanvas;
import com.pos.device.printer.PrintTask;
import com.pos.device.printer.Printer;
import com.pos.device.printer.PrinterCallback;

import org.w3c.dom.Text;

public class PrintActivity extends BaseActivity{
    private static LinearLayout reportDetailLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        setLogTv(findViewById(R.id.log_textview));

        String a = "1234";
        System.out.print(a);
    }

    @Override
    protected void onResume() {
        super.onResume();
        printTemp();
    }

    public void printTemp() {
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reportView = inflater.inflate(R.layout.view_slip_report_detail, null);
        reportView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        reportView.layout(0, 0, reportView.getMeasuredWidth(), reportView.getMeasuredHeight());

        reportDetailLinearLayout = reportView.findViewById(R.id.reportDetailLinearLayout);

        doPrinting(getBitmapFromView(reportDetailLinearLayout));
    }

    public static Bitmap getBitmapFromView(View view) {
        Log.d("Dom", "get Bitmap From View ");
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        Bitmap reSizeBmp = Bitmap.createScaledBitmap(returnedBitmap, returnedBitmap.getWidth() / 40 * 32, returnedBitmap.getHeight() / 40 * 32, true);
        return reSizeBmp;
    }

    public static void doPrinting(Bitmap slip) {

        new Thread() {
            @Override
            public void run() {
                Printer printer = Printer.getInstance();
                PrintTask printTask = new PrintTask();
                printTask.setGray(130);

                PrintCanvas canvas = new PrintCanvas();
                Paint paint = new Paint();
                canvas.drawBitmap(slip, paint);
                printData(printer, printTask, canvas, new PrinterListener() {
                    @Override
                    public void onPrintFinish() {
                        Log.d("Dom", "Print Finish.: ");
                    }

                    @Override
                    public void onPrintError(int i) {
                        Log.d("Dom", "Print Finish.: ");
                    }

                    @Override
                    public void onPrintOutOfPaper() {
                        Log.d("Dom", "Print Out of Paper");
                    }
                });
            }
        }.start();
    }

    public static void printData(Printer printer, PrintTask printTask, PrintCanvas pCanvas, PrinterListener pl) {

        int ret = printer.getStatus();

        Log.d("Dom", "printer status：" + ret);
        if (ret == Printer.PRINTER_STATUS_PAPER_LACK) {
            Log.d("Dom", "The printer is short of paper, please pack the paper" );
            pl.onPrintOutOfPaper();
        } else if (ret == Printer.PRINTER_OK) {
            Log.d("Dom", "printing" );
            printTask.setPrintCanvas(pCanvas);
            printer.startPrint(printTask, new PrinterCallback() {
                @Override
                public void onResult(int i, PrintTask printTask) {
                    pl.onPrintFinish();
                }
            });
        } else {
            pl.onPrintError(ret);
        }
    }

    public interface PrinterListener {

        public void onPrintFinish();

        public void onPrintOutOfPaper();

        public void onPrintError(int ErrorCode);
    }

    public static void printTempDomRegister(String text ,String QRCodeBase64 ,Context context ) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reportView = inflater.inflate(R.layout.slipregister, null);
        reportView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        reportView.layout(0, 0, reportView.getMeasuredWidth(), reportView.getMeasuredHeight());
        reportDetailLinearLayout = reportView.findViewById(R.id.reportDetailLinearLayout);



        TextView Tx1 = reportDetailLinearLayout.findViewById(R.id.text1);
        Log.d("Dom print", "printTemp: qr "+ QRCodeBase64);
        ImageView imageView = reportDetailLinearLayout.findViewById(R.id.myimgdom);
        if (QRCodeBase64 != null && !QRCodeBase64.isEmpty() && !QRCodeBase64.equals("null")) {
            Log.d("Dom print", "printTemp: Set");
            byte[] decodedBytes_qrcode = Base64.decode(QRCodeBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes_qrcode, 0, decodedBytes_qrcode.length);
            imageView.setImageBitmap(bitmap);

            // กำหนดความสูงเป็น 290dp
            int heightInDp = 290;
            float scale = context.getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (heightInDp * scale);
            imageView.getLayoutParams().height = heightInPixels;
            imageView.requestLayout();

        } else {
            Log.d("Dom print", "printTemp: No Set");
            imageView.setImageBitmap(null);


//            ViewGroup.LayoutParams layoutParams = Tx1.getLayoutParams();
//            layoutParams.height = 200; // ตัวอย่าง: กำหนดความสูงเป็น 100
//            Tx1.setLayoutParams(layoutParams);


//            LinearLayout mainLayout = reportDetailLinearLayout.findViewById(R.id.myimgdom);


//            int heightInDp = 200;
//            float scale = ().getDisplayMetrics().density;
//            int heightInPixels = (int) (heightInDp * scale + 0.5f);

            LinearLayout myLinearLayout = reportDetailLinearLayout.findViewById(R.id.reportDetailLinearLayout);

            int heightInDp = 100;
            float scale = context.getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (heightInDp * scale + 0.5f); // Adding 0.5f for rounding
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, heightInPixels);
            myLinearLayout.setLayoutParams(layoutParams);



            // กำหนดความสูงเป็น 0dp
            imageView.getLayoutParams().height = 0;
            imageView.requestLayout();
            imageView.setVisibility(View.INVISIBLE);

            LinearLayout reportDetailLinearLayout2 = reportDetailLinearLayout.findViewById(R.id.reportDetailLinearLayout); // ระบุ ID ของ LinearLayout ที่บรรจุ ImageView
            ImageView myimgdom = reportDetailLinearLayout.findViewById(R.id.myimgdom); // ระบุ ID ของ ImageView ที่ต้องการลบ
            reportDetailLinearLayout2.removeView(myimgdom);
        }

        Log.d("bearbug", "text: " + text);
        String str1 = text;
        String[] str2 = str1.split("\\|");
        String x1 = "";
        for( int i = 0; i < str2.length; i++ )
        {
            x1 += str2[i]+"\n";
        }
        Tx1.setText(x1);
        Log.d("bearbug", "Print text : " + x1);
        doPrinting(getBitmapFromView(reportDetailLinearLayout));
    }


    public static void printTempDomLakTam(String text ,String QRCodeBase64 ,Context context ) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reportView = inflater.inflate(R.layout.sliplaktam, null);
        reportView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        reportView.layout(0, 0, reportView.getMeasuredWidth(), reportView.getMeasuredHeight());
        reportDetailLinearLayout = reportView.findViewById(R.id.reportDetailLinearLayout);



        TextView Tx1 = reportDetailLinearLayout.findViewById(R.id.text1);
        Log.d("Dom print", "printTemp: qr "+ QRCodeBase64);
        ImageView imageView = reportDetailLinearLayout.findViewById(R.id.myimgdom);
        if (QRCodeBase64 != null && !QRCodeBase64.isEmpty() && !QRCodeBase64.equals("null")) {
            Log.d("Dom print", "printTemp: Set");
            byte[] decodedBytes_qrcode = Base64.decode(QRCodeBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes_qrcode, 0, decodedBytes_qrcode.length);
            imageView.setImageBitmap(bitmap);

            // กำหนดความสูงเป็น 290dp
            int heightInDp = 290;
            float scale = context.getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (heightInDp * scale);
            imageView.getLayoutParams().height = heightInPixels;
            imageView.requestLayout();

        } else {
            Log.d("Dom print", "printTemp: No Set");
            imageView.setImageBitmap(null);


//            ViewGroup.LayoutParams layoutParams = Tx1.getLayoutParams();
//            layoutParams.height = 200; // ตัวอย่าง: กำหนดความสูงเป็น 100
//            Tx1.setLayoutParams(layoutParams);


//            LinearLayout mainLayout = reportDetailLinearLayout.findViewById(R.id.myimgdom);


//            int heightInDp = 200;
//            float scale = ().getDisplayMetrics().density;
//            int heightInPixels = (int) (heightInDp * scale + 0.5f);

            LinearLayout myLinearLayout = reportDetailLinearLayout.findViewById(R.id.reportDetailLinearLayout);

            int heightInDp = 100;
            float scale = context.getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (heightInDp * scale + 0.5f); // Adding 0.5f for rounding
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, heightInPixels);
            myLinearLayout.setLayoutParams(layoutParams);



            // กำหนดความสูงเป็น 0dp
            imageView.getLayoutParams().height = 0;
            imageView.requestLayout();
            imageView.setVisibility(View.INVISIBLE);

            LinearLayout reportDetailLinearLayout2 = reportDetailLinearLayout.findViewById(R.id.reportDetailLinearLayout); // ระบุ ID ของ LinearLayout ที่บรรจุ ImageView
            ImageView myimgdom = reportDetailLinearLayout.findViewById(R.id.myimgdom); // ระบุ ID ของ ImageView ที่ต้องการลบ
            reportDetailLinearLayout2.removeView(myimgdom);
        }

        Log.d("bearbug", "text: " + text);
        String str1 = text;
        String[] str2 = str1.split("\\|");
        String x1 = "";
        for( int i = 0; i < str2.length; i++ )
        {
            x1 += str2[i]+"\n";
        }
        Tx1.setText(x1);
        Log.d("bearbug", "Print text : " + x1);
        doPrinting(getBitmapFromView(reportDetailLinearLayout));
    }


    public static void printTempDomJane(String text ,String QRCodeBase64 ,Context context ) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reportView = inflater.inflate(R.layout.slipjane, null);
        reportView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        reportView.layout(0, 0, reportView.getMeasuredWidth(), reportView.getMeasuredHeight());
        reportDetailLinearLayout = reportView.findViewById(R.id.reportDetailLinearLayout);



        TextView Tx1 = reportDetailLinearLayout.findViewById(R.id.text1);
        Log.d("Dom print", "printTemp: qr "+ QRCodeBase64);
        ImageView imageView = reportDetailLinearLayout.findViewById(R.id.myimgdom);
        if (QRCodeBase64 != null && !QRCodeBase64.isEmpty() && !QRCodeBase64.equals("null")) {
            Log.d("Dom print", "printTemp: Set");
            byte[] decodedBytes_qrcode = Base64.decode(QRCodeBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes_qrcode, 0, decodedBytes_qrcode.length);
            imageView.setImageBitmap(bitmap);

            // กำหนดความสูงเป็น 290dp
            int heightInDp = 290;
            float scale = context.getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (heightInDp * scale);
            imageView.getLayoutParams().height = heightInPixels;
            imageView.requestLayout();

        } else {
            Log.d("Dom print", "printTemp: No Set");
            imageView.setImageBitmap(null);


//            ViewGroup.LayoutParams layoutParams = Tx1.getLayoutParams();
//            layoutParams.height = 200; // ตัวอย่าง: กำหนดความสูงเป็น 100
//            Tx1.setLayoutParams(layoutParams);


//            LinearLayout mainLayout = reportDetailLinearLayout.findViewById(R.id.myimgdom);


//            int heightInDp = 200;
//            float scale = ().getDisplayMetrics().density;
//            int heightInPixels = (int) (heightInDp * scale + 0.5f);

            LinearLayout myLinearLayout = reportDetailLinearLayout.findViewById(R.id.reportDetailLinearLayout);

            int heightInDp = 100;
            float scale = context.getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (heightInDp * scale + 0.5f); // Adding 0.5f for rounding
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, heightInPixels);
            myLinearLayout.setLayoutParams(layoutParams);



            // กำหนดความสูงเป็น 0dp
            imageView.getLayoutParams().height = 0;
            imageView.requestLayout();
            imageView.setVisibility(View.INVISIBLE);

            LinearLayout reportDetailLinearLayout2 = reportDetailLinearLayout.findViewById(R.id.reportDetailLinearLayout); // ระบุ ID ของ LinearLayout ที่บรรจุ ImageView
            ImageView myimgdom = reportDetailLinearLayout.findViewById(R.id.myimgdom); // ระบุ ID ของ ImageView ที่ต้องการลบ
            reportDetailLinearLayout2.removeView(myimgdom);
        }

        Log.d("bearbug", "text: " + text);
        String str1 = text;
        String[] str2 = str1.split("\\|");
        String x1 = "";
        for( int i = 0; i < str2.length; i++ )
        {
            x1 += str2[i]+"\n";
        }
        Tx1.setText(x1);
        Log.d("bearbug", "Print text : " + x1);
        doPrinting(getBitmapFromView(reportDetailLinearLayout));
    }

    public static void printTempDom(String text ,String QRCodeBase64 ,Context context ) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reportView = inflater.inflate(R.layout.slip, null);
        reportView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        reportView.layout(0, 0, reportView.getMeasuredWidth(), reportView.getMeasuredHeight());
        reportDetailLinearLayout = reportView.findViewById(R.id.reportDetailLinearLayout);



        TextView Tx1 = reportDetailLinearLayout.findViewById(R.id.text1);
        Log.d("Dom print", "printTemp: qr "+ QRCodeBase64);
        ImageView imageView = reportDetailLinearLayout.findViewById(R.id.myimgdom);
        if (QRCodeBase64 != null && !QRCodeBase64.isEmpty() && !QRCodeBase64.equals("null")) {
            Log.d("Dom print", "printTemp: Set");
            byte[] decodedBytes_qrcode = Base64.decode(QRCodeBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes_qrcode, 0, decodedBytes_qrcode.length);
            imageView.setImageBitmap(bitmap);
            // กำหนดความสูงเป็น 290dp
            int heightInDp = 290;
            float scale = context.getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (heightInDp * scale);
            imageView.getLayoutParams().height = heightInPixels;
            imageView.requestLayout();

        } else {
            Log.d("Dom print", "printTemp: No Set");
            imageView.setImageBitmap(null);
            LinearLayout myLinearLayout = reportDetailLinearLayout.findViewById(R.id.reportDetailLinearLayout);
            int heightInDp = 100;
            float scale = context.getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (heightInDp * scale + 0.5f); // Adding 0.5f for rounding
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, heightInPixels);
            myLinearLayout.setLayoutParams(layoutParams);

            // กำหนดความสูงเป็น 0dp
            imageView.getLayoutParams().height = 0;
            imageView.requestLayout();
            imageView.setVisibility(View.INVISIBLE);

            LinearLayout reportDetailLinearLayout2 = reportDetailLinearLayout.findViewById(R.id.reportDetailLinearLayout); // ระบุ ID ของ LinearLayout ที่บรรจุ ImageView
            ImageView myimgdom = reportDetailLinearLayout.findViewById(R.id.myimgdom); // ระบุ ID ของ ImageView ที่ต้องการลบ
            reportDetailLinearLayout2.removeView(myimgdom);
        }

        Log.d("bearbug", "text: " + text);
        String str1 = text;
        String[] str2 = str1.split("\\|");
        String x1 = "";
        for( int i = 0; i < str2.length; i++ )
        {
            x1 += str2[i]+"\n";
        }
//        Tx1.setText(x1);
        Log.d("bearbug", "Print text : " + x1);
        doPrinting(getBitmapFromView(reportDetailLinearLayout));
    }
}
