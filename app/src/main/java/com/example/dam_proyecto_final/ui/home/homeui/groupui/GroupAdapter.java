package com.example.dam_proyecto_final.ui.home.homeui.groupui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.R;

import java.util.ArrayList;

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GroupModel> groupModels;

    public GroupAdapter(Context context, ArrayList<GroupModel> groupModels) {
        super();
        this.context = context;
        this.groupModels = groupModels;
    }


    @Override
    public int getCount() {
        return groupModels.size();
    }

    @Override
    public Object getItem(int i) {
        return groupModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return groupModels.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.fragment_group_listview_group_item, null);

        TextView txtv_GITitle = view.findViewById(R.id.txtv_GITitle);
        txtv_GITitle.setText(groupModels.get(i).getName());
        TextView txtv_GIDescription = view.findViewById(R.id.txtv_GIDescription);
        txtv_GIDescription.setText(groupModels.get(i).getDescription());

        return view;
    }

}

