package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameStateLoader;
import com.codecool.dungeoncrawl.dao.GameStateSaver;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.map.CellType;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.mapgenerator.MapLoader;
import com.codecool.dungeoncrawl.logic.map.Tiles;
import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;


public class Main extends Application {
   GameMap map = MapLoader.loadMap();
   Player player = map.getPlayer();
   Inventory inventory = player.getInventory();
   GameDatabaseManager dbManager;
   InventoryModel inventoryModel = new InventoryModel();


   final double SCREEN_WIDTH = 25;
   final double SCREEN_HEIGHT = 19;
   final int CENTER_X = (int) SCREEN_WIDTH / 2;
   final int CENTER_Y = (int) SCREEN_HEIGHT / 2;

   Canvas canvas = new Canvas(
           SCREEN_WIDTH * Tiles.TILE_WIDTH,
           SCREEN_HEIGHT * Tiles.TILE_WIDTH);

   GraphicsContext context = canvas.getGraphicsContext2D();

   URL stepURL = ClassLoader.getSystemClassLoader().getResource("step2.wav");
   AudioClip step = new AudioClip(stepURL.toString());
   static URL swordURL = ClassLoader.getSystemClassLoader().getResource("sword.wav");
   public static AudioClip sword = new AudioClip(swordURL.toString());
   static URL deadURL = ClassLoader.getSystemClassLoader().getResource("dead.wav");
   public static AudioClip dead = new AudioClip(deadURL.toString());
   static URL gateURL = ClassLoader.getSystemClassLoader().getResource("gate.wav");
   public static AudioClip gate = new AudioClip(gateURL.toString());

   Label healthLabel = new Label();
   Label strength = new Label();
   Label inventoryLabel = new Label("Inventory: ");

   Canvas inv = new Canvas(128, 128);
   GraphicsContext invContext = inv.getGraphicsContext2D();

   public static void main(String[] args) {
      launch(args);
   }

   @Override
   public void start(Stage primaryStage) throws Exception {

      GridPane ui = new GridPane();
      ui.setPrefWidth(200);
      ui.setPadding(new Insets(10));
      ui.setVgap(10);
      ui.add(healthLabel, 0, 0, 8, 1);
      ui.add(strength, 0, 1, 8, 1);
      ui.add(inventoryLabel, 0, 2, 8, 1);
      ui.add(inv, 2, 4, 8, 8);

      BorderPane borderPane = new BorderPane();
      borderPane.setCenter(canvas);
      borderPane.setRight(ui);
      Scene scene = new Scene(borderPane);
      primaryStage.setScene(scene);
      refresh();
      scene.setOnKeyPressed(this::onKeyPressed);
      primaryStage.setTitle("Dungeon Crawl");
      primaryStage.show();

      loadGamePopup(primaryStage);
      saveGamePopup(primaryStage);
   }

   private void loadGamePopup(Stage primaryStage) {
      primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,(event) -> {
         if(event.isControlDown() && event.getText().equals("l")){
            setupDbManager();
            ListView<String> listView = new ListView<>();
            List<GameState> gameStates = dbManager.getAllGameState();

            for (GameState gameState:gameStates){
                  listView.getItems().add(gameState.getName());
            }

            Button loadButton = new Button("Load");
            loadGame(listView, loadButton);
            Button exportButton = new Button("Export");
            exportToFile(listView, exportButton, "something");
            Button importButton = new Button("Import");
            final FileChooser fileChooser = new FileChooser();
            importFile(primaryStage, importButton, fileChooser);

            HBox hBox = new HBox(loadButton, importButton, exportButton);
            hBox.setSpacing(12);
            hBox.setAlignment(Pos.CENTER);
            hBox.setMinHeight(35);
            VBox vbox = new VBox(listView, hBox);

            Scene scene = new Scene(vbox, 300, 120);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Load game");
            stage.setScene(scene);
            stage.showAndWait();
         }
      });
   }

   private void importFile(Stage primaryStage, Button importButton, FileChooser fileChooser) {
      importButton.setOnAction(event1 -> {
         File file = fileChooser.showOpenDialog(primaryStage);
         if (file != null) {
            GameStateLoader.readFromFile(file.getName());
         }
      });
   }

   private void exportToFile(ListView<String> listView, Button exportButton, String filename) {
      exportButton.setOnAction(event1 -> {
         String selectedState = listView.getSelectionModel().getSelectedItem();
         GameState gameState = dbManager.getGameStateByName(selectedState);
         GameStateSaver.saveToFile(MapLoader.loadMap(gameState.getCurrentMap()), filename);
      });
   }

   private void loadGame(ListView<String> listView, Button loadButton) {
      loadButton.setOnAction(event1 -> {
        String selectedState = listView.getSelectionModel().getSelectedItem();
        GameState gameState = dbManager.getGameStateByName(selectedState);

         InventoryModel fromDBInventoryModel = dbManager.getInventory(gameState.getPlayer().getId());

         map = MapLoader.loadMap(gameState.getCurrentMap());
         PlayerModel playerModel = gameState.getPlayer();
         player = new Player(playerModel.getPlayerName(), new Cell(playerModel.getX(), playerModel.getX(), CellType.EMPTY), playerModel.getHp());
         System.out.println(fromDBInventoryModel.getInventoryMap());
         inventory = MapLoader.loadInventoryFromString(fromDBInventoryModel.getInventoryMap());
         player.setInventory(inventory);

        refresh();
      });
   }

   private void saveGamePopup(Stage primaryStage) {
      primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,(event) -> {
            PlayerModel playerModel = new PlayerModel(player);

         if(event.isControlDown() && event.getText().equals("s")){
            Button saveButton = new Button("Save Game");
            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField();
            HBox hBox = new HBox(nameLabel, nameField);
            hBox.setAlignment(Pos.BASELINE_CENTER);
            hBox.setSpacing(10);
            Button backButton = new Button("Cancel");
            VBox vBox = new VBox(hBox, saveButton,backButton);
            vBox.setAlignment(Pos.BASELINE_CENTER);
            vBox.setSpacing(30);

            saveGame(nameField, saveButton, playerModel);

            Scene sceneSave = new Scene(vBox,240,180);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Game Menu");
            stage.setScene(sceneSave);
            backButton.setOnAction(e -> stage.close());
            stage.showAndWait();
         }
      });
   }

   private void saveGame(TextField textField, Button saveButton, PlayerModel playerModel) {

      saveButton.setOnAction(e ->{
      setupDbManager();
         inventoryModel.setOwner(playerModel);
         inventoryModel.setInventoryMap(GameStateSaver.convertMapToString(inventory));
      String fieldText = "empty";
      if(textField.getText() != null && !textField.getText().isEmpty()){
         fieldText = textField.getText();
      }
      GameState gameState = new GameState(fieldText, GameStateSaver.convertMapToString(map),new Timestamp(System.currentTimeMillis()), playerModel);

         if(isStateNameAlreadyPresent(fieldText)){
            dialogShowUp(gameState);
         }else{
            dbManager.savePlayer(playerModel);
            dbManager.saveGameState(gameState);
            dbManager.saveInventory(inventoryModel);
         }
      });
   }

   private void dialogShowUp(GameState gameState) {
      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.setTitle("State already exists!!!");
      dialog.setContentText("Would you like to overwrite the already existing state?");
      ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES );
      ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
      dialog.getDialogPane().getButtonTypes().addAll(yesButton,noButton);

      dialog.showAndWait().filter( resp -> resp == yesButton).ifPresent(resp -> {
                  dbManager.updateGameState(gameState);
                  dbManager.updatePlayer(gameState.getPlayer());
                  dbManager.updateInventory(inventoryModel);
      });
   }

   private boolean isStateNameAlreadyPresent(String fieldText) {
         List<GameState> gameStates = dbManager.getAllGameState();
         if(gameStates == null){
         return false;
         }
         for (GameState item : gameStates){
            if(fieldText != null && fieldText.equals(item.getName())){
               return true;
            }
         }
         return false;
   }

   private void onKeyPressed(KeyEvent keyEvent) {
      switch (keyEvent.getCode()) {
         case UP:
            map.getPlayer().move(0, -1, map);
            refresh();
            step.play();
            break;
         case DOWN:
            map.getPlayer().move(0, 1, map);
            refresh();
            step.play();
            break;
         case LEFT:
            map.getPlayer().move(-1, 0, map);
            refresh();
            step.play();
            break;
         case RIGHT:
            map.getPlayer().move(1, 0, map);
            refresh();
            step.play();
            break;

      }
   }

   private void moveMobs() {
      for (int y = 0; y < map.getHeight(); y++) {
         for (int x = 0; x < map.getWidth(); x++) {
            Cell cell = map.getCell(x,y);
            if (cell.getActor() != null && !(cell.getActor().equals(map.getPlayer()))) {
               cell.getActor().enemyMove(map);
            }
         }
      }
   }

   private void refresh() {
      healthLabel.setText("Health: " + player.getHealth());
      strength.setText("Strength: " + player.getStrength());

      context.setFill(Color.BLACK);
      context.setGlobalAlpha(1.0);
      context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

      int playerPosX = map.getPlayer().getX();
      int playerPosY = map.getPlayer().getY();
      int shiftedXPos = playerPosX - (int) (SCREEN_WIDTH / 2);
      int shiftedYPos = playerPosY - (int) (SCREEN_HEIGHT / 2);

      // FOG OF WAR basic value
      double maxDistance; // 'powered' value (--> value^2)
      double minAlpha;


      for (int y = 0; y < SCREEN_HEIGHT; y++) {
         for (int x = 0; x < SCREEN_WIDTH; x++) {
            if ((shiftedXPos + x) < 0 || (shiftedYPos + y) < 0 ||
                    (shiftedXPos + x) >= map.getWidth() || (shiftedYPos + y) >= map.getHeight()) {
               // Tiles.drawTile(context, CellType.FLOOR, x, y); // --> valamilyen tile típus kirajzolása az üres helyekre
               continue;
            }

            // FOG OF WAR switched ON
            double distance = (CENTER_X - x) * (CENTER_X - x) + (CENTER_Y - y) * (CENTER_Y - y);
            if (playerPosY > 30) {
               // underground
               context.setGlobalAlpha(1 / distance / 4 * 10);
               minAlpha = 0.02;
               maxDistance = 26;
            }
            else {
               // outdoors
               context.setGlobalAlpha(1 / distance * 20);
               maxDistance = 55;
               minAlpha = 0.08;
            }

            if (distance > maxDistance) {
               context.setGlobalAlpha(minAlpha);
            }
            // FOG OF WAR PART END

            Cell cell = map.getCell(shiftedXPos + x, shiftedYPos + y);
            // automated picking up of the items and adding items into inventory
            if (cell.getActor() instanceof Player && (cell.getItem() != null)) {
               if (player.getInventory().getItems().size() < 16) {
                  Item res = cell.getItem();
                  inventory.addItem(res);
                  inventoryModel.addItem(res.getTileName());
                  System.out.println(res.getTileName());
                  cell.setItem(null);

                  if("weapon".equals(res.getTileName())){
                     player.setStrengthByWeapon();
                  }
               }
            }

            // drawing elements of map
            if (cell.getActor() != null) {
               Tiles.drawTile(context, cell.getActor(), x, y);
            } else if (cell.getItem() != null) {
               Tiles.drawTile(context, cell.getItem(), x, y);
            } else {
               Tiles.drawTile(context, cell, x, y);
            }
         }
      }
      drawInventory(inventory.getItems());
      moveMobs();
   }

   public void drawInventory(List<Item> items) {
      int itemsIndex = 0;

      for (int y = 0; y < inv.getHeight()/ Tiles.TILE_WIDTH; y++) {
         for (int x = 0; x < inv.getWidth()/Tiles.TILE_WIDTH; x++) {
            Cell cell = player.getInventory().getCell(x, y);
            if (itemsIndex < items.size()) {
               cell.setItem(items.get(itemsIndex));
               Tiles.drawTile(invContext, cell.getItem(), x, y);
               itemsIndex++;
               continue;
            }
            else {
               cell.setItem(null);
               Tiles.drawTile(invContext, cell, x, y);
            }
         }
      }
   }
    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException | FileNotFoundException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    public void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}