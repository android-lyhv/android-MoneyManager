package com.dut.moneytracker.objects;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 15/03/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Filter {
    private Date dateFilter;
    private String accountId;
    private int viewType;
    private boolean isRequestByAccount;
}
