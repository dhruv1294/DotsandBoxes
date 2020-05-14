package com.example.dotsandboxes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import android.widget.TextView;

import com.example.dotsandboxes.objects.Game;
import com.example.dotsandboxes.objects.HumanPlayer;
import com.example.dotsandboxes.objects.Player;
import com.example.dotsandboxes.objects.PlayerStateView;

import java.util.Map;

public class GameActivity extends AppCompatActivity implements PlayerStateView {

    GameView GameView;
    Player[] players;
    Integer[] playersOccupying = new Integer[]{0, 0};
    Player currentPlayer;
    int gridSize = MultiPlayerGridSelectionActivity.gridfinal;
    protected Game game;
    public static int undo = 0;
    public static int started=0;





    TextView player1name, player2name, player1state, player2state, player1occupying, player2occupying;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GameView =  findViewById(R.id.gameView);

        GameView.setPlayerState(this);
        Intent intent = getIntent();

        gridSize = intent.getIntExtra("gridSize",4);
        started=0;

        player1name = findViewById(R.id.player1name);
        player2name = findViewById(R.id.player2name);
        player1state = findViewById(R.id.player1state);
        player2state = findViewById(R.id.player2state);
        player1occupying = findViewById(R.id.player1occupying);
        player2occupying = findViewById(R.id.player2occupying);
        player1state.setText("Thinking");
        player2state.setText("Waiting");


        players =  new Player[]{new HumanPlayer("Player1"), new HumanPlayer("Player2")};


        GameView.startGame(players);

        updateState();



    }



    public void updateState() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(started==0){
                    player1state.setText("Thinking");
                    player2state.setText("Waiting");
                }
                else if (currentPlayer == players[0]) {
                    player1state.setText("Thinking");
                    player2state.setText("Waiting");
                } else {
                    player2state.setText("Thinking");
                    player1state.setText("Waiting");
                }
                player1occupying.setText("Boxes: " + playersOccupying[0]);
                player2occupying.setText("Boxes: " + playersOccupying[1]);
            }
        });
    }

    @Override
    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
        updateState();
    }

    @Override
    public void setPlayerOccupyingBoxesCount(Map<Player, Integer> player_occupyingBoxesCount_map) {
        playersOccupying[0] = player_occupyingBoxesCount_map.get(players[0]);
        playersOccupying[1] = player_occupyingBoxesCount_map.get(players[1]);
        updateState();

    }

    @Override
    public void setWinner( final String winner) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                started=0;
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(200);
                }
                String result="";
                if(winner.equals("It's a Tie")){
                    result = winner;
                }else{
                    result = winner + " Wins!!";
                }

                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("Dots And Boxes")
                        .setMessage(result)
                        .setPositiveButton("Play again",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        recreate();
                                    }
                                })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        })
                        .show();
            }
        });

    }
}
