package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/02/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Account extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String name;
    private String currencyCode;
    private String initAmount;
    private Date created;
    private String colorCode;
    private boolean saveLocation;
    private boolean isDefault;
    private RealmList<Exchange> exchanges;
    private RealmList<Debit> debits;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.currencyCode);
        dest.writeString(this.name);
        dest.writeString(this.initAmount);
        dest.writeLong(this.created != null ? this.created.getTime() : -1);
        dest.writeString(this.colorCode);
        dest.writeByte(this.saveLocation ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDefault ? (byte) 1 : (byte) 0);
        dest.writeList(this.exchanges);
        dest.writeList(this.debits);
    }

    public Account() {
    }

    protected Account(Parcel in) {
        this.id = in.readString();
        this.currencyCode = in.readString();
        this.name = in.readString();
        this.initAmount = in.readString();
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
        this.colorCode = in.readString();
        this.saveLocation = in.readByte() != 0;
        this.isDefault = in.readByte() != 0;
        this.exchanges = new RealmList<>();
        this.debits = new RealmList<>();
        in.readList(this.debits, Debit.class.getClassLoader());
        in.readList(this.exchanges, Exchange.class.getClassLoader());
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
