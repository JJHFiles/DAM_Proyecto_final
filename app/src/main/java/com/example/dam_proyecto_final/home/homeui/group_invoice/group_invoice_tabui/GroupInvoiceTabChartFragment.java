package com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupInvoiceTabChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupInvoiceTabChartFragment extends Fragment {

    private TabLayout tabLayout;


    private BarChart chart_AGITAmount;


    private static final String INVOICES = "invoices";
    private static final String TYPECHART = "typeChart";

    /*    // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;*/
    ArrayList<InvoiceModel> invoices;
    private int typeChart;


    public GroupInvoiceTabChartFragment() {
        // Required empty public constructor
    }


    public static GroupInvoiceTabChartFragment newInstance(ArrayList<InvoiceModel> invoices, int typeChart) {
        GroupInvoiceTabChartFragment fragment = new GroupInvoiceTabChartFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(INVOICES, invoices);
        args.putInt(TYPECHART, typeChart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            invoices = getArguments().getParcelableArrayList(INVOICES);
            typeChart = getArguments().getInt(TYPECHART);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_invoice_tab_chart, container, false);

        //TabLayout, seleccionamos la tab correspondiente por si venimos de hacer back
        tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.getTabAt(1).select();


        //Ordenamos invoices
//        ArrayList<InvoiceModel> invoicesSorted;
        if (typeChart == R.id.rb_GIFConsumption || typeChart == -1) {
            Collections.sort(invoices, new Comparator<InvoiceModel>() {
                @Override
                public int compare(InvoiceModel i1, InvoiceModel i2) {
                    return new String(i1.getDate()).compareTo(new String(i2.getDate()));
                }
            });
        }

//Obtenemos los grupos de las facturas obtenidas
        ArrayList<String> types = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            if (!types.contains(i.getType())) {
                types.add(i.getType());
            }
        }

        //BarChart
        chart_AGITAmount = view.findViewById(R.id.chart_AGITAmount);

        //Definición de datos y parametros
        BarData data = new BarData();
        data.addDataSet(getDataSet("Agua"));
        data.addDataSet(getDataSet("Electricidad"));
        chart_AGITAmount.setData(data);
        Description description = new Description();
        description.setText("Mi gráfico");
        chart_AGITAmount.setDescription(description);
        chart_AGITAmount.setFitBars(true);
        chart_AGITAmount.animateXY(1000, 1000);
        chart_AGITAmount.invalidate();
        return view;
    }


    //Datos para las barras
    private BarDataSet getDataSet(String tipo) {
        ArrayList dataSets = null;


        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
        int c = 0;
        for (InvoiceModel i : invoices) {
            if (tipo.equals(i.getType())){
                String[] dateEmitionStr = i.getDate().split("-");
                valueSet1.add(new BarEntry(c, (float) i.getConsumption()));
                c++;
            }
        }


/*
        valueSet1.add(new BarEntry(0f, 30));
        valueSet1.add(new BarEntry(1f, 80));
        valueSet1.add(new BarEntry(2f, 60));
        valueSet1.add(new BarEntry(3f, 50));
        // gap of 2f
        valueSet1.add(new BarEntry(5f, 70));
        valueSet1.add(new BarEntry(6f, 60));
*/

//        ArrayList valueSet2 = new ArrayList();
//        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
//        valueSet2.add(v2e1);
//        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
//        valueSet2.add(v2e2);
//        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
//        valueSet2.add(v2e3);
//        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
//        valueSet2.add(v2e4);
//        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
//        valueSet2.add(v2e5);
//        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
//        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, tipo);
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
//        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

//        dataSets = new ArrayList();
//        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);
        return barDataSet1;
    }

    // En teoría datos para establecer los campos de coordenadas X (a priori no funciona)
//    private IBarDataSet getXAxisValues() {
//        ArrayList xAxis = new ArrayList();
//        xAxis.add("JAN");
//        xAxis.add("FEB");
//        xAxis.add("MAR");
//        xAxis.add("APR");
//        xAxis.add("MAY");
//        xAxis.add("JUN");
//        return new IBarDataSet(xAxis);
//    }

}