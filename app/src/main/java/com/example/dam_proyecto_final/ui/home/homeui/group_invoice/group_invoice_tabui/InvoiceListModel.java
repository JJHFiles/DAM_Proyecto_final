package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class InvoiceListModel {

    private int code;
    private String type;
    private String amount;
    private String date;
    private String consumption;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }


}
