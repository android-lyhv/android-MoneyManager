package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Category extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String idGroup;
    private String name;
    private byte[] byteImage;
    private int colorCode;

    public Category() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.idGroup);
        dest.writeString(this.name);
        dest.writeByteArray(this.byteImage);
        dest.writeInt(this.colorCode);
    }

    protected Category(Parcel in) {
        this.id = in.readString();
        this.idGroup = in.readString();
        this.name = in.readString();
        this.byteImage = in.createByteArray();
        this.colorCode = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
