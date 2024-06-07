package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
    private Handler handler;
    private EditText txt;
    private static  final String TAG = "MainActivity";
    public static final String ADDRESS = "10.0.2.2";
    public static final int PORT = 9999;
    ApplicationUtil appUtil = (ApplicationUtil) this.getApplication();
    private AlertDialog alertDialog;
    private String fromserver = null;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button jump_botton = (Button) findViewById(R.id.jump_to_menu);
        jump_botton.setOnClickListener(this);

        Button online_botton = (Button) findViewById(R.id.online);
        jump_botton.setOnClickListener(this);

        RadioButton music_on = (RadioButton) findViewById(R.id.radio_on);
        music_on.setOnClickListener(this);

        RadioButton music_off = (RadioButton) findViewById(R.id.radio_off);
        music_off.setOnClickListener(this);

        intent_offline = new Intent(MainActivity.this, OfflineActivity.class);
        intent_online = new Intent(MainActivity.this, GameActivity.class);

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
            GetOnline getOnline = new GetOnline(handler);
            getOnline.start();
            startActivity(intent_online);
        }
        else if(v.getId() == R.id.radio_on){
                is_music_on = true;
        }
        else if(v.getId() == R.id.radio_off){
                is_music_on = false;
        }
    }
    public void showLoadingDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("匹配中，请等待...");
        builder.setCancelable(false); // 设置为不可取消
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void dismissLoadingDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    protected class GetOnline extends Thread{
        private BufferedReader in;
        private Handler toClientHandler;


        public GetOnline(Handler myHandler){
            this.toClientHandler = myHandler;
        }
        @Override
        public void run(){
            try{
                //创建socket对象
                appUtil.init();
                socket = appUtil.getSocket();
                //connect,要保证服务器已启动
                socket.connect(new InetSocketAddress
                        (ADDRESS,PORT),5000);

                //获取socket输入输出流
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"utf-8")),true);
                Log.i(TAG,"connect to server");

                //接收服务器返回的数据
                Thread receiveServerMsg =  new Thread(){
                    @Override
                    public void run(){
                        try{
                            while((fromserver = in.readLine())!=null)
                            {
                                //发送消息给UI线程
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = fromserver;
                                toClientHandler.sendMessage(msg);
                            }
                        }catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }
                };
                receiveServerMsg.start();
                while(Objects.equals((String) fromserver, "pause")){
                    showLoadingDialog(context);
                }
                dismissLoadingDialog();
            }catch(IOException ex){
                ex.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}