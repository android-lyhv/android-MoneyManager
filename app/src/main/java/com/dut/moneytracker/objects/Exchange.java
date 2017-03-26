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
    private String idAccountTransfer;
    private String currencyCode;
    private String amount;
    private String description;
    private Date created;
    private boolean isLoop;
    private int typeLoop;
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
        dest.writeString(this.idAccountTransfer);
        dest.writeString(this.currencyCode);
        dest.writeString(this.amount);
        dest.writeString(this.description);
        dest.writeLong(this.created != null ? this.created.getTime() : -1);
        dest.writeByte(this.isLoop ? (byte) 1 : (byte) 0);
        dest.writeInt(this.typeLoop);
        dest.writeParcelable(this.place, flags);
    }

    protected Exchange(Parcel in) {
        this.id = in.readString();
        this.typeExchange = in.readInt();
        this.idAccount = in.readString();
        this.idCategory = in.readString();
        this.idAccountTransfer = in.readString();
        this.currencyCode = in.readString();
        this.amount = in.readString();
        this.description = in.readString();
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
        this.isLoop = in.readByte() != 0;
        this.typeLoop = in.readInt();
        this.place = in.readParcelable(Place.class.getClassLoader());
    }

    public static final Creator<Exchange> CREATOR = new Creator<Exchange>() {
        @Override
        public Exchange createFromParcel(Parcel in) {
            return new Exchange(in);
        }

        @Override
        public Exchange[] newArray(int size) {
            return new Exchange[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id:").append(id).append("\n")
                .append("typeExchange: ").append(typeExchange).append("\n")
                .append("idAccount: ").append(idAccount).append("\n")
                .append("idCategory: ").append(idCategory).append("\n")
                .append("amount: ").append(amount).append("\n")
                .append("idAccountTransfer: ").append(idAccountTransfer).append("\n")
                .append("created: ").append(created).append("\n")
                .append("isLoop: ").append(isLoop).append("\n")
                .append("typeLoop: ").append(typeLoop).append("\n")
                .append("currencyCode: ").append(currencyCode).append("\n")
                .append("place: ").append(place).append("\n");
        return stringBuilder.toString();
    }
}