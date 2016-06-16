package com.lsm.barrister.data.db;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

public class Action<T> {

    AbstractDao<T, String> dao;

    public Action(AbstractDao<T, String> dao) {
        super();
        this.dao = dao;
    }

    public synchronized List<T> loadAll() {
        return dao.loadAll();
    }

    public synchronized List<T> loadAll(String order, Property properties) {
        QueryBuilder<T> qb = dao.queryBuilder();
        if (order.equals("ASC")) {
            qb.orderAsc(properties);
        } else
            qb.orderDesc(properties);
        return qb.build().list();
    }

    public synchronized List<T> where(WhereCondition whereCondition,
                                      Property order) {
        QueryBuilder<T> qb = dao.queryBuilder();
        dao.queryBuilder();
        qb.orderAsc(order);
        qb.where(whereCondition);
        return qb.build().list();
    }

    public synchronized List<T> and(WhereCondition w1, WhereCondition w2,
                                    Property order) {
        QueryBuilder<T> qb = dao.queryBuilder();
        dao.queryBuilder();
        qb.and(w1, w2);
        qb.orderDesc(order);
        return qb.build().list();
    }

    public synchronized List<T> and(WhereCondition w1, WhereCondition w2, WhereCondition w3) {
        QueryBuilder<T> qb = dao.queryBuilder();
        dao.queryBuilder();
        qb.and(w1, w2, w3);
        return qb.build().list();
    }

    public synchronized List<T> or(WhereCondition w1, WhereCondition w2, WhereCondition w3) {
        QueryBuilder<T> qb = dao.queryBuilder();
        dao.queryBuilder();
        qb.or(w1, w2, w3);
        return qb.build().list();
    }

    public synchronized void save(final List<T> list) {
        // TODO Auto-generated method stub
        if (list == null || list.isEmpty()) {
            return;
        }
        try {
            dao.getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < list.size(); i++) {
                        T note = list.get(i);
                        dao.insertOrReplace(note);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized T load(WhereCondition where) {
        QueryBuilder<T> qb = dao.queryBuilder();
        qb.where(where);
        return qb.count() > 0 ? qb.list().get(0) : null;
    }

    public synchronized boolean contains(WhereCondition where) {
        QueryBuilder<T> qb = dao.queryBuilder();
        qb.where(where);
        return qb.count() > 0;
    }

    public synchronized List<T> query(WhereCondition where) {
        QueryBuilder<T> qb = dao.queryBuilder();
        qb.where(where);
        return qb.list();
    }

    public synchronized List<T> query(String where, String... params) {
        return dao.queryRaw(where, params);
    }

    public synchronized List<T> query(WhereCondition where, String order,
                                      Property properties) {
        QueryBuilder<T> qb = dao.queryBuilder();
        qb.where(where);
        if (order.equals("ASC"))
            qb.orderAsc(properties);
        else
            qb.orderDesc(properties);

        return qb.list();
    }

    public boolean isSaved(WhereCondition where) {
        QueryBuilder<T> qb = dao.queryBuilder();
        qb.where(where);
        qb.buildCount().count();
        return qb.buildCount().count() > 0 ? true : false;
    }

    public synchronized void delete(T arg) {
        dao.delete(arg);
    }

    public synchronized void delete(WhereCondition where) {
        QueryBuilder<T> qb = dao.queryBuilder();
        DeleteQuery<T> dq = qb.where(where).buildDelete();
        dq.executeDeleteWithoutDetachingEntities();
    }

    public synchronized void deleteAll() {
        dao.deleteAll();
    }

    public synchronized long save(T arg) {
        try {
            return dao.insertOrReplace(arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long count() {
        return dao.count();
    }

    public AbstractDao<T, String> getDao() {
        return dao;
    }
}