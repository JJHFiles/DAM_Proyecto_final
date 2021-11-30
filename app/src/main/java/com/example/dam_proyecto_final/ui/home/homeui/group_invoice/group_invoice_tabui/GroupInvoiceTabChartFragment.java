package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

// BUG GroupBars doesn't respect missing data
// BUG https://github.com/PhilJay/MPAndroidChart/issues/3076

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
                return i1.getDate().compareTo(i2.getDate());
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

        if (rbChartBar.isChecked()) {
            barChart.setVisibility(View.VISIBLE);
            barChart();
        } else if (rbChartLine.isChecked()) {
            lineChart.setVisibility(View.VISIBLE);
            lineChart();
        } else if (rbChartCircle.isChecked()) {
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

        // Obtenemos los campos para el eje X
        ArrayList<String> itemsXAsix = collectionXAsis(invoices);

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

        //Solicitamos un DataSet por cada tipo de factura y le asignamos la coleccion de datos al gráfico
        for (String type : types) {
            data.addDataSet(getBarDataSet(type, itemsXAsix));
        }

        data.setValueFormatter(new MyValueFormatter());
        barChart.setData(data);


        // Aplicamos los tamaños en funcion de los tipos obtenidos
        if (types.size() > 1) {
            // Calculamos los tamaños de los campos
            float groupSpace = 0.06f;
            float barSpace = 0.04f / types.size();
            float barWidth = 0.90f / types.size();
            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
            data.setBarWidth(barWidth);
            barChart.getXAxis().setAxisMinimum(0);
            barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * itemsXAsix.size());
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
        barChart.setExtraOffsets(0f, 16f, 0f, 0f);

        //Opciones de gráfico
        barChart.setVisibleXRangeMaximum(3);
        barChart.setExtraBottomOffset(12);
        barChart.setNoDataText(getString(R.string.no_view_data));
        Log.d("DEBUGME",getString(R.string.no_view_data));


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

        // Opciones de la leyenda de tipo de factura
        lineChart.getLegend().setTextSize(18);
        lineChart.getLegend().setXEntrySpace(24);
        lineChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.setExtraOffsets(0f, 16f, 0f, 0f);

        // Obtenemos los campos para el eje X
        ArrayList<String> itemsXAsix = collectionXAsis(invoices);

        //Solicitamos un DataSet por cada tipo de factura y le asignamos la coleccion de datos al gráfico
        LineData data = new LineData();
        for (String type : types) {
            data.addDataSet(getLineDataSet(type, itemsXAsix));
        }
        lineChart.setData(data);
        data.setValueFormatter(new MyValueFormatter());
        lineChart.setData(data); //Duplicada intencioandamente para cargar correctamente la legenda en wrap


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


        //Opciones de gráfico
        lineChart.setVisibleXRangeMaximum(3);
        lineChart.setExtraBottomOffset(12);
        lineChart.setNoDataText(getString(R.string.no_view_data));
        Log.d("DEBUGME",getString(R.string.no_view_data));


        //Descripción
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

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
        pieChart.setExtraOffsets(18f, 0f, 18f, 0f);

        //Opciones de gráfico
        pieChart.setExtraBottomOffset(12);
        pieChart.setNoDataText(getString(R.string.no_view_data));
        Log.d("DEBUGME",getString(R.string.no_view_data));


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
        firstCal.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]));
        dateSplit = invoices.get(invoices.size() - 1).getDate().split("-");
        lastCal.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]));

        //Cogemos el valor númerico para calcular la diferencia de meses
        int nMonth1 = 12 * firstCal.get(Calendar.YEAR) + firstCal.get(Calendar.MONTH);
        int nMonth2 = 12 * lastCal.get(Calendar.YEAR) + lastCal.get(Calendar.MONTH);

        int numMonths = (nMonth2 - nMonth1) + 1; //Diferencia de meses más 1

        //Cogemos a partir de la fecha mínima la cadena Año mes para establecerla como eje X y sumandole 1 mes por iteración
        SimpleDateFormat formater = new SimpleDateFormat("yyyy MMM", Locale.getDefault());
        ArrayList<String> itemsXAsis = new ArrayList<>();
        for (int m = 0; m < numMonths; m++) {
            itemsXAsis.add(formater.format(firstCal.getTime()));
            firstCal.add(Calendar.MONTH, 1);
        }

        return itemsXAsis;
    }

    //Datos para las barras
    private BarDataSet getBarDataSet(String tipo, ArrayList<String> itemsXAsix) {

        // Creamos la lista y le incorporamos el dato requerido del tipo requerido
        ArrayList<BarEntry> valueSet = new ArrayList<>();

        // Valores de control para posición del ejeX
        int posX;
        String[] dateEmitionStr;
        Calendar dateEmition;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy MMM", Locale.getDefault());
        // Valor de control de la última posición insertada (valido para lineas vacias)
        int lastInsert = 0;

        for (InvoiceModel i : invoices) {
                if (tipo.equals(i.getType())) {

                    // Cogemos el valor fecha con formato yyyy MMM par coger el valor de posición en la lista de ejeX
                    dateEmitionStr = i.getDate().split("-");
                    dateEmition = Calendar.getInstance();
                    dateEmition.set(Integer.parseInt(dateEmitionStr[0]), Integer.parseInt(dateEmitionStr[1]) - 1, Integer.parseInt(dateEmitionStr[2]));
                    String dateEmitionParse = formater.format(dateEmition.getTime());
                    posX = itemsXAsix.indexOf(dateEmitionParse);


                    // BUG Line #46
                    // Se comprueba si el siguiente valor tiene una posición de EjeX no correlativo al anterior
                    // de ser así se introducen valores 0 en las posiciones vacias para rellenar, sólo aplica a gráfico de barras agrupadas
                    if (valueSet.size() < posX) {
                        for (int o = lastInsert; o < posX; o++) {
                            valueSet.add(new BarEntry(o, null));
                            lastInsert++;
                        }
                    }

                    // Escogemos según el tipo de gráfico escogido el valor a representar
                    if (typeChart == R.id.rb_GIFConsumption || typeChart == -1) {
                        valueSet.add(new BarEntry(posX, (float) i.getConsumption()));
                    } else {
                        valueSet.add(new BarEntry(posX, (float) i.getAmount()));
                    }
                    lastInsert++;
                }
            }


        //Creamos el elemento de datos para la gráfica y establecemos colores
        BarDataSet barDataSet = new BarDataSet(valueSet, tipo);
        Random r = new Random();
        barDataSet.setColor(Color.rgb(r.nextFloat(), r.nextFloat(), r.nextFloat()));

        return barDataSet;
    }

    private LineDataSet getLineDataSet(String tipo, ArrayList<String> itemsXAsix) {

        // Creamos la lista y le incorporamos el dato requerido del tipo requerido
        ArrayList<Entry> valueSet = new ArrayList<>();

        // Valores de control para posición del ejeX
        int posX;
        String[] dateEmitionStr;
        Calendar dateEmition;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy MMM", Locale.getDefault());
        // Valor de control de la última posición insertada (valido para lineas vacias)
        int lastInsert = 0;

        for (InvoiceModel i : invoices) {

            // Cogemos el valor fecha con formato yyyy MMM par coger el valor de posición en la lista de ejeX
            dateEmitionStr = i.getDate().split("-");
            dateEmition = Calendar.getInstance();
            dateEmition.set(Integer.parseInt(dateEmitionStr[0]), Integer.parseInt(dateEmitionStr[1]) - 1, Integer.parseInt(dateEmitionStr[2]));
            String dateEmitionParse = formater.format(dateEmition.getTime());
            posX = itemsXAsix.indexOf(dateEmitionParse);

            // BUG Line #46
            // Se comprueba si el siguiente valor tiene una posición de EjeX no correlativo al anterior
            // de ser así se introducen valores 0 en las posiciones vacias para rellenar, sólo aplica a gráfico de barras agrupadas
            if (valueSet.size() < posX) {
                for (int o = lastInsert; o < posX; o++) {
                    valueSet.add(new Entry(o, 0));
                    lastInsert++;
                }
            }


            if (tipo.equals(i.getType())) {
                if (typeChart == R.id.rb_GIFConsumption || typeChart == -1) {
                    valueSet.add(new Entry(posX, (float) i.getConsumption()));
                } else if (typeChart == R.id.rb_GIFCost){
                    valueSet.add(new Entry(posX, (float) i.getAmount()));
                }
                lastInsert++;

            }
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

        // Creamos la lista y le incorporamos el dato requerido del tipo requerido
        ArrayList<PieEntry> valueSet = new ArrayList<>();

        float typeSum = 0; //Valor de suma de tipo
//        float sum = 0; // Valor de suma de todos los tipos unidos
        String cad; // Valor del tipo

        // Obtenemos la lista de tipos con su suma de valores
        HashMap<String, Float> values = new HashMap<>();
            cad = getString(R.string.consumption);
            for (int t = 0; t < types.size(); t++) {
                String type = types.get(t);
                for (InvoiceModel i : invoices) {
                    if (type.equals(i.getType())) {
                        if (typeChart == R.id.rb_GIFConsumption || typeChart == -1) {
                            typeSum += i.getConsumption();
                        } else if (typeChart == R.id.rb_GIFCost) {
                            typeSum += i.getAmount();
                        }
                    }
                }
                values.put(type, typeSum);
                typeSum = 0;
            }


//        // Calculamos a partir de la suma total de los tipo y la suma individual de cada tipo su procentaje
//        HashMap<String, Float> percentValues = new HashMap<>();
//        for (Map.Entry<String, Float> entry : values.entrySet()) {
//            float percent = (entry.getValue() * 100) / sum;
//            percentValues.put(entry.getKey(), percent);
//        }

        // Asignamos a la lista de pieEntry los valores y su tipo y creamos una lista de colores
        ArrayList<Integer> colors = new ArrayList<>();
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
                view.getId() == R.id.rb_gitc_chartpie) {

            changeChart();
        }
    }

    // Subclase que permite formatear el texto en caso de ser 0
    private static class MyValueFormatter extends ValueFormatter {

        private final DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value) {
            if (value > 0){
                return mFormat.format(value);
            } else {
                return "";
            }
        }


    }
}

