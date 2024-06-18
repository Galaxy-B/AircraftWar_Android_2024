package com.example.aircraftwar2024.MySocket;

import android.app.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ApplicationUtil extends Application {
    public static final String ADDRESS = "10.0.2.2";
    public static final int PORT = 9999;
    private InputStreamReader in;
    private OutputStreamWriter out;

    private static Socket socket;

//    public void init() throws IOException, Exception {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if(socket == null) {
//                    //与服务器建立连接
//                    try{
//                        //创建socket对象
//                        socket = new Socket();
//                        //connect,要保证服务器已启动
//                        socket.connect(new InetSocketAddress
//                                (ADDRESS,PORT),5000);
//                        in = new InputStreamReader(socket.getInputStream(),"utf-8");
//                        out = new OutputStreamWriter(
//                                socket.getOutputStream(),"utf-8");
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }

    public void init(){
        if(socket == null) {
            //与服务器建立连接
            try{
                //创建socket对象
                socket = new Socket();
                //connect,要保证服务器已启动
                socket.connect(new InetSocketAddress
                        (ADDRESS,PORT),5000);
                in = new InputStreamReader(socket.getInputStream(),"utf-8");
                out = new OutputStreamWriter(
                        socket.getOutputStream(),"utf-8");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Socket getSocket(){
        return socket;
    }

    public void SetSocket(Socket socket){
        this.socket = socket;
    }
    public InputStreamReader getIN(){ return in; }
    public OutputStreamWriter getOut(){ return out; }
    public void setIn(InputStreamReader in){ this.in = in; }
    public void setOut(OutputStreamWriter out){ this.out = out; }

}
