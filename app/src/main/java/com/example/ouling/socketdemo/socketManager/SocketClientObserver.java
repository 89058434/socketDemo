package com.example.ouling.socketdemo.socketManager;

import java.util.Observable;
import java.util.Observer;

public abstract class SocketClientObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof SocketClientObservable.SocketClientBean) {
            SocketClientObservable.SocketClientBean bean = (SocketClientObservable.SocketClientBean) arg;
            switch (bean.getType()) {
                case SocketClientObservable.TYPE_OPEN:
                    onOpen(bean.getMsg());
                    break;
                case SocketClientObservable.TYPE_CLOSE:
                    onClose(bean.getMsg());
                    break;
                case SocketClientObservable.TYPE_ERROR:
                    onError(bean.getMsg());
                    break;
                default:
                    break;
            }
        }
    }

    protected abstract void onOpen(String msg);

    protected abstract void onError(String msg);

    protected abstract void onClose(String msg);
}
