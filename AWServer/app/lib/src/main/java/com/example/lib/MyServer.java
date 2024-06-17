package com.example.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer
{
    private Socket client1 = null;
    private Socket client2 = null;

    public static void main(String[] args){
        new MyServer();
    }

    public MyServer()
    {
        try
        {
            // 获取本机IP地址
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("local host: " + address);

            // 创建Server Socket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("--Listener Port: 9999--");

            // 等待客户端连接
            while (true)
            {
                System.out.println("waiting for client1 connect");
                // 等待接收请求，这里接收客户端1的请求
                client1 = serverSocket.accept();
                // 客户端1连接成功 开启子线程线程处理和客户端的消息传输
                System.out.println("accept client1 connect" + client1);

                new WaitThread().start();

                System.out.println("waiting for client2 connect");
                // 等待接受请求，这里接收客户端2的请求
                client2 = serverSocket.accept();
                // 客户端2连接成功 开启子线程线程处理和客户端的消息传输
                System.out.println("accept client2 connect" + client2);

                new ServerSocketThread(client1, client2).start();
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    // 当只有一台设备接入服务器时 启动等待线程 等待第二台设备的接入
    public class WaitThread extends Thread
    {
        private BufferedReader in;
        private String content;         // 来自客户端的信息

        @Override
        public void run()
        {
            try
            {
                in = new BufferedReader(new InputStreamReader(client1.getInputStream(), "UTF-8"));

                // 等待客户端2接入
                while ((content = in.readLine()) != null && client2 == null)
                {
                    // 从socket连接读取到bye标识客户端发出断开连接请求
                    if(content.equals("bye")){
                        System.out.println("disconnect from client1, close client1");
                        //关闭socket输入输出流
                        client1.shutdownInput();
                        client1.shutdownOutput();
                        client1.close();
                        return;
                    }
                    else
                    {
                        //从socket连接读取到的不是断开连接请求，则向客户端发信息
                        sendPauseMessage(client1);
                    }
                }

            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public void sendPauseMessage(Socket client)
        {
            PrintWriter pout = null;
            try
            {
                String message = "pause";
                System.out.println("message to client:" + message);

                // OutputStreamWriter：将字符流转换为字节流
                // BufferedWriter：是缓冲字符输出流
                // PrintWriter：字符类型的打印输出流
                pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"utf-8")),true);
                // 利用输出流输出数据
                pout.println(message);

            }catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}