package com.example.dam_proyecto_final.walktrough;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dam_proyecto_final.LoginActivity;
import com.example.dam_proyecto_final.R;

public class WalktroughFragment extends Fragment implements View.OnClickListener {

    TextView txtSkipe;
    LottieAnimationView lavAnimation;
    TextView txtvTitle;
    TextView txtDesciption;
    Button btnStart;


    private static final String POSITION = "position";


    private int position;

    public WalktroughFragment() {
        // Required empty public constructor
    }

    public static WalktroughFragment newInstance(int position) {
        WalktroughFragment fragment = new WalktroughFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_walktrough, container, false);

        txtSkipe = view.findViewById(R.id.txtv_fw_skipe);
        txtSkipe.setOnClickListener(this);
        lavAnimation = view.findViewById(R.id.lav_fw_animation);
        txtvTitle = view.findViewById(R.id.txtv_fw_title);
        txtDesciption = view.findViewById(R.id.txtv_fw_description);
        btnStart = view.findViewById(R.id.btn_fw_start);
        btnStart.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (position) {
            case 0:
                lavAnimation.setAnimation(R.raw.walktrough1);
                txtvTitle.setText(getString(R.string.walktrough_title_1));
                txtDesciption.setText(R.string.walktrough_description_1);
                break;
            case 1:
                lavAnimation.setAnimation(R.raw.walktrough2);
                txtvTitle.setText(getString(R.string.walktrough_title_2));
                txtDesciption.setText(R.string.walktrough_description_2);
                break;
            case 2:
                lavAnimation.setAnimation(R.raw.walktrough3);
                txtvTitle.setText(getString(R.string.walktrough_title_3));
                txtDesciption.setText(R.string.walktrough_description_3);
                txtSkipe.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        SharedPreferences preferences = getActivity().getSharedPreferences("savedData", getActivity().getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("walktroughtDone", true);
        editor.apply();

        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}