package Datos;

import Dominio.Categoria;
import Dominio.Stock;
import Dominio.producto;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class StockDAO {

    public List<Stock> listarTodos() {
        List<Stock> lista = new ArrayList<>();
        String sql = "SELECT s.idStock, s.cantidadDisponible, s.stockMinimo, s.ultimaActualizacion, " +
                "p.idProducto, p.nombre, p.descripcion, p.estado, p.precio, " +
                "c.idCategoria, c.nombre AS nombreCategoria " +
                "FROM stock s " +
                "JOIN producto p ON s.idProducto = p.idProducto " +
                "JOIN categoria c ON p.idCategoria = c.idCategoria";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Categoria cat = new Categoria(rs.getInt("idCategoria"), rs.getString("nombreCategoria"));
                producto prod = new producto(rs.getInt("idProducto"), rs.getString("nombre"),
                        rs.getString("descripcion"), rs.getBoolean("estado"), rs.getDouble("precio"), cat);
                LocalDate fecha = rs.getDate("ultimaActualizacion") != null ?
                        rs.getDate("ultimaActualizacion").toLocalDate() : null;
                lista.add(new Stock(rs.getInt("idStock"), prod,
                        rs.getInt("cantidadDisponible"), rs.getInt("stockMinimo"), fecha));
            }
        } catch (SQLException e) {
            System.out.println("Error listarStock: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Stock stock) {
        String sql = "UPDATE stock SET cantidadDisponible=?, stockMinimo=?, ultimaActualizacion=? WHERE idStock=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, stock.getCantidadDisponible());
            ps.setInt(2, stock.getStockMinimo());
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setInt(4, stock.getIdStock());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizarStock: " + e.getMessage());
            return false;
        }
    }

    public boolean insertar(Stock stock) {
        String sql = "INSERT INTO stock (cantidadDisponible, stockMinimo, ultimaActualizacion, idProducto) VALUES (?,?,?,?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, stock.getCantidadDisponible());
            ps.setInt(2, stock.getStockMinimo());
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setInt(4, stock.getProducto().getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error insertarStock: " + e.getMessage());
            return false;
        }
    }
}
