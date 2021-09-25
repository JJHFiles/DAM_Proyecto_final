package com.example.dam_proyecto_final.home.homeui.groupui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dam_proyecto_final.Model.Group;
import com.example.dam_proyecto_final.R;

import java.util.ArrayList;

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Group> groups;

    public GroupAdapter(Context context, ArrayList<Group> groups) {
        super();
        this.context = context;
        this.groups = groups;
    }


    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int i) {
        return groups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return groups.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.fragment_group_listview_group_item, null);

        TextView txtv_GITitle = view.findViewById(R.id.txtv_GITitle);
        txtv_GITitle.setText(groups.get(i).getNombre());
        TextView txtv_GIDescription = view.findViewById(R.id.txtv_GIDescription);
        txtv_GIDescription.setText(groups.get(i).getDescripción());

        return view;
    }

}

