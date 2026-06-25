package Datos;

import Dominio.Venta;
import java.sql.*;
import java.util.*;

public class VentaDAO {

    public int insertar(Venta venta) {
        String sql = "INSERT INTO venta (fecha, total) VALUES (?,?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, new java.sql.Date(venta.getFecha().getTime()));
            ps.setDouble(2, venta.getTotal());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error insertarVenta: " + e.getMessage());
        }
        return -1;
    }

    public List<Venta> listarTodas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT idVenta, fecha, total FROM venta ORDER BY idVenta DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Venta(rs.getInt("idVenta"),
                        rs.getDate("fecha"), rs.getDouble("total")));
            }
        } catch (SQLException e) {
            System.out.println("Error listarVentas: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminar(int idVenta) {
        String sql = "DELETE FROM venta WHERE idVenta=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error eliminarVenta: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarDetalles(int idVenta) {
        String sql = "DELETE FROM detalleventa WHERE idVenta=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            System.out.println("Error eliminarDetalles: " + e.getMessage());
            return false;
        }
    }

    public List<Object[]> reportePorDia() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT fecha, COUNT(*) AS cantVentas, SUM(total) AS totalDia " +
                "FROM venta GROUP BY fecha ORDER BY fecha DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getString("fecha"),
                        rs.getInt("cantVentas"),
                        rs.getDouble("totalDia")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error reportePorDia: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> reportePorProducto() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.nombre, SUM(dv.cantidad) AS totalUnidades, " +
                "SUM(dv.cantidad * dv.precioUnitario) AS totalVendido " +
                "FROM detalleventa dv " +
                "JOIN producto p ON dv.idProducto = p.idProducto " +
                "GROUP BY p.idProducto, p.nombre " +
                "ORDER BY totalVendido DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getString("nombre"),
                        rs.getInt("totalUnidades"),
                        rs.getDouble("totalVendido")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error reportePorProducto: " + e.getMessage());
        }
        return lista;
    }
}