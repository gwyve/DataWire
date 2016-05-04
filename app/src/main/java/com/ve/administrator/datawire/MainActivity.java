package com.ve.administrator.datawire;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gwyve.MiniServer;
import com.gwyve.Util;

import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button startBtn ;
    private Button stopBtn;
    private ProgressBar progressBar;

    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textView = (TextView) findViewById(R.id.TextView);
        startBtn = (Button)findViewById(R.id.startBtn);
        stopBtn = (Button) findViewById(R.id.stopBtn);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取wifi服务
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                //判断wifi是否开启
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                String ip = intToIp(ipAddress);

                port = 8080;
                try {
                    while (Util.isPortUsing("127.0.0.1",port)) {
                        port++;
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                textView.setText("please input "+ip+":"+port);
//                Log.e("111",""+Environment.getRootDirectory());
                MiniServer miniServer = new MiniServer(Environment.getExternalStorageDirectory(),port);
                Thread thread = new Thread(miniServer);
                thread.setDaemon(true);
                thread.start();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
}
