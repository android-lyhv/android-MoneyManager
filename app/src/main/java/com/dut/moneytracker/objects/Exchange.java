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
public class Exchange extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private int typeExchange;
    private String idAccount;
    private String idCategory;
    private int idDebit;
    private String idAccountTransfer;
    private String codeTransfer;
    private String currencyCode = CurrencyUtils.DEFAULT_CURRENCY_CODE;
    private String amount;
    private String description;
    private Date created;
    private Place place;

    public Exchange() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.typeExchange);
        dest.writeString(this.idAccount);
        dest.writeString(this.idCategory);
        dest.writeInt(this.idDebit);
        dest.writeString(this.idAccountTransfer);
        dest.writeString(this.codeTransfer);
        dest.writeString(this.currencyCode);
        dest.writeString(this.amount);
        dest.writeString(this.description);
        dest.writeLong(this.created != null ? this.created.getTime() : -1);
        dest.writeParcelable(this.place, flags);
    }

    protected Exchange(Parcel in) {
        this.id = in.readString();
        this.typeExchange = in.readInt();
        this.idAccount = in.readString();
        this.idCategory = in.readString();
        this.idDebit = in.readInt();
        this.idAccountTransfer = in.readString();
        this.codeTransfer = in.readString();
        this.currencyCode = in.readString();
        this.amount = in.readString();
        this.description = in.readString();
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
        this.place = in.readParcelable(Place.class.getClassLoader());
    }

    public static final Creator<Exchange> CREATOR = new Creator<Exchange>() {
        @Override
        public Exchange createFromParcel(Parcel source) {
            return new Exchange(source);
        }

        @Override
        public Exchange[] newArray(int size) {
            return new Exchange[size];
        }
    };
}
