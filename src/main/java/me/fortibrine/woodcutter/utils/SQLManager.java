package me.fortibrine.woodcutter.utils;

import java.sql.*;

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
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
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
