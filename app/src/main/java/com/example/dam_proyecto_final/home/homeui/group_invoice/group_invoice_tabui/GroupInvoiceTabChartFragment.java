package com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupInvoiceTabChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupInvoiceTabChartFragment extends Fragment implements View.OnClickListener {

    private TabLayout tabLayout;

    private BarChart barChart;
    private LineChart lineChart;
    private PieChart pieChart;

    private RadioButton rbChartCircle;
    private RadioButton rbChartBar;
    private RadioButton rbChartLine;



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

        //Instanciamos Vistas
        rbChartCircle = view.findViewById(R.id.rb_gitc_chartpie);
        rbChartCircle.setOnClickListener(this);
        rbChartBar = view.findViewById(R.id.rb_gitc_chartbar);
        rbChartBar.setOnClickListener(this);
        rbChartLine = view.findViewById(R.id.rb_gitc_chartline);
        rbChartLine.setOnClickListener(this);

        //Chart
        barChart = view.findViewById(R.id.chart_gitcf_barchart);
        lineChart = view.findViewById(R.id.chart_gitcf_linechart);
        pieChart = view.findViewById(R.id.chart_gitcf_piechart);

        // Inicializamos valores
        groupInvoicesMaxValue = -1;

        //Ordenamos invoices
        Collections.sort(invoices, new Comparator<InvoiceModel>() {
            @Override
            public int compare(InvoiceModel i1, InvoiceModel i2) {
                return new String(i1.getDate()).compareTo(new String(i2.getDate()));
            }
        });

        //Obtenemos el tipo de valor a representar
        changeChart();


        return view;
    }

    private void changeChart() {
        if (rbChartBar.isChecked()){
            barChart();
        } else if (rbChartLine.isChecked()) {
            lineChart();
        } else if(rbChartCircle.isChecked()){
            pieChart();
        } else {
            rbChartBar.setChecked(true);
            barChart();
        }
    }


    private void barChart() {

        barChart.setVisibility(View.VISIBLE);
        lineChart.setVisibility(View.INVISIBLE);
        pieChart.setVisibility(View.INVISIBLE);

        //Definición de datos y parametros
        BarData data = new BarData();

        //Obtenemos los grupos de las facturas obtenidas
        ArrayList<String> types = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            if (!types.contains(i.getType())) {
                types.add(i.getType());
            }
        }


        //Solicitamos un DataSet por cada tipo de factura
        for (String type: types){
            data.addDataSet(getBarDataSet(type));
        }

        barChart.setData(data);

        // TODO ver manera de asignar tan manualmente los meses mediante bucle y de añadir el año correspondiente
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
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false); // no axis line
        xAxis.setDrawGridLines(false); // no grid lines
        xAxis.setTextSize(16);

        //Barra Y
        barChart.getAxisRight().setEnabled(false);


        // TODO Controlar el tipo de gráfico para pintar uno u otro

        if (types.size() > 1){
            // Calculamos los tamaños de los campos
            float groupSpace = 0.06f;
            float barSpace = 0.04f / types.size(); //x2
            float barWidth = 0.90f / types.size(); //x2
            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
            data.setBarWidth(barWidth);
            barChart.getXAxis().setAxisMinimum(0);
            barChart.getXAxis().setAxisMaximum(0+ barChart.getBarData().getGroupWidth(groupSpace, barSpace)*groupInvoicesMaxValue);
            barChart.getAxisLeft().setAxisMinimum(0);
            barChart.groupBars(0, groupSpace, barSpace);
            barChart.setDragEnabled(true);

            xAxis.setCenterAxisLabels(true);
        }

        // Opciones de la leyenda de tipo de factura
        barChart.getLegend().setTextSize(18);
        barChart.getLegend().setXEntrySpace(24);
        barChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        barChart.getLegend().setWordWrapEnabled(true);

        //Opciones de gráfico
        barChart.setVisibleXRangeMaximum(3);
        barChart.setExtraBottomOffset(12);
        lineChart.setNoDataText("No hay datos que representar");

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        barChart.animateXY(1000, 1000);
        barChart.invalidate();
    }

    private void lineChart() {

        barChart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.INVISIBLE);

        //Definición de datos y parametros
        LineData data = new LineData();

        //Obtenemos los grupos de las facturas obtenidas
        ArrayList<String> types = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            if (!types.contains(i.getType())) {
                types.add(i.getType());
            }
        }


        //Solicitamos un DataSet por cada tipo de factura
        for (String type: types){
            data.addDataSet(getLineDataSet(type));
        }

        lineChart.setData(data);

        // TODO ver manera de asignar tan manualmente los meses mediante bucle y de añadir el año correspondiente
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
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false); // no axis line
        xAxis.setDrawGridLines(false); // no grid lines
        xAxis.setTextSize(16);

        //Barra Y
        lineChart.getAxisRight().setEnabled(false);


        // TODO Controlar el tipo de gráfico para pintar uno u otro

//        if (types.size() > 1){
//            // Calculamos los tamaños de los campos
//            float groupSpace = 0.06f;
//            float barSpace = 0.04f / types.size(); //x2
//            float barWidth = 0.90f / types.size(); //x2
//            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
//            data.setBarWidth(barWidth);
//            lineChart.getXAxis().setAxisMinimum(0);
//            lineChart.getXAxis().setAxisMaximum(0+ barChart.getBarData().getGroupWidth(groupSpace, barSpace)*groupInvoicesMaxValue);
//            lineChart.getAxisLeft().setAxisMinimum(0);
//            lineChart.groupBars(0, groupSpace, barSpace);
//            lineChart.setDragEnabled(true);
//
//            xAxis.setCenterAxisLabels(true);
//        }

        // Opciones de la leyenda de tipo de factura
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.getLegend().setTextSize(18);
        lineChart.getLegend().setXEntrySpace(24);
        lineChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);


        //Opciones de gráfico
        lineChart.setVisibleXRangeMaximum(3);
        lineChart.setExtraBottomOffset(12);
        lineChart.setNoDataText("No hay datos que representar");

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        lineChart.animateXY(1000, 1000);
        lineChart.invalidate();
    }

    private void pieChart() {

        barChart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.INVISIBLE);
        pieChart.setVisibility(View.VISIBLE);

        //Definición de datos y parametros
        PieData data = new PieData();

        //Obtenemos los grupos de las facturas obtenidas
        ArrayList<String> types = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            if (!types.contains(i.getType())) {
                types.add(i.getType());
            }
        }


        //Solicitamos un DataSet por cada tipo de factura
        data.addDataSet(getPieDataSet(types));

        pieChart.setData(data);

        // TODO ver manera de asignar tan manualmente los meses mediante bucle y de añadir el año correspondiente
        // Definimos los campos X


        // Barra X
//        XAxis xAxis = pieChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawAxisLine(false); // no axis line
//        xAxis.setDrawGridLines(false); // no grid lines
//        xAxis.setTextSize(16);

        //Barra Y
//        pieChart.getAxisRight().setEnabled(false);


        // TODO Controlar el tipo de gráfico para pintar uno u otro

//        if (types.size() > 1){
//            // Calculamos los tamaños de los campos
//            float groupSpace = 0.06f;
//            float barSpace = 0.04f / types.size(); //x2
//            float barWidth = 0.90f / types.size(); //x2
//            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
//            data.setBarWidth(barWidth);
//            lineChart.getXAxis().setAxisMinimum(0);
//            lineChart.getXAxis().setAxisMaximum(0+ barChart.getBarData().getGroupWidth(groupSpace, barSpace)*groupInvoicesMaxValue);
//            lineChart.getAxisLeft().setAxisMinimum(0);
//            lineChart.groupBars(0, groupSpace, barSpace);
//            lineChart.setDragEnabled(true);
//
//            xAxis.setCenterAxisLabels(true);
//        }

        // Opciones de la leyenda de tipo de factura
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setTextSize(18);
        pieChart.getLegend().setXEntrySpace(24);
        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);


        //Opciones de gráfico
        pieChart.setExtraBottomOffset(12);
        pieChart.setNoDataText("No hay datos que representar");

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }


    //Datos para las barras
    private BarDataSet getBarDataSet(String tipo) {

        ArrayList dataSets = null;

        ArrayList<BarEntry> valueSet = new ArrayList<BarEntry>();
        String[] dateEmitionStr = invoices.get(0).getDate().split("-");
        int firstMonth = Integer.parseInt(dateEmitionStr[1]);
        int c = firstMonth-1;
        if (typeChart == R.id.rb_GIFConsumption || typeChart == -1){
            for (InvoiceModel i : invoices) {
                if (tipo.equals(i.getType())){
                    valueSet.add(new BarEntry(c, (float) i.getConsumption()));
                    c++;
                }
            }
        } else if (typeChart == R.id.rb_GIFCost){
            for (InvoiceModel i : invoices) {
                if (tipo.equals(i.getType())){
                    valueSet.add(new BarEntry(c, (float) i.getAmount()));
                    c++;
                }
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

    private LineDataSet getLineDataSet(String tipo) {

        ArrayList dataSets = null;

        ArrayList<Entry> valueSet = new ArrayList<Entry>();
        String[] dateEmitionStr = invoices.get(0).getDate().split("-");
        int firstMonth = Integer.parseInt(dateEmitionStr[1]);
        int c = firstMonth-1;
        if (typeChart == R.id.rb_GIFConsumption || typeChart == -1){
            for (InvoiceModel i : invoices) {
                if (tipo.equals(i.getType())){
                    valueSet.add(new Entry(c, (float) i.getConsumption()));
                    c++;
                }
            }
        } else if (typeChart == R.id.rb_GIFCost){
            for (InvoiceModel i : invoices) {
                if (tipo.equals(i.getType())){
                    valueSet.add(new Entry(c, (float) i.getAmount()));
                    c++;
                }
            }
        }

        if (groupInvoicesMaxValue == -1 || groupInvoicesMaxValue < valueSet.size()){
            groupInvoicesMaxValue = valueSet.size();
        }


        LineDataSet lineDataSet = new LineDataSet(valueSet, tipo);
        Random r = new Random();
        lineDataSet.setColor(Color.rgb(r.nextFloat(), r.nextFloat(), r.nextFloat()));
        lineDataSet.setLineWidth(4);
        lineDataSet.setValueTextSize(10);

        return lineDataSet;
    }

    private PieDataSet getPieDataSet(ArrayList<String> types) {

        ArrayList dataSets = null;

        ArrayList<PieEntry> valueSet = new ArrayList<PieEntry>();
        String[] dateEmitionStr = invoices.get(0).getDate().split("-");
        int firstMonth = Integer.parseInt(dateEmitionStr[1]);
        int c = firstMonth - 1;

        float typeSum = 0; //Valor de suma de tipo
        float sum = 0; // Valor de suma de todos los tipos unidos
        String cad = "";
        // Obtenemos la lista de tipos con su suma de valores
        HashMap<String, Float> values = new HashMap<String, Float>();
        if (typeChart == R.id.rb_GIFConsumption || typeChart == -1) {
            cad = "Consumo";
            for (int t = 0; t < types.size(); t++) {
                String type = types.get(t);
                for (InvoiceModel i : invoices) {
                    if (type.equals(i.getType())) {
                        typeSum += i.getConsumption();
                        c++;
                    }
                }
                values.put(type, typeSum);
                typeSum = 0;
            }
        } else if (typeChart == R.id.rb_GIFCost) {
            cad = "Coste";
            if (typeChart == R.id.rb_GIFConsumption || typeChart == -1) {
                for (int t = 0; t < types.size(); t++) {
                    String type = types.get(t);
                    for (InvoiceModel i : invoices) {
                        if (type.equals(i.getType())) {
                            typeSum += i.getAmount();
                            c++;
                        }
                    }
                    values.put(type, typeSum);
                    sum += typeSum;
                    typeSum = 0;
                }
            }
        }

            // Calculamos a partir de la suma total de los tipo y la suma individual de cada tipo su procentaje
            HashMap<String, Float> percentValues = new HashMap<String, Float>();
            for (Map.Entry<String, Float> entry : values.entrySet()) {
                float percent = (entry.getValue() * 100) / sum;
                percentValues.put(entry.getKey(), percent);
            }

            // Asignamos a la lista de pieEntry los valores en porcentaje y su tipo y creamos una lista de colores
            ArrayList<Integer> colors = new ArrayList<Integer>();
            Random r = new Random();
            for (Map.Entry<String, Float> entry : values.entrySet()) {
                valueSet.add(new PieEntry(entry.getValue(), entry.getKey()));
                colors.add(Color.rgb(r.nextFloat(), r.nextFloat(), r.nextFloat()));
            }


            if (groupInvoicesMaxValue == -1 || groupInvoicesMaxValue < valueSet.size()) {
                groupInvoicesMaxValue = valueSet.size();
            }


            PieDataSet pieDataSet = new PieDataSet(valueSet, cad);


//            pieDataSet.setColor(Color.rgb(r.nextFloat(), r.nextFloat(), r.nextFloat()));
            pieDataSet.setColors(colors);


            pieDataSet.setValueTextSize(10);

            return pieDataSet;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rb_gitc_chartbar ||
                view.getId() == R.id.rb_gitc_chartline ||
                view.getId() == R.id.rb_gitc_chartpie){

            changeChart();
        }
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