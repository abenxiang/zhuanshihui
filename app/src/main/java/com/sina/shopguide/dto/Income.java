package com.sina.shopguide.dto;

import java.io.Serializable;

/**
 * Created by tiger on 18/5/10.
 */

public class Income implements Serializable {

    private static final long serialVersionUID = 4313185640041769093L;

    public String amount;
    public String all_amount;

    public String last_month_commision;
    public String this_month_commision;
    public String last_month_partner_commision;
    public String today_commision;
    public String today_partner_commision;

    public String today_order_count;
    public String last_day_commision;
    public String last_day_partner_commision;

    public String last_day_order_count;
    public String seven_days_commision;
    public String seven_days_partner_commision;

    public String seven_days_order_count;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAll_amount() {
        return all_amount;
    }

    public void setAll_amount(String all_amount) {
        this.all_amount = all_amount;
    }

    public String getLast_month_commision() {
        return last_month_commision;
    }

    public void setLast_month_commision(String last_month_commision) {
        this.last_month_commision = last_month_commision;
    }

    public String getThis_month_commision() {
        return this_month_commision;
    }

    public void setThis_month_commision(String this_month_commision) {
        this.this_month_commision = this_month_commision;
    }

    public String getLast_month_partner_commision() {
        return last_month_partner_commision;
    }

    public void setLast_month_partner_commision(String last_month_partner_commision) {
        this.last_month_partner_commision = last_month_partner_commision;
    }

    public String getToday_commision() {
        return today_commision;
    }

    public void setToday_commision(String today_commision) {
        this.today_commision = today_commision;
    }

    public String getToday_partner_commision() {
        return today_partner_commision;
    }

    public void setToday_partner_commision(String today_partner_commision) {
        this.today_partner_commision = today_partner_commision;
    }

    public String getToday_order_count() {
        return today_order_count;
    }

    public void setToday_order_count(String today_order_count) {
        this.today_order_count = today_order_count;
    }

    public String getLast_day_commision() {
        return last_day_commision;
    }

    public void setLast_day_commision(String last_day_commision) {
        this.last_day_commision = last_day_commision;
    }

    public String getLast_day_partner_commision() {
        return last_day_partner_commision;
    }

    public void setLast_day_partner_commision(String last_day_partner_commision) {
        this.last_day_partner_commision = last_day_partner_commision;
    }

    public String getLast_day_order_count() {
        return last_day_order_count;
    }

    public void setLast_day_order_count(String last_day_order_count) {
        this.last_day_order_count = last_day_order_count;
    }

    public String getSeven_days_commision() {
        return seven_days_commision;
    }

    public void setSeven_days_commision(String seven_days_commision) {
        this.seven_days_commision = seven_days_commision;
    }

    public String getSeven_days_partner_commision() {
        return seven_days_partner_commision;
    }

    public void setSeven_days_partner_commision(String seven_days_partner_commision) {
        this.seven_days_partner_commision = seven_days_partner_commision;
    }

    public String getSeven_days_order_count() {
        return seven_days_order_count;
    }

    public void setSeven_days_order_count(String seven_days_order_count) {
        this.seven_days_order_count = seven_days_order_count;
    }
}
