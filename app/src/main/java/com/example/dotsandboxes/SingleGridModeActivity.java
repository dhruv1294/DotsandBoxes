package com.example.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SingleGridModeActivity extends AppCompatActivity {
    String[] grids = {"3X3","4X4","5X5","6X6","7X7","8X8"};
    public static int grid;
    Spinner spin;
    Button playButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_grid_mode);
        playButton = findViewById(R.id.playButton);
        spin = findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,grids);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spin.setAdapter(arrayAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                grid=position+4;
                Log.i("gridsize",Integer.toString(grid));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound2);
                mediaPlayer.start();
                Intent intent = new Intent(getApplicationContext(),GameSingleActivity.class);

                startActivity(intent);
            }
        });

    }
}
