package com.codecool.dungeoncrawl.logic.items;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.CellType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Inventory extends GameMap {

    private List<Item> items = new ArrayList<>();


    public Inventory(int width, int height, CellType defaultCellType) {
        super(width, height, defaultCellType);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item){
        items.add(item);

    }

    public void removeItem(Item item){
        Iterator<Item> it = items.iterator();
        while (it.hasNext()){
            if(it.next() == item){
                it.remove();
            }
        }
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
