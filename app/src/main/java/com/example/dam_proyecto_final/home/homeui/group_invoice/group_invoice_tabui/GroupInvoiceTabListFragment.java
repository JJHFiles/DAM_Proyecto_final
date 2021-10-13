package com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.home.homeui.group_invoice.GroupInvoiceAdd;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.model.JsonResponseModel;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupInvoiceTabListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupInvoiceTabListFragment extends Fragment implements View.OnClickListener {

    private String userEmail, idGroup, groupName, currency, role;
    private WebApiRequest webApiRequest;
    private ListView lv_invoice;
    private ConstraintLayout root_background;
    private TabLayout tabLayout;
    private ImageView iv;
    private ImageButton ibAdd;
    private Context context;
    private ExtendedFloatingActionButton btOCR, btManual;
    private ArrayList<InvoiceModel> arrIM;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupInvoiceTabListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupInvoiceTabListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupInvoiceTabListFragment newInstance(String param1, String param2) {
        GroupInvoiceTabListFragment fragment = new GroupInvoiceTabListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        iv = view.findViewById(R.id.iv);

        root_background=view.findViewById(R.id.root_background);
        root_background.setOnClickListener(this);

        //TabLayout, seleccionamos la tab correspondiente por si venimos de hacer back
        tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.getTabAt(0).select();


        if (getArguments() != null) {
            ArrayList<InvoiceModel> arrIM = getArguments().getParcelableArrayList("invoices");
            currency = getArguments().getString("currency");
            fillListViewCustomAdapter(arrIM);
        }


        return view;
    }




    // recibe las facturas en ese grupo seleccionado
    public void getInvoiceByGroup(String idGroup) {
        Log.d("DEBUGME", "GroupInvoiceTabListFragment");
        webApiRequest = new WebApiRequest(context);

    }

/*    private void fillListViewSimpleAdapter(ArrayList<InvoiceModel> al) {

        ArrayList<String> arr = new ArrayList<>();
        for (int x = 0; x < al.size(); x++) {
            arr.add(""
                    + "#" + (x + 1)
                    + "\nTipo: " + al.get(x).getType()
                    + "\nDate: " + al.get(x).getDate()
                    + "\nInicio de facturación: " + al.get(x).getStart_period()
                    + "\nFin de facturación: " + al.get(x).getEnd_period()
                    + "\nConsumo: " + al.get(x).getConsumption()
                    + "\nMonto: " + al.get(x).getAmount());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arr);
        lv_invoice.setAdapter(arrayAdapter);

        AdapterView.OnItemClickListener lvClick = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(), "Seleccionado elemento: "+position, Toast.LENGTH_LONG).show();
            }
        };
        lv_invoice.setOnItemClickListener(lvClick);

    }*/

    private void fillListViewCustomAdapter(ArrayList<InvoiceModel> arrIM) {
        ArrayList<InvoiceListModel> ilm = new ArrayList<InvoiceListModel>();
        for (int x = 0; x < arrIM.size(); x++) {
            String measure = "";

            //Obtenemos measure
            if (arrIM.get(x).getType().equals("Electricidad")) {
                measure = " KW";
            } else if (arrIM.get(x).getType().equals("Gas")) {
                measure = " KW";
            } else if (arrIM.get(x).getType().equals("Agua")) {
                measure = " M3";
            } else if (arrIM.get(x).getType().equals("Telefonia")) {
                measure = " Mes";
            } else if (arrIM.get(x).getType().equals("Renting Coche")) {
                measure = " Mes";
            } else if (arrIM.get(x).getType().equals("IBI")) {
                measure = " Mes";
            }


            ilm.add(new InvoiceListModel());
            ilm.get(x).setType("Tipo: " + arrIM.get(x).getType());
            ilm.get(x).setAmount("Gasto: " + arrIM.get(x).getAmount() + currency);
            ilm.get(x).setDate("Fecha: " + arrIM.get(x).getDate());
            ilm.get(x).setConsumption("Consumo: " + arrIM.get(x).getConsumption() + measure);
            ilm.get(x).setCode(x);
        }

        InvoiceListAdapter ila = new InvoiceListAdapter(context, getActivity(), ilm);
        lv_invoice.setAdapter(ila);
    }

    /*
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "TAB 1"+tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();

            if (tabLayout.getSelectedTabPosition()== 0) {
                Toast.makeText(getApplicationContext(), "TAB 1", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "TAB 2", Toast.LENGTH_LONG).show();

            }
        }

     */
    @Override
    public void onClick(View v) {
        int choice = v.getId();
        switch (v.getId()) {
            case R.id.ibAdd:

                //FAB Principal
                if(btManual.getVisibility() == View.INVISIBLE) {
                    btManual.setVisibility(View.VISIBLE);
                    btOCR.setVisibility(View.VISIBLE);
                }else{
                    btManual.setVisibility(View.INVISIBLE);
                    btOCR.setVisibility(View.INVISIBLE);
                }
                // Para verse las sombras de los botones, provoca un back negro, cambiar el método back
                //  getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //  Toast.makeText(getApplicationContext(), "idGroup "+idGroup, Toast.LENGTH_LONG).show();

 /*            TODO: que la actividad se torne en escala grises
               new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setPositiveButton("Añadir de forma manual",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
*/

                break;

            case R.id.btManual:
                // Abre activity para añadir nuevas facturas manuales
                Intent intent = new Intent(context, GroupInvoiceAdd.class);
                intent.putExtra("idGroup", idGroup);
                intent.putExtra("groupName", groupName);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
                break;

            case R.id.btOCR:
                //TODO: lectura factura por OCR
                break;

            case R.id.root_background:

                    btManual.setVisibility(View.INVISIBLE);
                    btOCR.setVisibility(View.INVISIBLE);

        }
    }

}