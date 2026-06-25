package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/base_gastronomia";
    private static final String USER = "root";
    private static final String PASSWORD="Mathias_2008";
    public static Connection conectar(){
        Connection conexion = null;
        try{
            conexion = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("Conexion exitosa");
        } catch (Exception e){
            System.out.println("Error conexion:" + e.getMessage());
        }
        return conexion;
    }
}
