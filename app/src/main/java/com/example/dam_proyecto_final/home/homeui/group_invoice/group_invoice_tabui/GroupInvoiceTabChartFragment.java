package com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dam_proyecto_final.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupInvoiceTabChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupInvoiceTabChartFragment extends Fragment {

    private TabLayout tabLayout;

    //Grafic panel
    private BarChart chart_AGITAmount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupInvoiceTabChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupInvoiceTabChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupInvoiceTabChartFragment newInstance(String param1, String param2) {
        GroupInvoiceTabChartFragment fragment = new GroupInvoiceTabChartFragment();
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
        View view = inflater.inflate(R.layout.fragment_group_invoice_tab_chart, container, false);

        //TabLayout, seleccionamos la tab correspondiente por si venimos de hacer back
        tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.getTabAt(1).select();


        //BarChart
        chart_AGITAmount = view.findViewById(R.id.chart_AGITAmount);

        //Definición de datos y parametros
        BarData data = new BarData(getDataSet());
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
    private BarDataSet getDataSet() {
        ArrayList dataSets = null;

        ArrayList valueSet1 = new ArrayList();
        valueSet1.add(new BarEntry(0f, 30));
        valueSet1.add(new BarEntry(1f, 80));
        valueSet1.add(new BarEntry(2f, 60));
        valueSet1.add(new BarEntry(3f, 50));
        // gap of 2f
        valueSet1.add(new BarEntry(5f, 70));
        valueSet1.add(new BarEntry(6f, 60));

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

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Electricidad");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
//        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList();
        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);
        return new BarDataSet(valueSet1, "BarDataSet");
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