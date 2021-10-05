package com.example.dam_proyecto_final.home.homeui.groupui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dam_proyecto_final.model.MemberModel;
import com.example.dam_proyecto_final.R;

import java.util.ArrayList;

public class MembersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MemberModel> memberModels;
    private ListView lstv_Members;

    public MembersAdapter(Context context, ArrayList<MemberModel> memberModels, ListView lstv_Members) {
        super();
        this.context = context;
        this.memberModels = memberModels;
        this.lstv_Members = lstv_Members;
    }


    @Override
    public int getCount() {
        return memberModels.size();
    }

    @Override
    public Object getItem(int i) {
        return memberModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.activity_group_add_listview_members_item, null);

        String role = "";
        if (memberModels.get(i).getRole()==0){
            role = view.getResources().getString(R.string.role_admin);
        } else if (memberModels.get(i).getRole()==1){
            role = view.getResources().getString(R.string.role_editor);
        } else if (memberModels.get(i).getRole()==2){
            role = view.getResources().getString(R.string.role_reader);
        }


        TextView txtv_Member = view.findViewById(R.id.txtv_Member);
        txtv_Member.setText(memberModels.get(i).getEmail());
        TextView txtv_MemberRole = view.findViewById(R.id.txtv_MemberRole);
        txtv_MemberRole.setText(role);
        ImageButton imgb_MemberDelete = view.findViewById(R.id.imgb_MemberDelete);
        imgb_MemberDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberModels.remove(memberModels.get(i));
                notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lstv_Members);
            }
        });

        return view;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        MembersAdapter listAdapter = (MembersAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
