package com.example.ouling.socketdemo.socketManager;

import java.util.Observable;

class SocketClientObservable extends Observable {
    public static final int TYPE_OPEN = 1;
    public static final int TYPE_CLOSE = 0;
    public static final int TYPE_ERROR = -1;

    public void onOpen(String openMessage) {
        setChanged();
        notifyObservers(new SocketClientBean().setMsg(openMessage).setType(TYPE_OPEN));
    }


    public void onClose(int code, String reason, boolean remote) {
        setChanged();
        notifyObservers(new SocketClientBean().setMsg(reason).setType(TYPE_CLOSE));
    }

    public void onError(Exception ex) {
        setChanged();
        notifyObservers(new SocketClientBean().setMsg(ex.getMessage()).setType(TYPE_ERROR));
    }


    class SocketClientBean {
        private int type;
        private String msg;

        public int getType() {
            return type;
        }

        public SocketClientBean setType(int type) {
            this.type = type;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public SocketClientBean setMsg(String msg) {
            this.msg = msg;
            return this;
        }
    }

}
