package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui.edit_group;


import static com.example.dam_proyecto_final.R.layout.activity_group_invoice_edit_group_member_adapter_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.MemberModel;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

public class GroupInvoiceEditGroupMemberAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MemberModel> membersLis, membersAdd, membersUpd, membersDel;
    private ListView lstv_Members;
    private String guiUser, roleSelection;

    public GroupInvoiceEditGroupMemberAdapter(Context context,
                                              ArrayList<MemberModel> membersLis,
                                              ArrayList<MemberModel> membersAdd,
                                              ArrayList<MemberModel> membersUpd,
                                              ArrayList<MemberModel> membersDel,
                                              ListView lstv_Members,
                                              String guiUser) {
        super();
        this.context = context;
        this.membersLis = membersLis;
        this.membersAdd = membersAdd;
        this.membersUpd = membersUpd;
        this.membersDel = membersDel;
        this.lstv_Members = lstv_Members;
        this.guiUser = guiUser;
    }


    @Override
    public int getCount() {
        return membersLis.size();
    }

    @Override
    public Object getItem(int i) {
        return membersLis.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(activity_group_invoice_edit_group_member_adapter_item, null);

        String role = "";
        if (membersLis.get(i).getRole() == 0) {
            role = view.getResources().getString(R.string.role_admin);
        } else if (membersLis.get(i).getRole() == 1) {
            role = view.getResources().getString(R.string.role_editor);
        } else if (membersLis.get(i).getRole() == 2) {
            role = view.getResources().getString(R.string.role_reader);
        }


        TextView txtv_Member = view.findViewById(R.id.edt_AGIEG_AddMemberList);
        txtv_Member.setText(membersLis.get(i).getEmail());
        Spinner membersRole = view.findViewById(R.id.dd_AGIEG_RoleList);
        //txtv_MemberRole.setText(role);

        String[] roles = {
                context.getString(R.string.role_admin),
                context.getString(R.string.role_editor),
                context.getString(R.string.role_reader)
        };

        ArrayAdapter rolesAdapter = new ArrayAdapter(context, R.layout.activity_group_edit_dropdown_item, roles);
        //rolesAdapter.setDropDownViewResource(R.layout.activity_group_edit_dropdown_item);


        membersRole.setAdapter(rolesAdapter);

        membersRole.setSelection(membersLis.get(i).getRole());

        membersRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isRoleChanged = false;
                for (int x = 0; x < membersUpd.size(); x++) {
                    if (membersUpd.get(x).getEmail().equals(membersLis.get(i).getEmail())) {
                        membersUpd.get(x).setRole(position);
                        membersLis.get(i).setRole(position);
                        Toast.makeText(context, "" +
                                "Role:" + position, Toast.LENGTH_LONG).show();
                        isRoleChanged = true;
                    }
                }
                if (!isRoleChanged) {
                    membersUpd.add(new MemberModel(membersLis.get(i).getEmail(), position));


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ImageButton imgb_MemberDelete = view.findViewById(R.id.imgb_AGIEG_DelMemberList);
        imgb_MemberDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isUserFound = false;

                for (int x = 0; x < membersAdd.size(); x++) {
                    if (membersAdd.get(x).getEmail().equals(membersLis.get(i).getEmail())) {
                        membersAdd.remove(membersAdd.get(x));
                      //  isUserFound = true;
                     //   membersLis.remove(membersLis.get(i));
                    }
                }

                if (!isUserFound) {
                    for (int x = 0; x < membersUpd.size(); x++) {
                        if (membersUpd.get(x).getEmail().equals(membersLis.get(i).getEmail())) {
                            membersUpd.remove(membersUpd.get(x));
                        //    isUserFound = true;
                        //    membersLis.remove(membersLis.get(i));

                        }
                    }
                }

                // En el caso de que el usuario a eliminar no sea el usuario de la gui
                if (!isUserFound) {
                    if (!membersLis.get(i).getEmail().equals(guiUser)) {
                        if (membersDel.size() > 0) {
                            for (int x = 0; x < membersDel.size(); x++) {
                                if (!membersDel.get(x).getEmail().equals(membersLis.get(i).getEmail())) {
                                    membersDel.add(membersLis.get(i));
                             //       isUserFound = true;
                             //       membersLis.remove(membersLis.get(i));
                                }
                            }
                        } else {
                            membersDel.add(membersLis.get(i));
                         //   isUserFound = true;
                         //   membersLis.remove(membersLis.get(i));


                        }

                    }
                }


                    /* Usuario de la gui quiere salir del grupo, el usuario a eliminar es el usuario
                    de la gui y queda algun usuario en el grupo */
                if (!isUserFound) {
                    if (membersLis.get(i).getEmail().equals(guiUser) && membersLis.size() > 1) {
                        if (membersDel.size() > 0) {
                            for (int x = 0; x < membersDel.size(); x++) {
                                if (!membersDel.get(x).getEmail().equals(membersLis.get(i).getEmail())) {
                                    membersDel.add(membersLis.get(i));
                                    Toast.makeText(context, "" +
                                            "saliendo del grupo ", Toast.LENGTH_LONG).show();
                                   // membersLis.remove(membersLis.get(i));
                                }
                            }
                        } else {
                            membersDel.add(membersLis.get(i));
                            Toast.makeText(context, "" +
                                    "saliendo del grupo ", Toast.LENGTH_LONG).show();
                        //    membersLis.remove(membersLis.get(i));
                        }
                    }
                }
                membersLis.remove(membersLis.get(i));

                // Para que los cambios se hagan efectivos en el ListView de miembros
                notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lstv_Members);
            }
        });

        return view;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        GroupInvoiceEditGroupMemberAdapter listAdapter = (GroupInvoiceEditGroupMemberAdapter) listView.getAdapter();
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
