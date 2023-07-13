package com.example.qr_scanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLStatements {

    public MySQLStatements(){

    }

    public ResultSet performDatabaseOperation(String sql, int zgr) {

        ResultSet resultSet = null;

        try {
            Connection connection = MySQLConnection.getConnection();
            Statement statement = connection.createStatement();

            // Select
            if(zgr == 0){
                resultSet = statement.executeQuery(sql);
            }

            // Insert
            if(zgr == 1){
               int zeilen = statement.executeUpdate(sql);
            }


           //resultSet.close();
            //statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  resultSet;
    }
}
