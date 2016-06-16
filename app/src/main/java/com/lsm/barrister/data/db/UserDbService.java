package com.lsm.barrister.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UserDbService {

    private static final String TAG = UserDbService.class.getSimpleName();

    private static UserDbService instance;

    private static SQLiteDatabase db;
    private static DaoMaster daoMaster;
    private DaoSession daoSession;

    private PushMessageDao pushMessageDao;
    Action<PushMessage> pushMessageAction;


    public Action<PushMessage> getPushMessageAction() {
        return pushMessageAction;
    }


    Context context;

    private UserDbService(Context context) {
        this.context = context;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "user.db", null);
        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        pushMessageDao = daoSession.getPushMessageDao();
        pushMessageAction = new Action<PushMessage>(pushMessageDao);

    }

    public static UserDbService getInstance(Context context) {
        if (instance == null) {
            instance = new UserDbService(context);
        }
        return instance;
    }

    public void release() {
        try {
            if (db != null)
                db.close();
            daoMaster = null;
            daoSession = null;
            instance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
