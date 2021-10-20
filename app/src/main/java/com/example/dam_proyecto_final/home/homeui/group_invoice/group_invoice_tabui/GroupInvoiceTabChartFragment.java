package com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

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

    ArrayList<InvoiceModel> invoices;
    private int typeChart;
    private int groupInvoicesMaxValue;

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


        // Inicializamos valores
        groupInvoicesMaxValue = -1;

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



        //BarChart
        chart_AGITAmount = view.findViewById(R.id.chart_AGITAmount);

        //Definición de datos y parametros
        BarData data = new BarData();
            //Obtenemos los grupos de las facturas obtenidas
        ArrayList<String> types = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            if (!types.contains(i.getType())) {
                types.add(i.getType());
            }
        }
        //Solicitamos un DataSet por cada tipo de facturas
        for (String type: types){
            data.addDataSet(getDataSet(type));
        }

        chart_AGITAmount.setData(data);

        // Definimos los campos X
        String[] months = new String[]{
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Noviembre", "Diciembre",
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Noviembre", "Diciembre",
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Noviembre", "Diciembre",
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Noviembre", "Diciembre",
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Noviembre", "Diciembre",
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Noviembre", "Diciembre",
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Noviembre", "Diciembre"};

        // Barra X
        XAxis xAxis = chart_AGITAmount.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false); // no axis line
        xAxis.setDrawGridLines(false); // no grid lines
        xAxis.setTextSize(16);

        //Barra Y
        chart_AGITAmount.getAxisRight().setEnabled(false);


        // TODO Controlar el tipo de gráfico para pintar uno u otro

        if (types.size() > 1){
            // Calculamos los tamaños de los campos
            float groupSpace = 0.06f;
            float barSpace = 0.04f / types.size(); //x2
            float barWidth = 0.90f / types.size(); //x2
            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
            data.setBarWidth(barWidth);
            chart_AGITAmount.getXAxis().setAxisMinimum(0);
            chart_AGITAmount.getXAxis().setAxisMaximum(0+chart_AGITAmount.getBarData().getGroupWidth(groupSpace, barSpace)*groupInvoicesMaxValue);
            chart_AGITAmount.getAxisLeft().setAxisMinimum(0);
            chart_AGITAmount.groupBars(0, groupSpace, barSpace);
            chart_AGITAmount.setDragEnabled(true);

            xAxis.setCenterAxisLabels(true);
        }

        // Opciones de la leyenda de tipo de factura
        chart_AGITAmount.getLegend().setTextSize(18);
        chart_AGITAmount.getLegend().setXEntrySpace(24);
        chart_AGITAmount.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        chart_AGITAmount.getLegend().setWordWrapEnabled(true);

        //Opciones de gráfico
        chart_AGITAmount.setVisibleXRangeMaximum(3);
        chart_AGITAmount.setExtraBottomOffset(12);

        Description description = new Description();
        description.setText("");
        chart_AGITAmount.setDescription(description);

        chart_AGITAmount.animateXY(1000, 1000);
        chart_AGITAmount.invalidate();

        return view;
    }


    //Datos para las barras
    private BarDataSet getDataSet(String tipo) {
        ArrayList dataSets = null;


        ArrayList<BarEntry> valueSet = new ArrayList<BarEntry>();
        String[] dateEmitionStr = invoices.get(0).getDate().split("-");
        int firstMonth = Integer.parseInt(dateEmitionStr[1]);
        int c = firstMonth-1;
        for (InvoiceModel i : invoices) {
            if (tipo.equals(i.getType())){
                valueSet.add(new BarEntry(c, (float) i.getConsumption()));
                c++;
            }
        }

        if (groupInvoicesMaxValue == -1 || groupInvoicesMaxValue < valueSet.size()){
            groupInvoicesMaxValue = valueSet.size();
        }


        BarDataSet barDataSet1 = new BarDataSet(valueSet, tipo);
        Random r = new Random();
        barDataSet1.setColor(Color.rgb(r.nextFloat(), r.nextFloat(), r.nextFloat()));

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