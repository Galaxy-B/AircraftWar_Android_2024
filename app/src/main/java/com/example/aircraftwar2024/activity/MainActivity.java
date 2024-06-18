package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.aircraftwar2024.MySocket.ApplicationUtil;
import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.activityManager.ActivityManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean is_music_on = false;
    Intent intent_offline, intent_online;
    private Socket socket;
    private PrintWriter writer;
    private InputStreamReader in;
    private OutputStreamWriter out;
    private Handler handler;
    private static  final String TAG = "MainActivity";
    private AlertDialog alertDialog;
    private String fromserver = null;
    private Context context = this;
    private ApplicationUtil appUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appUtil = (ApplicationUtil) MainActivity.this.getApplication();
        try {
            //创建socket对象
//            appUtil.init();
//            socket = appUtil.getSocket();
            in = appUtil.getIN();
            out = appUtil.getOut();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button jump_botton = (Button) findViewById(R.id.jump_to_menu);
        jump_botton.setOnClickListener(this);

        Button online_botton = (Button) findViewById(R.id.online);
        online_botton.setOnClickListener(this);

        RadioButton music_on = (RadioButton) findViewById(R.id.radio_on);
        music_on.setOnClickListener(this);

        RadioButton music_off = (RadioButton) findViewById(R.id.radio_off);
        music_off.setOnClickListener(this);

        intent_offline = new Intent(MainActivity.this, OfflineActivity.class);
        intent_online = new Intent(MainActivity.this, OnlineGameActivity.class);

        ActivityManager.getActivityManager().addActivity(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.jump_to_menu) {
            intent_offline.putExtra("music", is_music_on);
            startActivity(intent_offline);
        }
        else if(v.getId() == R.id.online) {
            intent_online.putExtra("music", is_music_on);
            intent_online.putExtra("gameType", 2);
            showLoadingDialog();
            GetOnline getOnline = new GetOnline(alertDialog);
            getOnline.start();
        }
        else if(v.getId() == R.id.radio_on){
                is_music_on = true;
        }
        else if(v.getId() == R.id.radio_off){
                is_music_on = false;
        }
    }
    public void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle("系统提示")
                .setMessage("匹配中，请等待...")
                .setCancelable(false);// 设置为不可取消
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void dismissLoadingDialog(AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected class GetOnline extends Thread{
        private AlertDialog dialog;
        private BufferedReader bf;

        public GetOnline(AlertDialog dialog){
            this.dialog = dialog;
        }
        @Override
        public void run(){
            try{
                //ApplicationUtil appUtil = (ApplicationUtil) MainActivity.this.getApplication();
                try {
                    //创建socket对象
                    appUtil.init();
                    socket = appUtil.getSocket();
                    in = appUtil.getIN();
                    out = appUtil.getOut();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                //获取socket输入输出流
                bf = new BufferedReader(in);
                writer = new PrintWriter(new BufferedWriter(
                        out),true);
                Log.i(TAG,"connect to server");

                //接收服务器返回的数据
                fromserver = bf.readLine();
                if(fromserver.equals("start")){
                    dismissLoadingDialog(dialog);
                    startActivity(intent_online);
                }
            }catch(IOException ex){
                ex.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}