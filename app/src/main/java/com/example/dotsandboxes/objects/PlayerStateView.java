package com.example.dotsandboxes.objects;

import java.util.Map;

public interface PlayerStateView {
    void setCurrentPlayer(Player player);

    void setPlayerOccupyingBoxesCount(Map<Player, Integer> player_occupyingBoxesCount_map);



    void setWinner(String winner);
}
