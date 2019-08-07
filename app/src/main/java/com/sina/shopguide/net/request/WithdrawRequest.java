package com.sina.shopguide.net.request;

import com.sina.shopguide.net.HttpParam;

/**
 * Created by tiger on 18/5/10.
 */

public class WithdrawRequest extends BaseRequestParams {
    public String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
