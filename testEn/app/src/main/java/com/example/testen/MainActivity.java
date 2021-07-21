package com.example.testen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private TextView textView,textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView4);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        checkPermission();
//        ScanWifiInfo();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{Thread.sleep(500);}catch(InterruptedException e){e.printStackTrace();}
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkPermission();
                                ScanWifiInfo();
                            }
                        });
                    }
                });
                t.start();
//                Intent intent=new Intent();
//                intent.setClass(MainActivity.this,RegisterActivity.class);
//                startActivity(intent);
//                System.err.println("跳转成果");

            }
        });
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                try {
//                    textView.append(readParse("http://192.168.129.16:8080/register/darliroon/Hello23"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    textView.append("cannot connect");
//                }
//            }
//        }).start();
    }

    private void ScanWifiInfo(){
        WifiManager wifiManager= (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.startScan();
//        if(! wifiManager.isWifiEnabled()){
//            if(wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING){
//                Toast.makeText(this, "请打开WIFI", Toast.LENGTH_SHORT).show();
//            }
//        }
        StringBuilder scanBuilder= new StringBuilder();
        List<ScanResult> scanResults=wifiManager.getScanResults();//搜索到的设备列表
        scanResults.sort(new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult poet1, ScanResult poet2) {
                Integer score1 = poet1.level;
                Integer score2 = poet2.level;
                return score2.compareTo(score1);
            }
        });
        System.err.println("hasWifi "+scanResults.size());
        for (ScanResult scanResult : scanResults) {
            scanBuilder.append("\n设备名："+scanResult.SSID
                    +"\n设备位置："+scanResult.BSSID
                    +"\n信号强度："+scanResult.level+"\n");
        }
        textView.setText(scanBuilder);
        textView1.setText("hasWifi "+scanResults.size());
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET},
                        REQUEST_CODE
                );
                Toast.makeText(this, "正在授权！", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("getPermission", "checkPermission: 已经授权！");
            }
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    public static String readParse(String urlPath) throws Exception {
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        byte[] data = new byte[1024];
//        int len = 0;
//        URL url = new URL(urlPath);
//        System.err.println("connect ready");
//        URLConnection urlCon = url.openConnection();
//        urlCon.setDoOutput(false);
//        urlCon.setDoInput(true);
//        urlCon.setConnectTimeout(40000);
//        urlCon.setReadTimeout(40000);
//        urlCon.setUseCaches(false);
//        System.err.println("connect success");
//        InputStream inStream = urlCon.getInputStream();
//        while ((len = inStream.read(data)) != -1) {
//            outStream.write(data, 0, len);
//        }
//        System.err.println("read success");
//        inStream.close();
//        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
//    }
}

