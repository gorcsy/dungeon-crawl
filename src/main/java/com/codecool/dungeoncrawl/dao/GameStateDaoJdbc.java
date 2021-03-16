package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {

    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (state_name,current_map, saved_at, player_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getName());
            statement.setString(2, state.getCurrentMap());
            statement.setTimestamp(3, state.getSavedAt());
            statement.setInt(4, state.getPlayer().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET state_name = ? ,current_map = ?, saved_at = ?, player_id= ? WHERE state_name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, state.getName());
            statement.setString(2, state.getCurrentMap());
            statement.setTimestamp(3, state.getSavedAt());
            statement.setInt(4, state.getPlayer().getId());
            statement.setString(5, state.getName());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(String stateName) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT game_state.state_name, game_state.current_map, game_state.saved_at, player.player_name, player.hp, player.x," +
                    "player.y FROM game_state FULL OUTER JOIN player ON game_state.player_id = player.id WHERE game_state.state_name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, stateName);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            PlayerModel player = new PlayerModel(rs.getString(4),rs.getInt(6),rs.getInt(7));
            player.setHp(rs.getInt(5));
            return new GameState(rs.getString(1),rs.getString(2),rs.getTimestamp(3), player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT game_state.state_name, game_state.current_map, game_state.saved_at, player.player_name, player.hp, player.x," +
                    "player.y FROM game_state FULL OUTER JOIN player ON game_state.player_id = player.id";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            List<GameState> res = new ArrayList<>();

            while (rs.next()) {

                PlayerModel player = new PlayerModel(rs.getString(4), rs.getInt(6), rs.getInt(7));
                player.setHp(rs.getInt(5));
                res.add(new GameState(rs.getString(1), rs.getString(2), rs.getTimestamp(3), player));

            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
