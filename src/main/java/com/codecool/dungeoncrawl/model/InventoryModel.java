package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.items.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryModel extends BaseModel{

    private String inventoryMap;
    private PlayerModel owner;
    private List<String> items = new ArrayList<>();

    public InventoryModel(List<String> items, String inventoryMap) {
        this.items = items;
        this.inventoryMap = inventoryMap;
    }

    public InventoryModel(){
        this.setId(1);
    }

    public void addItem(String item){
        items.add(item);

    }

    public String getInventoryMap() {
        return inventoryMap;
    }

    public void setInventoryMap(String inventoryMap) {
        this.inventoryMap = inventoryMap;
    }

    public PlayerModel getOwner() {
        return owner;
    }

    public void setOwner(PlayerModel owner) {
        this.owner = owner;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i =0; i< items.size(); i++){
                sb.append(items.get(i));
            if(i< items.size()-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
