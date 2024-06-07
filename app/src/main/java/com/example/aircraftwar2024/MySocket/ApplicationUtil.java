package com.example.aircraftwar2024.MySocket;

import android.app.Application;

import java.io.IOException;
import java.net.Socket;

public class ApplicationUtil extends Application {
    public static final String ADDRESS = "10.0.2.2";
    public static final int PORT = 9999;

    private Socket socket;

    public void init() throws IOException, Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(socket == null) {
                    //与服务器建立连接
                    try{
                        socket = new Socket(ADDRESS, PORT);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public Socket getSocket(){
        return socket;
    }

    public void SetSocket(Socket socket){
        this.socket = socket;
    }
}
