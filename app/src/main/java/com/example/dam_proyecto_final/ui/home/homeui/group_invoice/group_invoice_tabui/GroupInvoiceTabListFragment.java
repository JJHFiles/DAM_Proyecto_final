package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.InvoiceListModel;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.GroupInvoiceAdd;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.model.InvoiceModel;

import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class GroupInvoiceTabListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv_invoice;
    private ConstraintLayout root_background;
    private TabLayout tabLayout;
    private ImageButton ibAdd;
    private Context context;
    private ExtendedFloatingActionButton btOCR, btManual;



    private static final String GROUPMODEL = "groupModel";
    private static final String USEREMAIL = "userEmail";
    private static final String USERPASS = "userPass";
    private static final String INVOICES = "invoices";


    private GroupModel groupModel;
    private String userEmail;
    private String userPass;
    private ArrayList<InvoiceModel> arrIM;



    public GroupInvoiceTabListFragment() {
        // Required empty public constructor
    }


    public static GroupInvoiceTabListFragment newInstance(GroupModel groupModel, String userEmail, String userPass,
                                                          ArrayList<InvoiceModel> invoices) {
        GroupInvoiceTabListFragment fragment = new GroupInvoiceTabListFragment();
        Bundle args = new Bundle();
        args.putSerializable(GROUPMODEL, groupModel);
        args.putString(USEREMAIL, userEmail);
        args.putString(USERPASS, userPass);
        args.putParcelableArrayList(INVOICES, invoices);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupModel = (GroupModel) getArguments().getSerializable(GROUPMODEL);
            userEmail = getArguments().getString(USEREMAIL);
            userPass = getArguments().getString(USERPASS);
            arrIM = getArguments().getParcelableArrayList(INVOICES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_invoice_tab_list, container, false);
        context = view.getContext();

        WebApiRequest webApiRequest = new WebApiRequest(context);

        //FAB Buttons
        ibAdd = view.findViewById(R.id.ibAdd);
        ibAdd.setOnClickListener(this);
        btManual = view.findViewById(R.id.btManual);
        btOCR = view.findViewById(R.id.btOCR);
        btManual.setOnClickListener(this);
        btOCR.setOnClickListener(this);

        //ListView
        lv_invoice = view.findViewById(R.id.lv_invoice);
        lv_invoice.setOnItemClickListener(this);

        root_background = view.findViewById(R.id.root_background);
        root_background.setOnClickListener(this);

        //TabLayout, seleccionamos la tab correspondiente por si venimos de hacer back
        tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.getTabAt(0).select();
        applyPermission();


        if (getArguments() != null) {
            fillListViewCustomAdapter(arrIM);
        }


        return view;
    }


    private void fillListViewCustomAdapter(ArrayList<InvoiceModel> arrIM) {
        ArrayList<InvoiceListModel> ilm = new ArrayList<InvoiceListModel>();
        for (int x = 0; x < arrIM.size(); x++) {
            String measure = "";

            //Obtenemos measure
            if (arrIM.get(x).getType().equals(getString(R.string.tab_Electricity))) {
                measure = getString(R.string.tab_Kw);
            } else if (arrIM.get(x).getType().equals(getString(R.string.tab_Gas))) {
                measure = getString(R.string.tab_Kw);
            } else if (arrIM.get(x).getType().equals(getString(R.string.tab_Water))) {
                measure = getString(R.string.tab_m3);
            } else if (arrIM.get(x).getType().equals(getString(R.string.tab_Telephone))) {
                measure = getString(R.string.tab_month);
            } else if (arrIM.get(x).getType().equals(getString(R.string.tab_rental))) {
                measure = getString(R.string.tab_month);
            } else if (arrIM.get(x).getType().equals(getString(R.string.tab_others))) {
                measure = "";
            }


            ilm.add(new InvoiceListModel());
            ilm.get(x).setType(getString(R.string.tab_invoice) + arrIM.get(x).getType());
            ilm.get(x).setAmount(arrIM.get(x).getAmount() + " " + groupModel.getCurrency());
            ilm.get(x).setDate(arrIM.get(x).getDate());
            ilm.get(x).setConsumption(arrIM.get(x).getConsumption() + measure);
            ilm.get(x).setCode(x);
        }

        InvoiceListAdapter ila = new InvoiceListAdapter(context, getActivity(), ilm);
        lv_invoice.setAdapter(ila);
    }


    @Override
    public void onClick(View v) {
        int choice = v.getId();
        switch (v.getId()) {
            case R.id.ibAdd:

                //FAB Principal
                if (btManual.getVisibility() == View.INVISIBLE) {
                    btManual.setVisibility(View.VISIBLE);
                    btOCR.setVisibility(View.VISIBLE);
                } else {
                    btManual.setVisibility(View.INVISIBLE);
                    btOCR.setVisibility(View.INVISIBLE);
                }

                break;

            case R.id.btManual:
                // Abre activity para añadir nuevas facturas manuales
                Intent intent = new Intent(context, GroupInvoiceAdd.class);
                intent.putExtra("groupModel", groupModel);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userPass", userPass);

                startActivity(intent);
                break;

            case R.id.btOCR:
                Intent intentScan = new Intent(context, InvoiceOCRAddActivity.class);
                intentScan.putExtra("groupModel", groupModel);
                intentScan.putExtra("userEmail", userEmail);
                intentScan.putExtra("userPass", userPass);
                startActivity(intentScan);
                break;

            case R.id.root_background:

                btManual.setVisibility(View.INVISIBLE);
                btOCR.setVisibility(View.INVISIBLE);
        }
        if (v.getId() != R.id.ibAdd) {
            btManual.setVisibility(View.INVISIBLE);
            btOCR.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, InvoiceDetail.class);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("userPass", userPass);
        intent.putExtra("groupModel", groupModel);
        intent.putExtra("invoice", arrIM.get(i));
        startActivity(intent);
    }

    public void applyPermission(){
        if(this.groupModel.getRole()>=2) {
   ibAdd.setVisibility(View.INVISIBLE);

        }
    }
}