package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.activityManager.ActivityManager;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener
{
    private String my_score;

    private String rival_score;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ActivityManager.getActivityManager().addActivity(this);

        intent = new Intent(ResultActivity.this, MainActivity.class);

        Button back_to_menu = findViewById(R.id.result_back_to_menu);
        back_to_menu.setOnClickListener(this);

        // 获取本次对战双方的分数
        my_score = getIntent().getStringExtra("my_score");
        rival_score = getIntent().getStringExtra("rival_score");

        TextView my_score_text = findViewById(R.id.my_score);
        TextView rival_score_text = findViewById(R.id.rival_score);

        my_score_text.setText("你的分数:    " + my_score);
        rival_score_text.setText("对手分数:    " + rival_score);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.result_back_to_menu)
        {
            ActivityManager.getActivityManager().finishAllActivity();
            startActivity(intent);
        }
    }
}