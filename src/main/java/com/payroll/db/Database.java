package com.payroll.db;
import java.sql.*;

public final class Database {
    private static final String URL = "jdbc:sqlite:payroll.db";
    private Database() {}
    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL); }
    public static void initialize() {
        String sql = "CREATE TABLE IF NOT EXISTS employees (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL UNIQUE, department TEXT NOT NULL, designation TEXT NOT NULL, basic_salary REAL NOT NULL, allowances REAL NOT NULL, deductions REAL NOT NULL, created_at TEXT DEFAULT CURRENT_TIMESTAMP)";
        try (Connection c=getConnection(); Statement s=c.createStatement()) { s.execute(sql); }
        catch(SQLException e){ throw new RuntimeException("Could not initialize database: "+e.getMessage(),e); }
    }
}