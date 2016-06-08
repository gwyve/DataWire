package com.ve.administrator.datawire;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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


public class MainActivity extends AppCompatActivity {


    private MiniServer miniServer = null;
    private Thread miniServerThread = null;
    private TextView textView;
    private Button startBtn ;
    private Button stopBtn;
    private ProgressBar progressBar;

    public Handler textViewHandler = new Handler(){
        public void handleMessage(Message msg){
            textView.setText(msg.getData().getString("textView"));
        }
    };


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


        stopBtn.setClickable(false);
        startBtn.setClickable(true);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取wifi服务
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                //判断wifi是否开启
                if (!wifiManager.isWifiEnabled()) {
                    textView.setText("Please open WIFI!!!");
                    return;
                }
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                String ip = intToIp(ipAddress);

                if (miniServerThread==null){
                    miniServer = MiniServer.singletonMiniServer(Environment.getExternalStorageDirectory(), ip, textViewHandler);
                    miniServerThread = new Thread(miniServer);
                    miniServerThread.setDaemon(true);
                    miniServerThread.start();
                }else{
                    if(!miniServerThread.isAlive()){
                        miniServerThread.start();
                    }
                    textView.setText("Please put \""+miniServer.ip+":"+miniServer.port+"\" as path address in the browser!");
                }
                stopBtn.setClickable(true);
                startBtn.setClickable(false);
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniServerThread.stop();
                textView.setText("Stopped!!");
                stopBtn.setClickable(false);
                startBtn.setClickable(true);
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
