package com.example.lib;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ServerSocketThread extends Thread{
    private BufferedReader in1;
    private BufferedReader in2;
    private Socket client1;     // 客户端1
    private Socket client2;     // 客户端2
    private String content1;    // 来自客户端1的信息
    private String content2;    // 来自客户端2的信息

    public ServerSocketThread(Socket client1, Socket client2)
    {
        this.client1 = client1;
        this.client2 = client2;
    }

    @Override
    public void run()
    {
        try
        {
            in1 = new BufferedReader(new InputStreamReader(client1.getInputStream(),"UTF-8"));
            in2 = new BufferedReader(new InputStreamReader(client1.getInputStream(),"UTF-8"));

            // 两个客户端均接入后 启动游戏
            this.sendFlagMessage(client1, client2, "start");

            boolean gameover1 = false;
            boolean gameover2 = false;

            // 执行游戏逻辑
            while ((content1 = in1.readLine()) != null && (content2 = in2.readLine()) != null)
            {
                // 从socket连接读取到over标识客户端发出断开连接请求
                if(gameover1 && gameover2)
                {
                    System.out.println("disconnect from both client, close both client");
                    this.sendFlagMessage(client1, client2, "shutdown");
                    //关闭socket输入输出流
                    client1.shutdownInput();
                    client2.shutdownOutput();
                    client1.shutdownOutput();
                    client2.shutdownOutput();
                    client1.close();
                    client2.close();
                    return;
                }
                // 某架飞机被击落
                if (content1.equals("end"))
                {
                    gameover1 = true;
                }
                if (content2.equals("end"))
                {
                    gameover2 = true;
                }
                // 从socket连接读取到的不是断开连接请求，也没有敌机死亡，则向客户端发送分数
                this.sendScoreMessage(client1, client2, content1, content2);
            }

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void sendFlagMessage(Socket client1, Socket client2, String message)
    {
        PrintWriter pout1 = null;
        PrintWriter pout2 = null;
        try
        {
            System.out.println("message to client:" + message);

            // OutputStreamWriter：将字符流转换为字节流
            // BufferedWriter：是缓冲字符输出流
            // PrintWriter：字符类型的打印输出流
            pout1 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client1.getOutputStream(),"utf-8")),true);
            pout2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client2.getOutputStream(), "utf-8")), true);
            // 利用输出流输出数据
            pout1.println(message);
            pout2.println(message);

        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void sendScoreMessage(Socket client1, Socket client2, String score1, String score2)
    {
        PrintWriter pout1 = null;
        PrintWriter pout2 = null;
        try
        {
            System.out.println("message to client1:" + score2);
            System.out.println("message to client2:" + score1);

            // OutputStreamWriter：将字符流转换为字节流
            // BufferedWriter：是缓冲字符输出流
            // PrintWriter：字符类型的打印输出流
            pout1 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client1.getOutputStream(),"utf-8")),true);
            pout2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client2.getOutputStream(), "utf-8")), true);
            // 利用输出流输出数据
            pout1.println(score2);
            pout2.println(score1);

        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
