package com.example.dotsandboxes.objects;

import android.util.Log;


import com.example.dotsandboxes.GameActivity;
import com.example.dotsandboxes.GameFourActivity;
import com.example.dotsandboxes.GameThreeActivity;
import com.example.dotsandboxes.GameView;
import com.example.dotsandboxes.HomeActivity;
import com.example.dotsandboxes.MultiPlayerGridSelectionActivity;
import com.example.dotsandboxes.SingleGridModeActivity;

import java.util.Observable;

public class Game extends Observable {
    public Player[] players;
    public int currentPlayerIndex;
    public int previousPlayerIndex;
    private int width;
    private int height;
    int maxGrid ;


    private Player[][] occupied;
    private Player[][]lineOccupied;
    private int[][] horizontalLines;
    private int[][] verticalLines;
    public Line latestLine;

    public Game(int width,int height,Player[] players ){


        this.width = width;
        this.height = height;
        this.players = players;
        lineOccupied = new Player[height][width];
        occupied = new Player[height][width];
        horizontalLines = new int[height+1][width];
        verticalLines = new int[height][width+1];
        if(HomeActivity.mode.equals("single")){
            maxGrid = SingleGridModeActivity.grid-1;
        }else{
            maxGrid =MultiPlayerGridSelectionActivity.gridfinal-1;
        }

        addPlayersToGame(players);
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
    private void addPlayersToGame(Player[] players){
        for(Player player : players){
            player.addToGame(this);
        }
    }




    public void start(){
        GameActivity.started=1;
        GameThreeActivity.started=1;
        GameFourActivity.started=1;
        while(!isGameFinished()){
                if(GameView.undo==0)
                addMove(currentPlayer().move());
                else addMove(previousPlayer().move());
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
    public Player previousPlayer(){
        if(currentPlayerIndex==0){
            previousPlayerIndex=players.length-1;
            Log.i("here",Integer.toString(previousPlayerIndex));
        }else{

            previousPlayerIndex = currentPlayerIndex-1;
            Log.i("here",Integer.toString(previousPlayerIndex));
        }
        Log.i("prevous","player");
        return players[previousPlayerIndex];
    }

    public void setCurrentPlayer(){
        currentPlayerIndex = previousPlayerIndex;
        Log.i("index",Integer.toString(currentPlayerIndex));
        setChanged();
        notifyObservers();
    }
    public boolean isLineOccupied(Direction direction, int row, int column){
        return isLineOccupied(new Line(direction,row,column));
    }
    public boolean isLineOccupied(Line line){
        switch (line.direction()){
            case HORIZONTAL:
                return (horizontalLines[line.row()][line.column()]==1||horizontalLines[line.row()][line.column()]==2||horizontalLines[line.row()][line.column()]==3||horizontalLines[line.row()][line.column()]==4);
            case VERTICAL:
                return  (verticalLines[line.row()][line.column()]==1||verticalLines[line.row()][line.column()]==2||verticalLines[line.row()][line.column()]==3||verticalLines[line.row()][line.column()]==4);

        }
        throw new IllegalArgumentException(line.direction().toString());
    }
    public int getLineOccupier(Line line) {
        switch (line.direction()) {
            case HORIZONTAL:
                return horizontalLines[line.row()][line.column()];
            case VERTICAL:
                return verticalLines[line.row()][line.column()];
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
    public void unsetLineOccupier(int row,int column){
      lineOccupied[row][column]=null;
    }

    public boolean tryToOccupyBox(Line move){
        boolean rightOccupied = tryToOccupyRightBox(move);
        boolean underOccupied = tryToOccupyUnderBox(move);
        boolean upperOccupied = tryToOccupyUpperBox(move);
        boolean leftOccupied = tryToOccupyLeftBox(move);
        return rightOccupied || underOccupied || upperOccupied || leftOccupied;
    }


    private void setLineOccupied(Line line){
        switch (line.direction()){
            case VERTICAL:
                verticalLines[line.row()][line.column()] = currentPlayerIndex+1;
                break;
            case HORIZONTAL:
                horizontalLines[line.row()][line.column()] = currentPlayerIndex+1;
                break;

        }
    }
    public void unsetLineOccupied(Line line){
        switch (line.direction()){
            case VERTICAL:
                verticalLines[line.row()][line.column()] = 0;
                break;
            case HORIZONTAL:
                horizontalLines[line.row()][line.column()] = 0;
                break;

        }
    }

    private void setBoxOccupied(int row, int column , Player player){
        occupied[row][column] = player;
    }
    public void unsetBoxOccupied(int row,int column){
        occupied[row][column]=null;
    }

    public boolean tryToOccupyUpperBox(Line move){
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

    public boolean tryToOccupyLeftBox(Line move){
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
        for (int i = 0; i < getHeight()-1; i++) {
            for (int j = 0; j < getWidth()-1; j++) {
                if (getBoxOccupier(i, j) == null) {
                    Log.i("game", "unfinished " + Integer.toString(i) + Integer.toString(j));
                    return false;
                }
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
            int maxScore=playersOccupyingBoxCount[0];
            int maxIndex=0;
            for(int i=0;i<players.length;i++){
                if(playersOccupyingBoxCount[i]>maxScore){
                    maxScore=playersOccupyingBoxCount[i];
                    maxIndex=i;
                }
            }
            for(int i=0;i<players.length;i++){
                if(playersOccupyingBoxCount[maxIndex]==playersOccupyingBoxCount[i]&&i!=maxIndex){
                    return "It's a Tie";
                }
            }
            return players[maxIndex].getName();

            /*if (playersOccupyingBoxCount[0] > playersOccupyingBoxCount[1]) {
                Log.i("winner", players[0].getName());
                return players[0].getName();
            } else if(playersOccupyingBoxCount[0] < playersOccupyingBoxCount[1]){
                Log.i("winner", players[1].getName());
                return players[1].getName();
            }else{
                return "It's a Tie!!";
            }*/
        }
        return null;

    }

}
