package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 01/03/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentType extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String name;
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
    }

    public PaymentType() {
    }

    protected PaymentType(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<PaymentType> CREATOR = new Parcelable.Creator<PaymentType>() {
        @Override
        public PaymentType createFromParcel(Parcel source) {
            return new PaymentType(source);
        }

        @Override
        public PaymentType[] newArray(int size) {
            return new PaymentType[size];
        }
    };
}
