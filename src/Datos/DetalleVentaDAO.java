package Datos;

import Dominio.*;
import java.sql.*;
import java.util.*;

public class DetalleVentaDAO {

    public boolean insertar(DetalleVenta detalle) {
        String sql = "INSERT INTO detalleventa (idVenta, idProducto, cantidad, precioUnitario) VALUES (?,?,?,?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detalle.getVenta().getIdVenta());
            ps.setInt(2, detalle.getProducto().getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getProducto().getPrecio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error insertarDetalle: " + e.getMessage());
            return false;
        }
    }

    public List<DetalleVenta> listarPorVenta(int idVenta) {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT dv.idDetalle, dv.cantidad, dv.precioUnitario, " +
                "p.idProducto, p.nombre, p.descripcion, p.estado, p.precio, " +
                "c.idCategoria, c.nombre AS nombreCategoria " +
                "FROM detalleventa dv " +
                "JOIN producto p ON dv.idProducto = p.idProducto " +
                "JOIN categoria c ON p.idCategoria = c.idCategoria " +
                "WHERE dv.idVenta = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria cat = new Categoria(rs.getInt("idCategoria"), rs.getString("nombreCategoria"));
                producto prod = new producto(rs.getInt("idProducto"), rs.getString("nombre"),
                        rs.getString("descripcion"), rs.getBoolean("estado"),
                        rs.getDouble("precioUnitario"), cat);
                Venta v = new Venta(); v.setIdVenta(idVenta);
                double subtotal = rs.getDouble("precioUnitario") * rs.getInt("cantidad");
                lista.add(new DetalleVenta(rs.getInt("idDetalle"), v, prod,
                        rs.getInt("cantidad"), subtotal));
            }
        } catch (SQLException e) {
            System.out.println("Error listarDetalle: " + e.getMessage());
        }
        return lista;
    }
}