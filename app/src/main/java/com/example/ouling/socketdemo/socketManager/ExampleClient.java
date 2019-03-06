package com.example.ouling.socketdemo.socketManager;

import android.os.Handler;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * 连接包装
 */
public class ExampleClient extends WebSocketClient {
    //连接被观察者
    private SocketClientObservable cliObservable;
    //信息被观察者
    private SocketMessageObservable msgObservable;
    //发送被观察者
    private SocketSendObservable sendObservable;
    private Handler handler;
    //心跳包的时间
    private int HEART_BEAT_DELAY = 30 * 1000;
    //心跳包的内容
    private String heartBeatStr = "KEEPALIVE";
    //心跳包的任务
    private HeartBeatTask task;
    //信息结束标识
    private String endTag = "<EOF>";

    public ExampleClient setHeatBeatLoopTime(int timeMile) {
        HEART_BEAT_DELAY = timeMile;
        return this;
    }

    public ExampleClient setHeatBeatMsg(String msg) {
        heartBeatStr = msg;
        return this;
    }

    public ExampleClient setMessageEndTag(String endTag) {
        this.endTag = endTag;
        return this;
    }

    public ExampleClient(URI serverUri) {
        super(serverUri);
        handler = new Handler();
        task = new HeartBeatTask();
        cliObservable = new SocketClientObservable();
        msgObservable = new SocketMessageObservable();
        sendObservable = new SocketSendObservable();
        setConnectionLostTimeout(-1);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (cliObservable == null) {
            return;
        }
        cliObservable.onOpen(handshakedata.getHttpStatusMessage());
//        task.startTask();
    }

    /**
     * 开启心跳发送
     */
    public void startHeartBeat() {
        if (task == null) {
            task = new HeartBeatTask();
        }
        task.startTask();
    }

    /**
     * 关闭心跳发送,退出的时候一定要调,否则内存泄漏
     */
    public void stopHeartBeat() {
        if (task != null) {
            task.stopTask();
        }
    }

    @Override
    public void onMessage(String message) {
        if (msgObservable == null) {
            return;
        }
        if (message.endsWith(endTag)) {
            msgObservable.onMessage(message.replaceAll(endTag, ""));
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (cliObservable == null) {
            return;
        }
        cliObservable.onClose(code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        if (cliObservable == null) {
            return;
        }
        cliObservable.onError(ex);
    }

    /**
     * 添加连接观察者
     *
     * @param observer
     */
    public void addCliObserver(SocketClientObserver observer) {
        if (cliObservable == null) {
            cliObservable = new SocketClientObservable();
        }
        cliObservable.addObserver(observer);
    }

    /**
     * 删除连接观察者
     *
     * @param observer
     */
    public void removeCliObserver(SocketClientObserver observer) {
        if (cliObservable != null) {
            cliObservable.deleteObserver(observer);
        }
    }

    /**
     * 添加信息观察者
     *
     * @param observer
     */
    public void addMsgObserver(SocketMsgObserver observer) {
        if (msgObservable == null) {
            msgObservable = new SocketMessageObservable();
        }
        msgObservable.addObserver(observer);
    }

    /**
     * 移除全部监听
     */
    public void removeObservables() {
        if (cliObservable != null) {
            cliObservable.deleteObservers();
        }
        if (msgObservable != null) {
            msgObservable.deleteObservers();
        }
        if (sendObservable != null) {
            sendObservable.deleteObservers();
        }
    }

    /**
     * 删除信息观察者
     *
     * @param observer
     */
    public void removeMsgObserver(SocketMsgObserver observer) {
        if (msgObservable != null) {
            msgObservable.deleteObserver(observer);
        }
    }

    /**
     * 添加发送观察者
     *
     * @param observer
     */
    public void addSendObserver(SocketSendObserver observer) {
        if (sendObservable == null) {
            sendObservable = new SocketSendObservable();
        }
        sendObservable.addObserver(observer);
    }

    /**
     * 删除发送观察者
     *
     * @param observer
     */
    public void removeCliObserver(SocketSendObserver observer) {
        if (sendObservable != null) {
            sendObservable.deleteObserver(observer);
        }
    }


    /**
     * todo 自动添加<EOF>
     *
     * @param data
     */
    @Override
    @Deprecated
    public void send(byte[] data) {
        super.send(data);
    }

    @Override
    public void send(String text) {
        if (sendObservable == null) {
            return;
        }
        String msg = text + endTag;
        try {
            super.send(msg);
        } catch (Exception e) {
            sendObservable.onMessage(new SocketSendObservable.SocketSendBean().setMsg(text).setType(SocketSendObservable.SEND_ERROR));
        }
        sendObservable.onMessage(new SocketSendObservable.SocketSendBean().setMsg(text).setType(SocketSendObservable.SEND_SUCCESS));
    }


    private class HeartBeatTask implements Runnable {

        @Override
        public void run() {
            ExampleClient.this.send(heartBeatStr);
            if (handler == null) {
                handler = new Handler();
            }
            handler.postDelayed(this, HEART_BEAT_DELAY);
        }

        public void startTask() {
            if (handler == null) {
                handler = new Handler();
            }
            stopTask();
            handler.postDelayed(this, HEART_BEAT_DELAY);
        }

        public void stopTask() {
            if (handler != null) {
                handler.removeCallbacks(this);
            }
        }
    }

    @Override
    public void reconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ExampleClient.super.reconnect();
            }
        }).start();
    }


}
