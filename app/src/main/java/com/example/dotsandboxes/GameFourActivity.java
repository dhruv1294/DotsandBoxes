package com.example.dotsandboxes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dotsandboxes.objects.HumanPlayer;
import com.example.dotsandboxes.objects.Player;
import com.example.dotsandboxes.objects.PlayerStateView;

import java.util.Map;

public class GameFourActivity extends AppCompatActivity implements PlayerStateView {
    GameView GameView;
    Player[] players;
    Integer[] playersOccupying = new Integer[]{0, 0, 0, 0};
    Player currentPlayer;
    public static int started=0;
    int gridSize = MultiPlayerGridSelectionActivity.gridfinal;
    TextView player1name, player2name,player3name,player4name, player1state, player2state,player3state,player4state, player1occupying, player2occupying,player3occupying,player4occupying;

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.undo_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.undo){
            try{
                GameView.undo();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        super.onOptionsItemSelected(item);
        return true;
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_four);
        GameView =  findViewById(R.id.gameView);
        started=0;

        GameView.setPlayerState(this);

        player1name = findViewById(R.id.player14name);
        player2name = findViewById(R.id.player24name);
        player3name = findViewById(R.id.player34name);
        player4name = findViewById(R.id.player44name);
        player1state = findViewById(R.id.player14state);
        player2state = findViewById(R.id.player24state);
        player3state = findViewById(R.id.player34state);
        player4state = findViewById(R.id.player44state);
        player1occupying = findViewById(R.id.player14occupying);
        player2occupying = findViewById(R.id.player24occupying);
        player3occupying = findViewById(R.id.player34occupying);
        player4occupying = findViewById(R.id.player44occupying);

        players =  new Player[]{new HumanPlayer("Player1"), new HumanPlayer("Player2"),new HumanPlayer("Player3"),new HumanPlayer("Player4")};
        GameView.startGame(players);
        updateState();
    }

    public void updateState() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(started == 0){
                    player1state.setText("Thinking");
                    player2state.setText("Waiting");
                    player3state.setText("Waiting");
                    player4state.setText("Waiting");
                }
                else if (currentPlayer == players[0]) {
                    player1state.setText("Thinking");
                    player2state.setText("Waiting");
                    player3state.setText("Waiting");
                    player4state.setText("Waiting");
                } else if(currentPlayer==players[1]) {
                    player2state.setText("Thinking");
                    player1state.setText("Waiting");
                    player3state.setText("Waiting");
                    player4state.setText("Waiting");
                }else if(currentPlayer==players[2]){
                    player2state.setText("Waiting");
                    player1state.setText("Waiting");
                    player3state.setText("Thinking");
                    player4state.setText("Waiting");
                }else{
                    player2state.setText("Waiting");
                    player1state.setText("Waiting");
                    player3state.setText("Waiting");
                    player4state.setText("Thinking");
                }
                player1occupying.setText("Boxes: " + playersOccupying[0]);
                player2occupying.setText("Boxes: " + playersOccupying[1]);
                player3occupying.setText("Boxes:" + playersOccupying[2]);
                player4occupying.setText("Boxes:" + playersOccupying[3]);
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
        playersOccupying[2] = player_occupyingBoxesCount_map.get(players[2]);
        playersOccupying[3] = player_occupyingBoxesCount_map.get(players[3]);
        updateState();
    }

    @Override
    public void setWinner(final String winner) {
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

                new AlertDialog.Builder(GameFourActivity.this)
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
