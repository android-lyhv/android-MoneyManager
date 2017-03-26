package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
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
public class GroupCategory extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String name;
    private String tag;
    private String description;
    private byte[] byteImage;
    private RealmList<Category> categories;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.tag);
        dest.writeByteArray(this.byteImage);
        dest.writeList(this.categories);
    }

    public GroupCategory() {
    }

    protected GroupCategory(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.tag = in.readString();
        this.byteImage = in.createByteArray();
        this.categories = new RealmList<>();
        in.readList(this.categories, Category.class.getClassLoader());
    }

    public static final Parcelable.Creator<GroupCategory> CREATOR = new Parcelable.Creator<GroupCategory>() {
        @Override
        public GroupCategory createFromParcel(Parcel source) {
            return new GroupCategory(source);
        }

        @Override
        public GroupCategory[] newArray(int size) {
            return new GroupCategory[size];
        }
    };
}
