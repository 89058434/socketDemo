package com.example.ouling.socketdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ouling.socketdemo.socketManager.ExampleClient;
import com.example.ouling.socketdemo.socketManager.SocketClientObserver;
import com.example.ouling.socketdemo.socketManager.SocketMsgObserver;
import com.example.ouling.socketdemo.socketManager.SocketSendObserver;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    private ExampleClient c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            c = new ExampleClient(new URI("ws://121.196.207.174:8001"));
            c.setHeatBeatLoopTime(30 * 1000).setHeatBeatMsg("KEEPALIVE").setSendEndTag("<EOF>").connect();
            c.addCliObserver(new SocketClientObserver() {
                @Override
                protected void onOpen(String msg) {
                    System.out.println("=======================open" + msg);
                    c.send("{\"Users_Id\":\"6176C6A13B132E24E74E621B7D3830D3\",\"Source\":\"Android 7.1 .2Xiaomi Redmi Note 5 A \"}");
                    c.send("{\"msgtype\":\"12\",\"roomid\":\"" + 30 + "\",\"state\":\"" + 1 + "\"}");
                    c.startHeartBeat();
                }

                @Override
                protected void onError(String msg) {
                    System.out.println("==========================error" + msg);
                    c.stopHeartBeat();
                    c.reconnect();
                }

                @Override
                protected void onClose(String msg) {
                    System.out.println("===========================close" + msg);
                    c.stopHeartBeat();
                    c.reconnect();
                }
            });
            c.addMsgObserver(new SocketMsgObserver() {
                @Override
                protected void onMsg(String msg) {
                    System.out.println(msg + "==============================");
                }
            });

            c.addSendObserver(new SocketSendObserver() {
                @Override
                protected void onSendSuccess(String msg) {
                    System.out.println(msg + "===============sendSuccess");
                }

                @Override
                protected void onSendError(String msg) {
                    System.out.println(msg + "===============sendError");
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        c.stopHeartBeat();
        c.removeObservables();
        c.close();
    }
}
