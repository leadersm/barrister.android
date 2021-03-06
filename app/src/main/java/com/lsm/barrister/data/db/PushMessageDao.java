package com.lsm.barrister.data.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.lsm.barrister.data.db.PushMessage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PUSH_MESSAGE.
*/
public class PushMessageDao extends AbstractDao<PushMessage, String> {

    public static final String TABLENAME = "PUSH_MESSAGE";

    /**
     * Properties of entity PushMessage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Type = new Property(1, String.class, "type", false, "TYPE");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property ContentId = new Property(3, String.class, "contentId", false, "CONTENT_ID");
        public final static Property Date = new Property(4, String.class, "date", false, "DATE");
        public final static Property Read = new Property(5, Boolean.class, "read", false, "READ");
    };


    public PushMessageDao(DaoConfig config) {
        super(config);
    }
    
    public PushMessageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PUSH_MESSAGE' (" + //
                "'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "'TYPE' TEXT NOT NULL ," + // 1: type
                "'CONTENT' TEXT," + // 2: content
                "'CONTENT_ID' TEXT," + // 3: contentId
                "'DATE' TEXT," + // 4: date
                "'READ' INTEGER);"); // 5: read
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PUSH_MESSAGE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PushMessage entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
        stmt.bindString(2, entity.getType());
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        String contentId = entity.getContentId();
        if (contentId != null) {
            stmt.bindString(4, contentId);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(5, date);
        }
 
        Boolean read = entity.getRead();
        if (read != null) {
            stmt.bindLong(6, read ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PushMessage readEntity(Cursor cursor, int offset) {
        PushMessage entity = new PushMessage( //
            cursor.getString(offset + 0), // id
            cursor.getString(offset + 1), // type
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // contentId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // date
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0 // read
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PushMessage entity, int offset) {
        entity.setId(cursor.getString(offset + 0));
        entity.setType(cursor.getString(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setContentId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDate(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRead(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(PushMessage entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(PushMessage entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
