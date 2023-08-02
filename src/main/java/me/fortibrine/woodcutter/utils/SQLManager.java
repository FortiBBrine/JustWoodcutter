package me.fortibrine.woodcutter.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLManager {

    private Connection connection;

    public SQLManager() {
        try {
            String url = "jdbc:sqlite:plugins/Woodcutter/statistic.db";

            connection = DriverManager.getConnection(url);

            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS axe (" +
                    "uuid TEXT PRIMARY KEY," +
                    "level INTEGER" +
                    ")");

            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS boosters (" +
                    "uuid TEXT," +
                    "booster DOUBLE," +
                    "time BIGINT," +
                    "global BOOLEAN)");

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<Booster> getBoosters(String uuid) {
        List<Booster> listBoosters = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT time, booster, global FROM boosters WHERE uuid = ?");
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booster booster = new Booster(resultSet.getLong(1), resultSet.getInt(2), resultSet.getBoolean(3));
                listBoosters.add(booster);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException exception) {
            return new ArrayList<>();
        }

        return listBoosters;
    }

    public void addBooster(String uuid, long time, double booster, boolean global) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO boosters(uuid, booster, time, global)" +
                    "VALUES(?, ?, ?, ?);");

            preparedStatement.setString(1, uuid);
            preparedStatement.setDouble(2, booster);
            preparedStatement.setLong(3, time);
            preparedStatement.setBoolean(4, global);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException ignored) {
        }
    }

    public int getAxeLevel(String uuid) {
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT level FROM axe WHERE uuid = ?");

            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int result = resultSet.getInt("level");

                preparedStatement.close();
                resultSet.close();

                return result;
            } else {

                preparedStatement.close();
                resultSet.close();

                return 0;
            }

        } catch (SQLException exception) {
            return 0;
        }
    }

    public void levelUp(String uuid) {
        try {

            int level = this.getAxeLevel(uuid) + 1;

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE axe SET level = ? WHERE uuid = ?");

            preparedStatement.setInt(1, level);
            preparedStatement.setString(2, uuid);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                PreparedStatement preparedStatementInsert = connection.prepareStatement("INSERT INTO axe (uuid, level) VALUES (?, ?)");
                preparedStatementInsert.setString(1, uuid);
                preparedStatementInsert.setInt(2, level);

                preparedStatementInsert.executeUpdate();
                preparedStatementInsert.close();
            }

            preparedStatement.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

}
