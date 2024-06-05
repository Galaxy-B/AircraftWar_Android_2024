package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.aircraftwar2024.Dao.PlayerScore;
import com.example.aircraftwar2024.Dao.PlayerScoreDao;
import com.example.aircraftwar2024.Dao.ScoreDaoImpl;
import com.example.aircraftwar2024.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecordActivity extends AppCompatActivity {

    private String gameType;

    private Context context;
    private PlayerScore playerScore;

    private String file = "easy_game.txt";

    private PlayerScoreDao playerScoreDao = new ScoreDaoImpl(this, file);

    private File scoreFile;

    private int index = 0;
    private FileOutputStream fos = null;
    private OutputStreamWriter osw = null;

    private FileInputStream fis = null;
    private InputStreamReader isr = null;
    private int row = -1;
    private int cnt = 0;
    private String[][] tableData;

    public RecordActivity() throws IOException, ClassNotFoundException {
        // 读取文件中的数据并更新列表
        playerScoreDao.getAllItems(scoreFile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        gameType = getIntent().getStringExtra("mode");
        TextView degree = findViewById(R.id.pattern);

        switch(gameType){
            case "A":
                file = "easy_game.txt";
                degree.setText("简单模式");
                break;
            case "B":
                file = "normal_game.txt";
                degree.setText("普通模式");
                break;
            case "C":
                file = "hard_game.txt";
                degree.setText("困难模式");
                break;
        }

        //获得Layout里面的ListView
        ListView list = (ListView) findViewById(R.id.record);

        getData();
        //添加单击监听
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long id) {
                //Map<String, Object> record_map = (Map<String, Object>) adapterView.getItemAtPosition(postion);
                row = postion;
            }
            AlertDialog builder = new AlertDialog.Builder(context)
                    //设置对话框的按钮文本及按钮监听器
                    .setMessage("是否确定删除？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        playerScoreDao.deleteByTime(tableData,scoreFile);
                        tableData[row + cnt][3] = "delete";
                        cnt++;
                        playerScoreDao.deleteByTime(tableData,scoreFile);
                    }
                })
                    .setNegativeButton("取消",null)
                    .show();
        });



    }

    private void getData(){
        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        // 读取文件中的数据并更新列表
        playerScoreDao.addItem(playerScore, scoreFile);
        playerScoreDao.getAllItems(scoreFile);
        playerScoreDao.sortByScore();
        tableData = playerScoreDao.outPutItems();
        playerScoreDao.getAllItems(scoreFile);

        map.put("rank", ++index);
        map.put("user", "test");
        map.put("score", tableData[index-1][2]);
        map.put("time", tableData[index-1][3]);
        listitem.add(map);

        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(
                this,
                listitem,
                R.layout.activity_item,
                new String[]{"排名","用户","得分","时间"},
                new int[]{R.id.rank,R.id.user,R.id.score,R.id.time}
        );
        //添加并显示
        list.setAdapter(listItemAdapter);
    }
}