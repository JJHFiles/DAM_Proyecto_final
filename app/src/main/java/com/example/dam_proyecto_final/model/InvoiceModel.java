package com.example.dam_proyecto_final.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class InvoiceModel implements Parcelable {

    private int idInvoice;
    private String identifier;
    private String type;
    private String date;
    private String start_period;
    private String end_period;
    private double consumption;
    private double amount;
    private String filetype;
    private int idgroup;

    public InvoiceModel() {
    }

    public InvoiceModel(String identifier, String type, String date, String start_period, String end_period, double consumption, double amount, String filetype, int idgroup) {
        this.identifier = identifier;
        this.type = type;
        this.date = date;
        this.start_period = start_period;
        this.end_period = end_period;
        this.consumption = consumption;
        this.amount = amount;
        this.filetype = filetype;
        this.idgroup = idgroup;
    }
    public InvoiceModel(int idInvoice, String identifier, String type, String date, String start_period, String end_period, double consumption, double amount, String filetype, int idgroup) {
        this.idInvoice = idInvoice;
        this.identifier = identifier;
        this.type = type;
        this.date = date;
        this.start_period = start_period;
        this.end_period = end_period;
        this.consumption = consumption;
        this.amount = amount;
        this.filetype = filetype;
        this.idgroup = idgroup;
    }



    public static final Creator<InvoiceModel> CREATOR = new Creator<InvoiceModel>() {
        @Override
        public InvoiceModel createFromParcel(Parcel in) {
            return new InvoiceModel(in);
        }

        @Override
        public InvoiceModel[] newArray(int size) {
            return new InvoiceModel[size];
        }
    };

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_period() {
        return start_period;
    }

    public void setStart_period(String start_period) {
        this.start_period = start_period;
    }

    public String getEnd_period() {
        return end_period;
    }

    public void setEnd_period(String end_period) {
        this.end_period = end_period;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public int getIdgroup() {
        return idgroup;
    }

    public void setIdgroup(int idgroup) {
        this.idgroup = idgroup;
    }

    protected InvoiceModel(Parcel in) {
        idInvoice = in.readInt();
        identifier = in.readString();
        type = in.readString();
        date = in.readString();
        start_period = in.readString();
        end_period = in.readString();
        consumption = in.readDouble();
        amount = in.readDouble();
        filetype = in.readString();
        idgroup = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idInvoice);
        parcel.writeString(identifier);
        parcel.writeString(type);
        parcel.writeString(date);
        parcel.writeString(start_period);
        parcel.writeString(end_period);
        parcel.writeDouble(consumption);
        parcel.writeDouble(amount);
        parcel.writeString(filetype);
        parcel.writeInt(idgroup);
    }
}


