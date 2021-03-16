package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.InventoryModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class InventoryDaoJdbc implements InventoryDao{

    private DataSource dataSource;


    public InventoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(InventoryModel inventoryModel) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO inventory (items, inventory_map, owner_id) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, inventoryModel.toString());
            statement.setString(2,inventoryModel.getInventoryMap());
            statement.setInt(3, inventoryModel.getOwner().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            inventoryModel.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(InventoryModel inventoryModel) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE inventory SET " +
                    "items = ?," +
                    "inventory_map = ?," +
                    "owner_id = ?" +
                    "WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, inventoryModel.toString());
            statement.setString(2,inventoryModel.getInventoryMap());
            statement.setInt(3, inventoryModel.getOwner().getId());
            statement.setInt(4, inventoryModel.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //not tested yet!!!!!!!!!!
    @Override
    public InventoryModel getInventoryModel(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM inventory WHERE owner_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            InventoryModel inventoryModel = new InventoryModel(new ArrayList<>(Arrays.asList(resultSet.getString(2).split("\\s*,\\s*"))), resultSet.getString("inventory_map"));
            statement.close();
            return inventoryModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
