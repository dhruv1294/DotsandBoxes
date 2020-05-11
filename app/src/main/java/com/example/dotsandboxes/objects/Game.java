package com.example.dotsandboxes.objects;

import android.util.Log;

import com.example.dotsandboxes.GameActivity;
import com.example.dotsandboxes.GridSelectionActivity;

import java.util.Observable;

public class Game extends Observable {
    private Player[] players;
    private int currentPlayerIndex;
    private int width;
    private int height;
    int maxGrid = GameActivity.gridSize-1;


    private Player[][] occupied;
    private Player[][]lineOccupied;
    private boolean[][] horizontalLines;
    private boolean[][] verticalLines;
    private Line latestLine;

    public Game(int width,int height,Player[] players ){


        this.width = width;
        this.height = height;
        this.players = players;
        lineOccupied = new Player[height][width];
        occupied = new Player[height][width];
        horizontalLines = new boolean[height+1][width];
        verticalLines = new boolean[height][width+1];

        // addPlayersToGame(players);
        currentPlayerIndex=0;


    }



    public Player[] getPlayers(){
        return players;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public Line getLatestLine(){
        return latestLine;
    }

    /*private void addPlayersToGame(Player[] players){
        for(Player player : players){
            player.addToGame(this);
        }
    }*/


    public void start(){
        while(!isGameFinished()){
            addMove(currentPlayer().move());
            setChanged();
            notifyObservers();
        }

    }
    public void addMove(Line move){
        if(isLineOccupied(move)){
            return;
        }
        boolean newBoxOccupied = tryToOccupyBox(move);
        setLineOccupied(move);
        latestLine = move;
        if(!newBoxOccupied)
        toNextPlayer();

    }
    public Player currentPlayer(){
        return players[currentPlayerIndex];
    }
    public boolean isLineOccupied(Direction direction, int row, int column){
        return isLineOccupied(new Line(direction,row,column));
    }
    public boolean isLineOccupied(Line line){
        switch (line.direction()){
            case HORIZONTAL:
                return horizontalLines[line.row()][line.column()];
            case VERTICAL:
                return  verticalLines[line.row()][line.column()];
        }
        throw new IllegalArgumentException(line.direction().toString());
    }
    public Player getBoxOccupier(int row, int column){
        return occupied[row][column];
    }

    public int getPlayerOccupyingBoxCount(Player player){
        int count =0;
        for(int i=0;i<getHeight();i++){
            for(int j=0;j<getWidth();j++){
                if(getBoxOccupier(i,j)==player){
                    count++;
                }
            }

        }
        return count;
    }

    public Player getLineOccupier(int row,int column){
        return lineOccupied[row][column];
    }
    public void setLineOccupier(int row,int column, Player player)
    {
        lineOccupied[row][column] = player;
    }

    private boolean tryToOccupyBox(Line move){
        boolean rightOccupied = tryToOccupyRightBox(move);
        boolean underOccupied = tryToOccupyUnderBox(move);
        boolean upperOccupied = tryToOccupyUpperBox(move);
        boolean leftOccupied = tryToOccupyLeftBox(move);
        return rightOccupied || underOccupied || upperOccupied || leftOccupied;
    }


    private void setLineOccupied(Line line){
        switch (line.direction()){
            case VERTICAL:
                verticalLines[line.row()][line.column()] = true;
                break;
            case HORIZONTAL:
                horizontalLines[line.row()][line.column()] = true;
                break;

        }
    }

    private void setBoxOccupied(int row, int column , Player player){
        occupied[row][column] = player;
    }

    private boolean tryToOccupyUpperBox(Line move){
        if(move.direction()!=Direction.HORIZONTAL || move.row()<=0){
            return false;
        }
        if(isLineOccupied(Direction.HORIZONTAL,move.row()-1,move.column()) && isLineOccupied(Direction.VERTICAL,move.row()-1,move.column()) && isLineOccupied(Direction.VERTICAL,move.row()-1,move.column()+1)){
            setBoxOccupied(move.row()-1,move.column(),currentPlayer());
            return true;
        }else{
            return false;
        }
    }

    private boolean tryToOccupyUnderBox(Line move){
        if(move.direction()!=Direction.HORIZONTAL || move.row()>=height){
            return false;
        }
        if(isLineOccupied(Direction.HORIZONTAL,move.row()+1,move.column()) && isLineOccupied(Direction.VERTICAL,move.row(),move.column()) && isLineOccupied(Direction.VERTICAL,move.row(),move.column()+1)){
            setBoxOccupied(move.row(),move.column(),currentPlayer());
            return true;
        }else{
            return false;
        }
    }

    private boolean tryToOccupyLeftBox(Line move){
        if(move.direction()!=Direction.VERTICAL || move.column()<=0){
            return false;
        }
        if(isLineOccupied(Direction.VERTICAL,move.row(),move.column()-1) && isLineOccupied(Direction.HORIZONTAL,move.row(),move.column()-1) && isLineOccupied(Direction.HORIZONTAL,move.row()+1,move.column()-1)){
            setBoxOccupied(move.row(),move.column()-1,currentPlayer());
            return true;
        }else{
            return false;
        }
    }

    private boolean tryToOccupyRightBox(Line move){
        if(move.direction()!=Direction.VERTICAL || move.column()>=width){
            return false;
        }
        if(isLineOccupied(Direction.VERTICAL,move.row(),move.column()+1) && isLineOccupied(Direction.HORIZONTAL,move.row(),move.column()) && isLineOccupied(Direction.HORIZONTAL,move.row()+1,move.column())){
            setBoxOccupied(move.row(),move.column(),currentPlayer());
            return true;
        }else{
            return false;
        }
    }
    private void toNextPlayer(){
        currentPlayerIndex = (currentPlayerIndex+1)%players.length;
    }

    public boolean isGameFinished(){
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (getBoxOccupier(i, j) == null)
                    Log.i("game","unfinished "+ Integer.toString(i)+Integer.toString(j) );
                return false;
            }
        }
        Log.i("game","finished");
        return true;

    }

    public String getWinners() {


        int sum=0;
        int[] playersOccupyingBoxCount = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            playersOccupyingBoxCount[i] = getPlayerOccupyingBoxCount(players[i]);
            sum+=playersOccupyingBoxCount[i];
        }


        if(sum==(maxGrid*maxGrid)) {

            if (playersOccupyingBoxCount[0] > playersOccupyingBoxCount[1]) {
                Log.i("winner", players[0].getName());
                return players[0].getName();
            } else if(playersOccupyingBoxCount[0] < playersOccupyingBoxCount[1]){
                Log.i("winner", players[1].getName());
                return players[1].getName();
            }else{
                return "It's a Tie!!";
            }
        }
        return null;

    }

}
