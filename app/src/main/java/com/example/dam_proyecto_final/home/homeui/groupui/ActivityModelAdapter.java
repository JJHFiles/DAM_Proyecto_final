package com.example.dam_proyecto_final.home.homeui.groupui;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.ActivityModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ActivityModelAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ActivityModel> activitys;

    public ActivityModelAdapter(Context context, ArrayList<ActivityModel> activitys) {
        this.context = context;
        this.activitys = activitys;
    }

    @Override
    public int getCount() {
        return activitys.size();
    }

    @Override
    public Object getItem(int i) {
        return activitys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return activitys.get(i).getIdentifierinvoice();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.fragment_activity_item, null);


        // TODO agregar iconos de figma y setearlos al imagenview∑
        ActivityModel activity = activitys.get(i);
        int icon = R.drawable.ic_activity_group;
        switch (activity.getIcon()){
            case 0:
                // TODO icon grupo
                icon = R.drawable.ic_activity_group;
                break;
            case 1:
                // TODO icon factura
                icon = R.drawable.ic_activity_invoice;
                break;
            case 2:
                // TODO icon person
                icon = R.drawable.ic_activity_person;
                break;
            default:
                break;
        }

        ImageView imgActivityIco = view.findViewById(R.id.imgv_fai_activityico);
        TextView txtvAction = view.findViewById(R.id.txtv_fai_action);
        TextView txtDate = view.findViewById(R.id.txtv_fai_date);

        imgActivityIco.setImageDrawable(context.getDrawable(icon));

        String actionParse = "<b> " + activity.getEmail() + " </b> " + activity.getAction();
        // TODO revisar deprecated
        txtvAction.setText(Html.fromHtml(actionParse));

        Calendar calToday = Calendar.getInstance();
        Calendar calActivity = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            calActivity.setTime(sdf.parse(activity.getDate_activity()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Si el día es hoy
        String dateText;
        if (calToday.getTime().getDay() == calActivity.getTime().getDay()){
            dateText = context.getString(R.string.today);
        } else if (calToday.getTime().getDay() == calActivity.getTime().getDay()-1){
            dateText = context.getString(R.string.yesterday);
        } else {
            dateText = String.valueOf(calActivity.getTime().getDay() + "/" +
                    calActivity.getTime().getMonth() + "/" +
                    calActivity.getTime().getYear());
        }
        txtDate.setText(dateText);

        return view;
    }
}
