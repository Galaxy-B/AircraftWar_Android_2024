package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.aircraftwar2024.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean is_music_on = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button jump_botton = (Button) findViewById(R.id.jump_to_menu);
        jump_botton.setOnClickListener(this);

        RadioButton music_on = (RadioButton) findViewById(R.id.radio_on);
        music_on.setOnClickListener(this);

        RadioButton music_off = (RadioButton) findViewById(R.id.radio_off);
        music_off.setOnClickListener(this);

        intent = new Intent(MainActivity.this, OfflineActivity.class);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.jump_to_menu) {
            intent.putExtra("music", is_music_on);
            startActivity(intent);
        }
        else if(v.getId() == R.id.radio_on){
                is_music_on = true;
        }
        else if(v.getId() == R.id.radio_off){
                is_music_on = false;
        }
    }
}