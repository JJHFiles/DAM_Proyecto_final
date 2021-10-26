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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupInvoiceTabChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupInvoiceTabChartFragment extends Fragment implements View.OnClickListener {

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


    // TODO realizar la llamada al fragment mediante newInstance y no con Bundle/Constructor
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
        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);
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

        //Ordenamos invoices por fecha
        invoices.sort(new Comparator<InvoiceModel>() {
            @Override
            public int compare(InvoiceModel i1, InvoiceModel i2) {
                return new String(i1.getDate()).compareTo(new String(i2.getDate()));
            }
        });

        //Obtenemos el tipo de valor a representar
        changeChart();


        return view;
    }

    //Método para cambiar el tipo de gráfico a mostrar
    private void changeChart() {
        barChart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.INVISIBLE);
        pieChart.setVisibility(View.INVISIBLE);

        if (rbChartBar.isChecked()){
            barChart.setVisibility(View.VISIBLE);
            barChart();
        } else if (rbChartLine.isChecked()) {
            lineChart.setVisibility(View.VISIBLE);
            lineChart();
        } else if(rbChartCircle.isChecked()){
            pieChart.setVisibility(View.VISIBLE);
            pieChart();
        } else {
            rbChartBar.setChecked(true);
            barChart.setVisibility(View.VISIBLE);
            barChart();
        }
    }


    // Método para mostrar gráfico de barras
    private void barChart() {

        //Obtenemos los grupos de las facturas obtenidas
        ArrayList<String> types = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            if (!types.contains(i.getType())) {
                types.add(i.getType());
            }
        }

        //Definición de datos y parametros
        BarData data = new BarData();

        //Solicitamos un DataSet por cada tipo de factura y le asignamos la coleccion de datos al gráfico
        for (String type: types){
            data.addDataSet(getBarDataSet(type));
        }

        barChart.setData(data);

        // Obtenemos los campos para el eje X
        ArrayList<String>itemsXAsix = collectionXAsis(invoices);

        // Barra X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(itemsXAsix));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false); // no axis line
        xAxis.setDrawGridLines(false); // no grid lines
        xAxis.setTextSize(16);
        xAxis.setGranularityEnabled(true);

        //Barra Y
        barChart.getAxisRight().setEnabled(false);

        // Aplicamos los tamaños en funcion de los tipos obtenidos
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
        barChart.setExtraOffsets(0f,16f,0f,0f);

        //Opciones de gráfico
        barChart.setVisibleXRangeMaximum(3);
        barChart.setExtraBottomOffset(12);
        barChart.setNoDataText("No hay datos que representar");

        //Descripción
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        // Establecemos animación y refrescamos
        barChart.animateXY(1000, 1000);
        barChart.invalidate();
    }


    // Método para mostrar gráfico
    private void lineChart() {


        //Obtenemos los grupos de las facturas obtenidas
        ArrayList<String> types = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            if (!types.contains(i.getType())) {
                types.add(i.getType());
            }
        }

        // Obtenemos los campos para el eje X
        ArrayList<String>itemsXAsix = collectionXAsis(invoices);

        // Barra X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(itemsXAsix));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false); // no axis line
        xAxis.setDrawGridLines(false); // no grid lines
        xAxis.setTextSize(16);
        xAxis.setGranularityEnabled(true);

        //Barra Y
        lineChart.getAxisRight().setEnabled(false);

        // Opciones de la leyenda de tipo de factura
        lineChart.getLegend().setTextSize(18);
        lineChart.getLegend().setXEntrySpace(24);
        lineChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.setExtraOffsets(0f,16f,0f,0f);

        //Opciones de gráfico
        lineChart.setVisibleXRangeMaximum(3);
        lineChart.setExtraBottomOffset(12);
        lineChart.setNoDataText("No hay datos que representar");

        //Descripción
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        //Solicitamos un DataSet por cada tipo de factura y le asignamos la coleccion de datos al gráfico
        LineData data = new LineData();
        for (String type: types){
            data.addDataSet(getLineDataSet(type));
        }
        lineChart.setData(data);
        lineChart.setData(data); //Duplicada intencioandamente para cargar correctamente la legenda en wrap

        // Establecemos animación y refrescamos
        lineChart.animateXY(1000, 1000);
        lineChart.invalidate();
    }

    private void pieChart() {


        //Obtenemos los grupos de las facturas obtenidas
        ArrayList<String> types = new ArrayList<>();
        for (InvoiceModel i : invoices) {
            if (!types.contains(i.getType())) {
                types.add(i.getType());
            }
        }

        // Opciones de la leyenda de tipo de factura
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setTextSize(18);
        pieChart.getLegend().setXEntrySpace(24);
        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pieChart.setExtraOffsets(18f,0f,18f,0f);

        //Opciones de gráfico
        pieChart.setExtraBottomOffset(12);
        pieChart.setNoDataText("No hay datos que representar");

        //Descripción
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        //Definición de datos y DataSet por cada tipo de factura
        PieData data = new PieData();
        data.addDataSet(getPieDataSet(types));
        pieChart.setData(data);
        pieChart.setData(data);

        // Establecemos animación y refrescamos
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }

    //Método para obtener los elementos de la barra X
    private ArrayList<String> collectionXAsis(ArrayList<InvoiceModel> invoices) {

        //Creamos dos calendarios con la fecha mínima de las facturas
        Calendar firstCal = Calendar.getInstance();
        Calendar lastCal = Calendar.getInstance();

        //Establecemos el valor a los calendarios a partir de las facturas
        String[] dateSplit;
        dateSplit = invoices.get(0).getDate().split("-");
        firstCal.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]));
        dateSplit = invoices.get(invoices.size()-1).getDate().split("-");
        lastCal.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]));

        //Cogemos el valor númerico para calcular la diferencia de meses
        int nMonth1=12*firstCal.get(Calendar.YEAR)+firstCal.get(Calendar.MONTH);
        int nMonth2=12*lastCal.get(Calendar.YEAR)+lastCal.get(Calendar.MONTH);

        int numMonths = (nMonth2 - nMonth1) + 1; //Diferencia de meses más 1

        //Cogemos a partir de la fecha mínima la cadena Año mes para establecerla como eje X y sumandole 1 mes por iteración
        SimpleDateFormat formater = new SimpleDateFormat("yyyy MMM", Locale.getDefault());
        ArrayList<String> itemsXAsis = new ArrayList<String>();
        for (int m = 0; m<numMonths; m++){
            itemsXAsis.add(formater.format(firstCal.getTime()));
            firstCal.add(Calendar.MONTH, 1);
        }

        return itemsXAsis;
    }


    //Datos para las barras
    private BarDataSet getBarDataSet(String tipo) {

        ArrayList dataSets = null;

        // Creamos la lista y le incorporamos el dato requerido del tipo requerido
        ArrayList<BarEntry> valueSet = new ArrayList<BarEntry>();
        String[] dateEmitionStr = invoices.get(0).getDate().split("-");
        int firstMonth = Integer.parseInt(dateEmitionStr[1]);
        int c = 0;
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

        // Establecemos el valor del grupo con más facturas (nos permite calcular tamaño del gráfico)
        if (groupInvoicesMaxValue == -1 || groupInvoicesMaxValue < valueSet.size()){
            groupInvoicesMaxValue = valueSet.size();
        }

        //Creamos el elemento de datos para la gráfica y establecemos colores
        BarDataSet barDataSet = new BarDataSet(valueSet, tipo);
        Random r = new Random();
        barDataSet.setColor(Color.rgb(r.nextFloat(), r.nextFloat(), r.nextFloat()));

        return barDataSet;
    }

    private LineDataSet getLineDataSet(String tipo) {

        ArrayList dataSets = null;

        // Creamos la lista y le incorporamos el dato requerido del tipo requerido
        ArrayList<Entry> valueSet = new ArrayList<Entry>();
        String[] dateEmitionStr = invoices.get(0).getDate().split("-");
        int firstMonth = Integer.parseInt(dateEmitionStr[1]);
        int c = 0;
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

        // Establecemos el valor del grupo con más facturas (nos permite calcular tamaño del gráfico)
        if (groupInvoicesMaxValue == -1 || groupInvoicesMaxValue < valueSet.size()){
            groupInvoicesMaxValue = valueSet.size();
        }

        //Creamos el elemento de datos para la gráfica, establecemos colores y tamaños
        LineDataSet lineDataSet = new LineDataSet(valueSet, tipo);
        Random r = new Random();
        lineDataSet.setColor(Color.rgb(r.nextFloat(), r.nextFloat(), r.nextFloat()));
        lineDataSet.setLineWidth(4);
        lineDataSet.setValueTextSize(10);

        return lineDataSet;
    }

    private PieDataSet getPieDataSet(ArrayList<String> types) {

        ArrayList dataSets = null;

        // Creamos la lista y le incorporamos el dato requerido del tipo requerido
        ArrayList<PieEntry> valueSet = new ArrayList<PieEntry>();

        float typeSum = 0; //Valor de suma de tipo
        float sum = 0; // Valor de suma de todos los tipos unidos
        String cad = ""; // Valor del tipo

        // Obtenemos la lista de tipos con su suma de valores
        HashMap<String, Float> values = new HashMap<String, Float>();
        if (typeChart == R.id.rb_GIFConsumption || typeChart == -1) {
            cad = "Consumo";
            for (int t = 0; t < types.size(); t++) {
                String type = types.get(t);
                for (InvoiceModel i : invoices) {
                    if (type.equals(i.getType())) {
                        typeSum += i.getConsumption();
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
                        }
                    }

                    // Inicializamos typeSum por cada tipo y sumamos elemento al total
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

            // Establecemos el valor del grupo con más facturas (nos permite calcular tamaño del gráfico)
            if (groupInvoicesMaxValue == -1 || groupInvoicesMaxValue < valueSet.size()) {
                groupInvoicesMaxValue = valueSet.size();
            }

            //Creamos el elemento de datos para la gráfica, establecemos colores y tamaños
            PieDataSet pieDataSet = new PieDataSet(valueSet, cad);
            pieDataSet.setColors(colors);
            pieDataSet.setValueTextSize(10);

            return pieDataSet;
    }

    // Método de click para cambiar de tipo de gráfica
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rb_gitc_chartbar ||
                view.getId() == R.id.rb_gitc_chartline ||
                view.getId() == R.id.rb_gitc_chartpie){

            changeChart();
        }
    }

}