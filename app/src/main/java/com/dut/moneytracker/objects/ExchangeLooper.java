package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 23/03/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExchangeLooper extends RealmObject implements Parcelable {
    RealmList<Exchange> exchanges;

    public ExchangeLooper() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.exchanges);
    }

    protected ExchangeLooper(Parcel in) {
        this.exchanges = new RealmList<>();
        in.readList(this.exchanges, Debit.class.getClassLoader());
    }

    public static final Parcelable.Creator<ExchangeLooper> CREATOR = new Parcelable.Creator<ExchangeLooper>() {
        @Override
        public ExchangeLooper createFromParcel(Parcel source) {
            return new ExchangeLooper(source);
        }

        @Override
        public ExchangeLooper[] newArray(int size) {
            return new ExchangeLooper[size];
        }
    };
}
