/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author ACER
 */
public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/kepegawaian";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getKoneksi() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
            return null;
        }
    }
}
