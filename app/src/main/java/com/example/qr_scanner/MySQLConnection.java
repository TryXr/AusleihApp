package com.example.qr_scanner;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {

        private static final String URL = "jdbc:mysql://<your_mysql_server>:<port>/<database_name>";
        private static final String USERNAME = "<your_username>";
        private static final String PASSWORD = "<your_password>";



        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }


}
