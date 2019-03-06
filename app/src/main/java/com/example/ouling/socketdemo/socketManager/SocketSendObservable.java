package com.example.ouling.socketdemo.socketManager;

import java.util.Observable;

class SocketSendObservable extends Observable {
    public static final int SEND_SUCCESS = 1;
    public static final int SEND_ERROR = 0;

    public void onMessage(SocketSendBean msgBean) {
        setChanged();
        notifyObservers(msgBean);
    }


    static class SocketSendBean {
        private int type;
        private String msg;

        public int getType() {
            return type;
        }

        public SocketSendBean setType(int type) {
            this.type = type;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public SocketSendBean setMsg(String msg) {
            this.msg = msg;
            return this;
        }
    }
}
