package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/02/2017.
 */

public class Exchange extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String idAccount;
    private String idCategory;
    private String idPaymentType;
    private String currencyCode;
    private int type;
    private String amount;
    private String note;
    private Place place;
    private Date created;
    private RealmList<Attachment> attachments;

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

    public String getIdPaymentType() {
        return idPaymentType;
    }

    public void setIdPaymentType(String idPaymentType) {
        this.idPaymentType = idPaymentType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public RealmList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(RealmList<Attachment> attachments) {
        this.attachments = attachments;
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
        dest.writeString(this.idPaymentType);
        dest.writeString(this.currencyCode);
        dest.writeInt(this.type);
        dest.writeString(this.amount);
        dest.writeString(this.note);
        dest.writeParcelable(this.place,flags);
        dest.writeLong(this.created != null ? this.created.getTime() : -1);
        dest.writeList(this.attachments);
    }

    public Exchange() {
    }

    protected Exchange(Parcel in) {
        this.id = in.readString();
        this.idAccount = in.readString();
        this.idCategory = in.readString();
        this.idPaymentType = in.readString();
        this.currencyCode = in.readString();
        this.type = in.readInt();
        this.amount = in.readString();
        this.note = in.readString();
        this.place = (Place) in.readSerializable();
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
        this.attachments = new RealmList<>();
        in.readList(this.attachments, Attachment.class.getClassLoader());
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
