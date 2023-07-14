package com.example.qr_scanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLStatements {

    public MySQLStatements(){

    }

    public ResultSet performDatabaseOperation(String sql, int zgr, Connection connection, Statement statement) {


        ResultSet resultSet = null;

        try {
            // Select
            if(zgr == 0){
                resultSet = statement.executeQuery(sql);

            }

            // Insert
            if(zgr == 1){
               int zeilen = statement.executeUpdate(sql);
            }
            //statement.close();
            //connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  resultSet;
    }


}
