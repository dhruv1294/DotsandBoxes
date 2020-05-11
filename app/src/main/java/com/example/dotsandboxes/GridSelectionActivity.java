package com.example.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GridSelectionActivity extends AppCompatActivity {

    Button button4x4,button5x5,button7x7,button10x10;
   public static int grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_selection);
        button4x4 = findViewById(R.id.button4x4);
        button5x5 = findViewById(R.id.button5x5);
        button7x7 = findViewById(R.id.button7x7);
        button10x10= findViewById(R.id.button10x10);
        button4x4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passToGameActivity(5);

            }
        });
        button5x5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passToGameActivity(6);

            }
        });
        button7x7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passToGameActivity(8);

            }
        });
        button10x10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passToGameActivity(11);

            }
        });
    }
    public void passToGameActivity(int gridSize){
        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
        intent.putExtra("gridSize",gridSize);
        grid=gridSize;
        startActivity(intent);

    }
}
