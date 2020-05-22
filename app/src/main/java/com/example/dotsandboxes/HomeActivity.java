package com.example.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    public static String mode="";
    Button multiPlayerButton;

   Button singlePlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        multiPlayerButton = findViewById(R.id.multiPlayerButton);
        singlePlayerButton = findViewById(R.id.singlePlayerButton);
        multiPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound2);
                mediaPlayer.start();
                Intent intent = new Intent(getApplicationContext(),MultiPlayerGridSelectionActivity.class);
                mode="multi";
                startActivity(intent);
            }
        });
        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound2);
                mediaPlayer.start();
                Intent intent = new Intent(getApplicationContext(),SingleGridModeActivity.class);
                mode="single";
                startActivity(intent);
            }
        });


    }
}
