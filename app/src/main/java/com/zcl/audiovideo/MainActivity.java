package com.zcl.audiovideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    
    private ListView mListView;
    private String[] mArrays;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list);
        mArrays = getResources().getStringArray(R.array.task);
        mListView.setAdapter(new ArrayAdapter<>(this, R.layout.item_main, mArrays));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, mArrays[position], Toast.LENGTH_LONG).show();
    
                switch (position) {
                case 0:
                    Intent intent = new Intent(MainActivity.this, SurfaceViewActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, AudioActivity.class));
                    break;
                default:
                    break;
                }
            }
        });
    }
    
}
