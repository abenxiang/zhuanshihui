package com.sina.shopguide.dto;

import java.io.Serializable;

/**
 * Created by tiger on 18/5/10.
 */

public class WithDrawItem implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    public String ctime;
    public String amount;
    public String from;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
