package com.example.dam_proyecto_final.home.homeui.groupui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dam_proyecto_final.R;

import java.util.ArrayList;

public class MembersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> members;

    public MembersAdapter(Context context, ArrayList<String> members) {
        super();
        this.context = context;
        this.members = members;
    }


    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return members.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.activity_group_add_listview_members_item, null);

        TextView txtv_Member = view.findViewById(R.id.txtv_Member);
        txtv_Member.setText(members.get(i));
        ImageButton imgb_MemberDelete = view.findViewById(R.id.imgb_MemberDelete);
        imgb_MemberDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                members.remove(members.get(i));
                notifyDataSetChanged();
            }
        });

        return view;
    }

}
