package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/02/2017.
 */

public class Exchange extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private int typeExchange;// thu nhap, chuyen tien, chi tieu, di vay, cho vay
    private String idAccount;
    private String idCategory;
    private String idAccountTransfer;// truong hop chuyen tien
    private String currencyCode;
    private String amount;
    private String description;
    private Date created;
    private ExchangePlace exchangePlace;

    public String getIdAccountTransfer() {
        return idAccountTransfer;
    }

    public void setIdAccountTransfer(String idAccountTransfer) {
        this.idAccountTransfer = idAccountTransfer;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public int getTypeExchange() {
        return typeExchange;
    }

    public void setTypeExchange(int typeExchange) {
        this.typeExchange = typeExchange;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExchangePlace getExchangePlace() {
        return exchangePlace;
    }

    public void setExchangePlace(ExchangePlace exchangePlace) {
        this.exchangePlace = exchangePlace;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.idAccount);
        dest.writeString(this.idCategory);
        dest.writeString(this.currencyCode);
        dest.writeInt(this.typeExchange);
        dest.writeString(this.amount);
        dest.writeString(this.description);
        dest.writeParcelable(this.exchangePlace, flags);
        dest.writeString(this.idAccountTransfer);
        dest.writeLong(this.created != null ? this.created.getTime() : -1);
    }

    public Exchange() {
    }

    protected Exchange(Parcel in) {
        this.id = in.readString();
        this.idAccount = in.readString();
        this.idCategory = in.readString();
        this.currencyCode = in.readString();
        this.typeExchange = in.readInt();
        this.amount = in.readString();
        this.description = in.readString();
        this.exchangePlace = in.readParcelable(ExchangePlace.class.getClassLoader());
        this.idAccountTransfer = in.readString();
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
    }

    public static final Parcelable.Creator<Exchange> CREATOR = new Parcelable.Creator<Exchange>() {
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
