package com.dut.moneytracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private Context context;
    private List<T> mObjects = new ArrayList<>();

    public BaseRecyclerAdapter(Context context, final List<T> objects) {
        mObjects = objects;
        this.context = context;
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(final T object) {
        if (mObjects == null) {
            mObjects = new ArrayList<>();
        }
        mObjects.add(object);
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        if (mObjects == null) {
            mObjects = new ArrayList<>();
        }
        final int size = getItemCount();
        mObjects.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return mObjects != null ? mObjects.size() : 0;
    }

    public T getItem(final int position) {
        return mObjects.get(position);
    }

    public void removeItem(final int position) {
        if (mObjects == null) {
            mObjects = new ArrayList<>();
            return;
        }
        mObjects.remove(position);
        notifyItemRemoved(position);
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

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(final T object, int index) {
        if (mObjects == null) {
            mObjects = new ArrayList<>();
        }
        mObjects.add(index, object);
        notifyItemInserted(index);
    }

    public void addItems(final List<T> objects) {
        if (mObjects == null) {
            mObjects = new ArrayList<>();
            return;
        }
        mObjects.clear();
        mObjects.addAll(objects);
        notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        if (mObjects == null) {
            return;
        }
        final int position = getPosition(object);
        mObjects.remove(object);
        notifyItemRemoved(position);
    }

    public Context getContext() {
        return context;
    }

    public void setObjects(List<T> mObjects) {
        this.mObjects = mObjects;
    }
}