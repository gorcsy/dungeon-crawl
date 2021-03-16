package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {

    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, hp, x, y) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //minden adott playerre lefut a getplayer
    @Override
    public void update(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE player SET " +
                    "hp = ?," +
                    "x = ?," +
                    "y = ? " +
                    "WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, player.getHp());
            statement.setInt(2, player.getX());
            statement.setInt(3, player.getY());
            statement.setInt(4, player.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public PlayerModel get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM player WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            PlayerModel resultModell = new PlayerModel(resultSet.getString("player_name"),
                    resultSet.getInt("x"),
                    resultSet.getInt("y"));
            resultModell.setHp(resultSet.getInt("hp"));
            statement.close();
            return resultModell;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM player";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List<PlayerModel> list = new ArrayList<>();
            while (resultSet.next()) {
                PlayerModel resultModell = new PlayerModel(resultSet.getString("player_name"),
                        resultSet.getInt("x"),
                        resultSet.getInt("y"));
                resultModell.setHp(resultSet.getInt("hp"));
                list.add(resultModell);

            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
