package com.example.qr_scanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLStatements {

    int rows = 0;
    public MySQLStatements(){

    }

    public ResultSet performDatabaseOperation(String sql, int zgr, Connection connection, Statement statement) {

        rows = 0;
        ResultSet resultSet = null;

        try {
            // Select
            if(zgr == 0){
                resultSet = statement.executeQuery(sql);

            }
            if(zgr == 3){
                resultSet = statement.executeQuery(sql);

            }

            // Insert
            if(zgr == 1)
            {
                rows = statement.executeUpdate(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  resultSet;
    }

    public int getRows()
    {
        return rows;
    }


}
