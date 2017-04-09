package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.dut.moneytracker.currency.CurrencyUtils;

import java.util.Date;

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
    private String currencyCode = CurrencyUtils.DEFAULT_CURRENCY_CODE;
    private String initAmount;
    private Date created;
    private String colorHex;
    private String description;
    private boolean saveLocation;
    private boolean isDefault;

    public Account() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.currencyCode);
        dest.writeString(this.initAmount);
        dest.writeLong(this.created != null ? this.created.getTime() : -1);
        dest.writeString(this.colorHex);
        dest.writeString(this.description);
        dest.writeByte(this.saveLocation ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDefault ? (byte) 1 : (byte) 0);
    }

    protected Account(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.currencyCode = in.readString();
        this.initAmount = in.readString();
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
        this.colorHex = in.readString();
        this.description = in.readString();
        this.saveLocation = in.readByte() != 0;
        this.isDefault = in.readByte() != 0;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
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
