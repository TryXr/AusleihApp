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

        private static final String URL = "jdbc:mysql://bszw.ddns.net:3306/bfi2124a_digibooks_v2";
        private static final String USERNAME = "bfi2124a";
        private static final String PASSWORD = "geheim";



        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }


}
