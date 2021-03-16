package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GameDatabaseManager {

    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private InventoryDao inventoryDao;

    public void setup() throws SQLException, FileNotFoundException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao= new GameStateDaoJdbc(dataSource);
        inventoryDao = new InventoryDaoJdbc(dataSource);
    }

    public void savePlayer(PlayerModel player) {
        playerDao.add(player);
    }
    public void updatePlayer(PlayerModel player){
        playerDao.update(player);
    }

    public PlayerModel getPlayer(int id){
        return playerDao.get(id);
    }

    public List<PlayerModel>getAllPlayer(){
        return playerDao.getAll();
    }

    public void saveGameState(GameState gameState){
        gameStateDao.add(gameState);
    }

    public void saveInventory(InventoryModel inventoryModel){
        inventoryDao.add(inventoryModel);
    }

    public void updateInventory(InventoryModel inventoryModel) {
        inventoryDao.update(inventoryModel);
    }

    public InventoryModel getInventory(int id){
        return inventoryDao.getInventoryModel(id);
    }

    public void updateGameState(GameState gameState){
        gameStateDao.update(gameState);
    }

    public GameState getGameStateByName(String name){
        return gameStateDao.get(name);
    }

    public List<GameState> getAllGameState(){
        return gameStateDao.getAll();
    }

    private DataSource connect() throws SQLException, FileNotFoundException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = "dungeon";
        String user = "user";
        String password = "user";

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        //new code to run the sql file first by every connection
        /*Connection conn = dataSource.getConnection();
        ScriptRunner sr = new ScriptRunner(conn);
        Reader reader = new BufferedReader(new FileReader("sample_data/schema_ddl.sql"));
        sr.runScript(reader);*/
        //
        //System.out.println("Trying to connect");
        dataSource.getConnection().close();
        //System.out.println("Connection ok.");

        return dataSource;
    }

}
