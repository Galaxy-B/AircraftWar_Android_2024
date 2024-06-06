package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.EasyGame;
import com.example.aircraftwar2024.game.HardGame;
import com.example.aircraftwar2024.game.MediumGame;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    private int gameType=0;
    public static int screenWidth,screenHeight;
    private BaseGame baseGameView = null;
    public TextView myTextView;
    private Intent intent;
    private boolean is_music_on = false;
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getScreenHW();

        intent = new Intent(GameActivity.this, RecordActivity.class);
        //在主线程中创建handler实例
        myHandler = new MyHandler(this);

        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
            is_music_on = getIntent().getBooleanExtra("music", false);
        }
        if(gameType == 1){
            baseGameView = new EasyGame(this, is_music_on, myHandler);
            setContentView(baseGameView);
        }
        else if(gameType == 2){
            baseGameView = new MediumGame(this, is_music_on, myHandler);
            setContentView(baseGameView);
        }
        else {
            baseGameView = new HardGame(this, is_music_on, myHandler);
            setContentView(baseGameView);
        }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class MyHandler extends Handler
    {
        private GameActivity activity;
        public MyHandler(GameActivity activity)
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
                intent.putExtra("mode", result.substring(0,1));
                intent.putExtra("score", result.substring(1));
                activity.startActivity(intent);
            }
        }
    }
}