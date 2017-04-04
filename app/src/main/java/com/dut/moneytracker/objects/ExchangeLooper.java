package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 23/03/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExchangeLooper extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private int typeExchange;
    private String idAccount;
    private String idCategory;
    private String idAccountTransfer;
    private String codeTransfer;
    private String currencyCode;
    private String amount;
    private String description;
    private Date created;
    private boolean isLoop;
    private int typeLoop;
    private Place place;

    public ExchangeLooper() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.typeExchange);
        dest.writeString(this.idAccount);
        dest.writeString(this.idCategory);
        dest.writeString(this.idAccountTransfer);
        dest.writeString(this.codeTransfer);
        dest.writeString(this.currencyCode);
        dest.writeString(this.amount);
        dest.writeString(this.description);
        dest.writeLong(this.created != null ? this.created.getTime() : -1);
        dest.writeByte(this.isLoop ? (byte) 1 : (byte) 0);
        dest.writeInt(this.typeLoop);
        dest.writeParcelable(this.place, flags);
    }

    protected ExchangeLooper(Parcel in) {
        this.id = in.readInt();
        this.typeExchange = in.readInt();
        this.idAccount = in.readString();
        this.idCategory = in.readString();
        this.idAccountTransfer = in.readString();
        this.codeTransfer = in.readString();
        this.currencyCode = in.readString();
        this.amount = in.readString();
        this.description = in.readString();
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
        this.isLoop = in.readByte() != 0;
        this.typeLoop = in.readInt();
        this.place = in.readParcelable(Place.class.getClassLoader());
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
