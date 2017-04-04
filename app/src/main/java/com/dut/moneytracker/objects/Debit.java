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
public class Debit extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String amount;
    private String idAccount;
    private int typeDebit;
    private boolean isClose;
    private Date create;
    private Date expires;
    private String name;
    private String description;

    public Debit() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.amount);
        dest.writeString(this.idAccount);
        dest.writeInt(this.typeDebit);
        dest.writeByte(this.isClose ? (byte) 1 : (byte) 0);
        dest.writeLong(this.create != null ? this.create.getTime() : -1);
        dest.writeLong(this.expires != null ? this.expires.getTime() : -1);
        dest.writeString(this.name);
        dest.writeString(this.description);
    }

    protected Debit(Parcel in) {
        this.id = in.readString();
        this.amount = in.readString();
        this.idAccount = in.readString();
        this.typeDebit = in.readInt();
        this.isClose = in.readByte() != 0;
        long tmpCreate = in.readLong();
        this.create = tmpCreate == -1 ? null : new Date(tmpCreate);
        long tmpExpires = in.readLong();
        this.expires = tmpExpires == -1 ? null : new Date(tmpExpires);
        this.name = in.readString();
        this.description = in.readString();
    }

    public static final Creator<Debit> CREATOR = new Creator<Debit>() {
        @Override
        public Debit createFromParcel(Parcel source) {
            return new Debit(source);
        }

        @Override
        public Debit[] newArray(int size) {
            return new Debit[size];
        }
    };
}
