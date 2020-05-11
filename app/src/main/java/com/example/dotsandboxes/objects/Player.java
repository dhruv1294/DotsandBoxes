package com.example.dotsandboxes.objects;

public abstract class Player {

    protected  String name;
    protected Game game;
    public Player(String name){
        this.name= name;
    }

    public static int indexIn(Player player,Player[] players){
        for(int i=0;i<players.length;i++){
            if(player == players[i]){
                //Log.i("length",Integer.toString(players.length));
                return i;
            }
        }
        return -1;
    }

    public abstract Line move();



    public void addToGame(Game game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

}
