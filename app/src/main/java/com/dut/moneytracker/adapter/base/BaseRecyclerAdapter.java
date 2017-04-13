package com.dut.moneytracker.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

public abstract class BaseRecyclerAdapter<T extends RealmModel, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private Context mContext;
    private RealmResults<T> mObjects;
    private Realm mRealm;

    public BaseRecyclerAdapter(final Context context, final RealmResults<T> objects) {
        mObjects = objects;
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        mRealm.beginTransaction();
        mObjects.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    public void removeItem(final int position) {
        mRealm.beginTransaction();
        mObjects.deleteFromRealm(position);
        mRealm.commitTransaction();
    }

    @Override
    public int getItemCount() {
        return mObjects != null ? mObjects.size() : 0;
    }

    public T getItem(final int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(final T item) {
        return mObjects.indexOf(item);
    }

    public Context getContext() {
        return mContext;
    }

    public void setObjects(RealmResults<T> mObjects) {
        this.mObjects = mObjects;
    }

    public int getSize() {
        if (mObjects == null) {
            return 0;
        }
        return mObjects.size();
    }
}