package com.codecool.dungeoncrawl.model;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

public class GameState extends BaseModel {
    private static final AtomicInteger count = new AtomicInteger(0);
    private final int ID;
    private String name;
    private Timestamp savedAt;
    private String currentMap;
    private PlayerModel player;

    public GameState(String name,String currentMap, Timestamp savedAt, PlayerModel player) {
        this.name = name;
        this.currentMap = currentMap;
        this.savedAt = savedAt;
        this.player = player;
        ID = count.incrementAndGet();
    }

    public Timestamp getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Timestamp savedAt) {
        this.savedAt = savedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }
    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    @Override
    public int getId() {
        return ID;
    }
}
