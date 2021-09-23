package com.example.dam_proyecto_final.home.homeui.groupui;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dam_proyecto_final.R;

import java.util.ArrayList;

public class GroupAddActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView lstv_Members;
    private ArrayList<String> members;
    private EditText edt_AddMember;
    private ImageButton imgb_AddMember;
    private MembersAdapter membersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);


        edt_AddMember = findViewById(R.id.edt_AddMember);
        imgb_AddMember = findViewById(R.id.imgb_AddMember);
        imgb_AddMember.setOnClickListener(this);

        lstv_Members = findViewById(R.id.lstv_Members);
        members = new ArrayList<String>();
        membersAdapter = new MembersAdapter(this, members);
        lstv_Members.setAdapter(membersAdapter);
        lstv_Members.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("DEBUGME ", "on Item Click");
        membersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        //TODO comprobar con la BBDD si el user existe
        if (!edt_AddMember.getText().toString().equals("")){
            members.add(edt_AddMember.getText().toString());
            membersAdapter.notifyDataSetChanged();
            edt_AddMember.setText("");
        }
    }
}