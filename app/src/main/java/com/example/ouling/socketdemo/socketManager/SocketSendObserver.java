package com.example.ouling.socketdemo.socketManager;

import java.util.Observable;
import java.util.Observer;

public abstract class SocketSendObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof SocketSendObservable.SocketSendBean) {
            SocketSendObservable.SocketSendBean bean = (SocketSendObservable.SocketSendBean) arg;
            if (bean.getType() == SocketSendObservable.SEND_SUCCESS) {
                onSendSuccess(bean.getMsg());
            } else {
                onSendError(bean.getMsg());
            }
        }
    }

    protected abstract void onSendSuccess(String msg);

    protected abstract void onSendError(String msg);

}
