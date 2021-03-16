package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;


public interface InventoryDao {

    void add(InventoryModel inventoryModel);
    void update(InventoryModel inventoryModel);
    InventoryModel getInventoryModel(int id);

}
