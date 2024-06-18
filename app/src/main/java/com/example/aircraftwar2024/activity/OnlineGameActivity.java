package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.example.aircraftwar2024.MySocket.ApplicationUtil;
import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.activityManager.ActivityManager;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.EasyGame;
import com.example.aircraftwar2024.game.HardGame;
import com.example.aircraftwar2024.game.MediumGame;
import com.example.aircraftwar2024.game.OnlineGame;

import java.io.IOException;
import java.net.Socket;

public class OnlineGameActivity extends AppCompatActivity {
    private static final String TAG = "OnlineGameActivity";
    public static int screenWidth,screenHeight;
    private BaseGame baseGameView = null;
    public TextView myTextView;
    private Intent intent;
    private boolean is_music_on = false;
    private MyHandler myHandler;
    private Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getScreenHW();

        intent = new Intent(OnlineGameActivity.this, RecordActivity.class);

        ActivityManager.getActivityManager().addActivity(this);

        if(getIntent() != null){
            is_music_on = getIntent().getBooleanExtra("music", false);
        }

        myHandler = new MyHandler(this);
        socket = ApplicationUtil.getSocket();
        baseGameView = new OnlineGame(this, is_music_on, myHandler, socket);
        setContentView(baseGameView);
    }

    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getDisplay().getRealMetrics(dm);

        //窗口的宽度
        screenWidth= dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + screenWidth + " screenHeight : " + screenHeight);
    }
    public class MyHandler extends Handler
    {
        private OnlineGameActivity activity;
        public MyHandler(OnlineGameActivity activity)
        {
            super(Looper.getMainLooper());
            this.activity = activity;
        }
        // 通过复写handlerMessage() 从而确定更新UI的操作
        @Override
        public void handleMessage(Message msg)
        {
            // 根据不同线程发送过来的消息，执行不同的UI操作
            // 根据 Message对象的what属性 标识不同的消息
            if(msg.what == 1)
            {
                String result = (String)msg.obj;
                String[] result_n = result.split("#",3);
                intent.putExtra("mode", result_n[0]);
                intent.putExtra("my_score", result_n[1]);
                intent.putExtra("op_score",result_n[2]);
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                activity.startActivity(intent);
            }
        }
    }
}