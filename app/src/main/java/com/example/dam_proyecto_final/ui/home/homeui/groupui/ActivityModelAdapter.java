package com.example.dam_proyecto_final.ui.home.homeui.groupui;

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
        return activitys.get(i).getIdactivity();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.fragment_activity_item, null);

        ActivityModel activity = activitys.get(i);
        int icon = R.drawable.ic_activity_group;
        switch (activity.getIcon()){
            case 0:
                icon = R.drawable.ic_activity_group;
                break;
            case 1:
                icon = R.drawable.ic_activity_invoice;
                break;
            case 2:
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
        SimpleDateFormat sdf2 = new SimpleDateFormat(context.getString(R.string.SimpleDateFormat_pattern),
                Locale.getDefault());
        String datea = sdf2.format(calToday.getTime());
        String dateb = sdf2.format(calActivity.getTime());
        String datec = sdf2.format(calActivity.getTime());
        String dateText;
        if (datea.equals(dateb)){
            dateText = context.getString(R.string.today);
        } else if (datea.equals(datec)){
            dateText = context.getString(R.string.yesterday);
        } else {
            dateText = sdf2.format(calActivity.getTime());
        }
        txtDate.setText(dateText);

        return view;
    }
}
