package com.dut.moneytracker.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 15/03/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Filter implements Parcelable {
    private Date dateFilter;
    private String accountId;
    private int viewType;
    private Date formDate;
    private Date toDate;
    private boolean isRequestByAccount;

    public Filter() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.dateFilter != null ? this.dateFilter.getTime() : -1);
        dest.writeString(this.accountId);
        dest.writeInt(this.viewType);
        dest.writeLong(this.formDate != null ? this.formDate.getTime() : -1);
        dest.writeLong(this.toDate != null ? this.toDate.getTime() : -1);
        dest.writeByte(this.isRequestByAccount ? (byte) 1 : (byte) 0);
    }

    protected Filter(Parcel in) {
        long tmpDateFilter = in.readLong();
        this.dateFilter = tmpDateFilter == -1 ? null : new Date(tmpDateFilter);
        this.accountId = in.readString();
        this.viewType = in.readInt();
        long tmpStartDate = in.readLong();
        this.formDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = in.readLong();
        this.toDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
        this.isRequestByAccount = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel source) {
            return new Filter(source);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}
