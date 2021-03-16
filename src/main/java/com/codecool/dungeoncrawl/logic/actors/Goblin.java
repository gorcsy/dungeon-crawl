package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.GameMap;

public class Goblin extends Actor {

    public Goblin(Cell cell) {
        super("Goblin", cell);
        super.setHealth(7);
        super.setStrength(1);
        cell.setActor(this);
    }
    public void enemyMove(GameMap map) {
        int dx = 1 - (int) (Math.random()*3.0);
        int dy = 1 - (int) (Math.random()*3.0);
        super.move(dx, dy, map);
    }
}
