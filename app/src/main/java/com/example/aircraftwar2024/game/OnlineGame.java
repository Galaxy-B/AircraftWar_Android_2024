package com.example.aircraftwar2024.game;

import android.content.Context;

import com.example.aircraftwar2024.MySocket.ApplicationUtil;
import com.example.aircraftwar2024.activity.GameActivity;
import com.example.aircraftwar2024.activity.OnlineGameActivity;
import com.example.aircraftwar2024.basic.aircraft.AbstractAircraft;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;


public class OnlineGame extends MediumGame{
    private int my_score, op_score;
    Canvas canvas;
    AbstractAircraft heroAircraft;
    private BufferedReader in;
    private PrintWriter writer;
    private Socket socket;
    private Boolean gameOverFlag = false;
    public OnlineGame(Context context, boolean music_state, Handler handler, Socket socket) {
        super(context, music_state, handler);
        this.socket = socket;
    }

    @Override
    public void paintScoreAndLife() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        // 设置文本大小
        paint.setTextSize(60);
        //加粗
        paint.setFakeBoldText(true);

        // 绘制分数和生命值文本
        my_score = getScore();
        String my_scoreText = "SCORE: " + my_score; // 假设score为分数变量
        String op_scoreText = "Opponent SCORE" + op_score;
        String lifeText = "LIFE: " + heroAircraft.getHp(); // 假设life为生命值变量

        canvas.drawText(my_scoreText, 10, 50, paint); // 分数文本框位置
        canvas.drawText(op_scoreText, 10, 130, paint);//对手分数文本框位置
        canvas.drawText(lifeText, 10, 210, paint); // 生命值文本框位置
    }

    @Override
    public void run(){
        try{
            //获取socket输入输出流
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            writer = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream(),"utf-8")),true);
            Log.i("OnlineGame","connect to server");

            //不断传送分数
            new Thread(()->{
                while (!gameOverFlag) {
                    try {
                        writer.println("" + getScore());
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                writer.println("end");
            }).start();

            //接收服务器返回的数据
            new Thread(()->{
                try {
                    String fromserver = null;
                    while((fromserver = in.readLine()) != null){
                        if (fromserver.equals("end")){
                            Message message = Message.obtain();
                            message.what = 1;
                            message.obj = gameType + my_score + "#" + op_score;
                            myHandler.sendMessage(message);
                            mbLoop = false;
                            break;
                        }else {
                            op_score = Integer.parseInt(fromserver);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void GameOver(){
        if (heroAircraft.notValid()) {
            gameOverFlag = true;
            mbLoop = false;
            // 播放游戏失败音效
            mySoundPool.playMusic(4);
            Log.i("OnlineGame", "heroAircraft is not Valid");
        }
    }
}
