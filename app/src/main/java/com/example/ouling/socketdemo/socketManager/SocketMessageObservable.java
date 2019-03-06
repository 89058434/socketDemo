package com.example.ouling.socketdemo.socketManager;

import java.util.Observable;

class SocketMessageObservable extends Observable {

    public void onMessage(String openMessage) {
        setChanged();
        notifyObservers(openMessage);
    }
}
