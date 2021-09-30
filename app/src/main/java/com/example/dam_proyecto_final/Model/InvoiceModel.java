package com.example.dam_proyecto_final.Model;

public class InvoiceModel {
    // Table invoice columns:  `identifier`, `type`, `date`, `start_period`, `end_period`, `consumption`, `amount`, `file`, `filetype`, `idgroup`
    private String
            identifier,
            type,
            date,
            start_period,
            end_period,
            consumption,
            amount,
            filetype,
            idgroup;

    public InvoiceModel(){}

    public InvoiceModel(String identifier, String type, String date, String start_period, String end_period, String consumption, String amount, String filetype, String idgroup) {
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

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getStart_period() {
        return start_period;
    }

    public String getEnd_period() {
        return end_period;
    }

    public String getConsumption() {
        return consumption;
    }

    public String getAmount() {
        return amount;
    }

    public String getFiletype() {
        return filetype;
    }

    public String getIdgroup() {
        return idgroup;
    }
}
