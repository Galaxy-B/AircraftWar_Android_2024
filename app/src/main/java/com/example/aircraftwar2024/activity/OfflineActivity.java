package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aircraftwar2024.R;

public class OfflineActivity extends AppCompatActivity implements View.OnClickListener{
    private boolean is_music_on = false;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        is_music_on = getIntent().getBooleanExtra("music",false);

        Button easy_botton = findViewById(R.id.easy_game);
        Button normal_button = findViewById(R.id.normal_game);
        Button hard_button = findViewById(R.id.hard_game);

        easy_botton.setOnClickListener(this);
        normal_button.setOnClickListener(this);
        hard_button.setOnClickListener(this);

        intent = new Intent(OfflineActivity.this, GameActivity.class);
        intent.putExtra("music", is_music_on);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.easy_game) {
            intent.putExtra("gameType", 1);
            startActivity(intent);
        }
        else if(v.getId() == R.id.normal_game){
            intent.putExtra("gameType", 2);
            startActivity(intent);
        }
        else if(v.getId() == R.id.hard_game){
            intent.putExtra("gameType", 3);
            startActivity(intent);
        }
    }

}