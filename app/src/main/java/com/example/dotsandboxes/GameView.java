package com.example.dotsandboxes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.dotsandboxes.objects.Game;

import com.example.dotsandboxes.objects.Direction;

import com.example.dotsandboxes.objects.HumanPlayer;
import com.example.dotsandboxes.objects.Line;
import com.example.dotsandboxes.objects.Player;
import com.example.dotsandboxes.objects.PlayerStateView;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;



public class GameView extends View implements Observer {

    Rect mRect;
    Paint mPaint;
    Paint linePaint;
    Paint boardPaint;
    Rect board;
    int gridSize = MultiPlayerGridSelectionActivity.gridfinal;


    Rect Border;
    int dotRadius;
    int margintop;

    int linewidth=40;
    protected Line move;
    protected int noOfPlayers=MultiPlayerGridSelectionActivity.players;

    protected int[] playerColors;
    protected int[] playerBoxColors;




    int height;
    int width;
    int cellSize = width / gridSize;
    protected Game game;
    Paint boardBorder;
    Paint lineColor;
    protected  PlayerStateView playerState;
    MediaPlayer sound;




    public GameView(Context context) {
        super(context);
        init(null);

    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(@Nullable AttributeSet attributeSet) {
        mRect = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(10);
        boardPaint = new Paint();
        boardPaint.setColor(Color.parseColor("#eacda3"));
        boardBorder = new Paint();
        boardBorder.setStrokeWidth(10);
        boardBorder.setStyle(Paint.Style.STROKE);
        boardBorder.setColor(Color.RED);

        lineColor = new Paint();
        lineColor.setAntiAlias(true);
        lineColor.setColor(Color.WHITE);

        board = new Rect();
        Border = new Rect();
        if(noOfPlayers==2) {
            playerColors = new int[]{Color.RED, Color.BLUE};
        }else if(noOfPlayers==3){
            playerColors = new int[]{Color.RED,Color.BLUE,Color.GREEN};
        }else if(noOfPlayers==4){
            playerColors = new int[]{Color.RED,Color.BLUE,Color.GREEN,Color.MAGENTA};
        }
        playerBoxColors = new int[]{Color.parseColor("#ff726f"),Color.CYAN};
        sound = MediaPlayer.create(getContext(),R.raw.sound);



    }

    public void setPlayerState(PlayerStateView playersState) {
        this.playerState = playersState;
    }




    public void startGame(Player[] players) {
        game = new Game(gridSize, gridSize, players);
        game.addObserver(this);
        Log.i("gridSize",Integer.toString(gridSize));

        new Thread() {
            @Override
            public void run() {
                game.start();
            }
        }.start();
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = getHeight();
        width = getWidth();
        cellSize = width / (gridSize+1);
        margintop = cellSize;
        dotRadius = cellSize / 6;
        linewidth = cellSize/6;


        board.set(0, 0, getWidth(), getHeight());
        Border.set(0, 0, getWidth(), getHeight());

        canvas.drawRect(board, boardPaint);
        canvas.drawRect(Border, boardBorder);



        for(int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize-1;j++){
                Line horizontal = new Line(Direction.HORIZONTAL, i, j);
                if (horizontal.equals(game.getLatestLine())) {
                    lineColor.setColor(game.getLineOccupier(i, j) == null ? Color.WHITE : playerColors[Player.indexIn(game.getLineOccupier(i, j), game.getPlayers())]);
                } else if (game.isLineOccupied(horizontal)) {
                    //lineColor.setColor(game.getLineOccupier(i, j) == null ? Color.GREEN : playerColors[Player.indexIn(game.getLineOccupier(i, j), game.getPlayers())]);
                    lineColor.setColor(Color.BLACK);
                } else {
                    lineColor.setColor(Color.WHITE);
                }
                canvas.drawRect(cellSize*(j+1)+linewidth,margintop+cellSize*i,cellSize*(j+2),margintop+cellSize*i+linewidth,lineColor);
                Line vertical = new Line(Direction.VERTICAL, j, i);
                 if (vertical.equals(game.getLatestLine())) {
                    lineColor.setColor(game.getLineOccupier(j, i) == null ? Color.WHITE : playerColors[Player.indexIn(game.getLineOccupier(j, i), game.getPlayers())]);
                } else if (game.isLineOccupied(vertical)) {
                    //lineColor.setColor(game.getLineOccupier(j, i) == null ? Color.GREEN : playerColors[Player.indexIn(game.getLineOccupier(j, i), game.getPlayers())]);
                    lineColor.setColor(Color.BLACK);
                } else {
                    lineColor.setColor(Color.WHITE);
                }

                canvas.drawRect(cellSize*(i+1),margintop+cellSize*(j)+linewidth,cellSize*(i+1)+linewidth,margintop+cellSize*(j+1),lineColor);
            }
        }
        for(int i=0;i<gridSize-1;i++){
            for(int j=0;j<gridSize-1;j++){


                linePaint.setColor(game.getBoxOccupier(j, i) == null ? Color.TRANSPARENT : playerColors[Player.indexIn(game.getBoxOccupier(j, i), game.getPlayers())]);
                canvas.drawRect(cellSize*(i+1)+linewidth,margintop+cellSize*j+linewidth,cellSize*(i+2),margintop+cellSize*(j+1),linePaint);
            }
        }

        for(int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize;j++){
                canvas.drawCircle(cellSize * (j+1)+linewidth/2, margintop + cellSize * (i)+linewidth/2, dotRadius, mPaint);
            }
        }






        invalidate();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                float touchX = event.getX();
                float touchY = event.getY();

                int d=-1,a=-1,b=-1;
                for(int i=0;i<gridSize;i++){
                    for(int j=0;j<gridSize-1;j++){
                        if(cellSize*(j+1)+linewidth<=touchX && touchX<=cellSize*(j+2) && margintop+cellSize*i<=touchY && touchY<=margintop+cellSize*i+linewidth){
                            d=0;
                            a=i;
                            b=j;
                            Log.i("touched",Integer.toString(a)+ " " +Integer.toString(b) +" "+ Integer.toString(d));
                        }
                        if(cellSize*(i+1)<=touchX && touchX<=cellSize*(i+1)+linewidth && margintop+cellSize*(j)+linewidth<=touchY && touchY<=margintop+cellSize*(j+1))
                        {
                            d=1;
                            a=j;
                            b=i;
                            Log.i("touched",Integer.toString(a)+ " "+ Integer.toString(b) +" "+ Integer.toString(d));
                        }
                    }
                }

                if((a!=-1) && (b!=-1)){


                    sound.start();
                    Direction direction;
                    if(d==0){
                        direction=Direction.HORIZONTAL;
                    }else{
                        direction = Direction.VERTICAL;
                    }
                    move = new Line(direction,a,b);

                    game.setLineOccupier(a, b, game.currentPlayer());

                    try {
                        ((HumanPlayer) game.currentPlayer()).add(move);
                    } catch (Exception e) {
                        Log.e("GameView", e.toString());
                    }


                }

                invalidate();
                return true;
            }


        }
        return  value;
    }

    @Override
    public void update(Observable o, Object arg) {
        playerState.setCurrentPlayer(game.currentPlayer());
        Map<Player,Integer> playerBoxCountMap = new HashMap<>();
        for(Player player : game.getPlayers()){
            playerBoxCountMap.put(player,game.getPlayerOccupyingBoxCount(player));

        }
        playerState.setPlayerOccupyingBoxesCount(playerBoxCountMap);

        String winners = game.getWinners();
        if (winners != null) {
            playerState.setWinner(winners);
        }


    }


}
