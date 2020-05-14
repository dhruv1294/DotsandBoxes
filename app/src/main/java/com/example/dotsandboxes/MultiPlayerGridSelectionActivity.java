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

public class MultiPlayerGridSelectionActivity extends AppCompatActivity {
    String[] grids = {"3X3","4X4","5X5","6X6","7X7","8X8","9x9","10x10"};
    public static int gridfinal;
    public static int players;
    Spinner spin;
    Button dualPlayerButton,threePlayerButton,fourPlayerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player_grid_selection);
        GameActivity.started=0;
        GameThreeActivity.started=0;
        GameFourActivity.started=0;
        dualPlayerButton=findViewById(R.id.dualPlayerButton);
        threePlayerButton = findViewById(R.id.threePlayerButton);
        fourPlayerButton = findViewById(R.id.fourPlayerButton);
        spin = findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,grids);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spin.setAdapter(arrayAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gridfinal=position+4;
                Log.i("gridsize",Integer.toString(gridfinal));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dualPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound2);
                mediaPlayer.start();
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                players=2;
                startActivity(intent);
            }
        });
        threePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound2);
                mediaPlayer.start();
                Intent intent = new Intent(getApplicationContext(),GameThreeActivity.class);
                players=3;
                startActivity(intent);
            }
        });
        fourPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound2);
                mediaPlayer.start();
                Intent intent = new Intent(getApplicationContext(),GameFourActivity.class);
                players=4;
                startActivity(intent);
            }
        });

    }
}
