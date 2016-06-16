package com.lsm.barrister.app;

public class MsgHelper {


    private static MsgHelper instance = null;

    private MsgHelper() {
        super();
    }

    public static MsgHelper getInstance() {
        if (instance == null) {
            instance = new MsgHelper();
        }
        return instance;
    }

    MsgListener msgListener;

    public MsgListener getMsgListener() {
        return msgListener;
    }

    public void setMsgListener(MsgListener msgListener) {
        this.msgListener = msgListener;
    }

    public interface MsgListener {
        void onReceiveMessage();
    }

}
