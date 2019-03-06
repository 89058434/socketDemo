package com.example.ouling.socketdemo.socketManager;

import java.util.Observable;
import java.util.Observer;

public abstract class SocketMsgObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        onMsg((String) arg);
    }

    protected abstract void onMsg(String msg);

}
