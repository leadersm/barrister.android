package com.lsm.barrister.data.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.lsm.barrister.data.db.PushMessage;

import com.lsm.barrister.data.db.PushMessageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig pushMessageDaoConfig;

    private final PushMessageDao pushMessageDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        pushMessageDaoConfig = daoConfigMap.get(PushMessageDao.class).clone();
        pushMessageDaoConfig.initIdentityScope(type);

        pushMessageDao = new PushMessageDao(pushMessageDaoConfig, this);

        registerDao(PushMessage.class, pushMessageDao);
    }
    
    public void clear() {
        pushMessageDaoConfig.getIdentityScope().clear();
    }

    public PushMessageDao getPushMessageDao() {
        return pushMessageDao;
    }

}