package com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dam_proyecto_final.R;

import java.util.ArrayList;

import static com.example.dam_proyecto_final.R.layout.group_invoice_tabui_listview;

public class InvoiceListAdapter extends BaseAdapter {
    private Context context;
    private Activity activity = null;
    private ArrayList<InvoiceListModel> invoiceListModel;


    public InvoiceListAdapter(Context context,
                              Activity activity,
                              ArrayList<InvoiceListModel> invoiceListModel) {
        this.context = context;
        this.activity = activity;
        this.invoiceListModel = invoiceListModel;
    }


    @Override
    public int getCount() {
        return invoiceListModel.size();
    }

    @Override
    public Object getItem(int position) {
        return invoiceListModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return invoiceListModel.get(position).getCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(group_invoice_tabui_listview, null);

        TextView tvType = (TextView) view.findViewById(R.id.tvType);
        tvType.setText(invoiceListModel.get(position).getType());

        TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvAmount.setText(invoiceListModel.get(position).getAmount());

        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvDate.setText(invoiceListModel.get(position).getDate());

        TextView tvBasePrice = (TextView) view.findViewById(R.id.tvConsumption);
        tvBasePrice.setText(invoiceListModel.get(position).getConsumption());

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
    /*    if (position % 2 == 0) {
            ll.setBackgroundColor(Color.LTGRAY);
        } else {
            ll.setBackgroundColor(Color.WHITE);

        }
*/
        return view;
    }
}
