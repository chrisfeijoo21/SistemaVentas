package Datos;

import Dominio.Categoria;
import Dominio.producto;
import java.sql.*;
import java.util.*;

public class ProductoDAO {

        public List<producto> listarTodos() {
            List<producto> lista = new ArrayList<>();
            String sql = "SELECT p.idProducto, p.nombre, p.descripcion, p.estado, p.precio, " +
                    "c.idCategoria, c.nombre AS nombreCategoria " +
                    "FROM producto p JOIN categoria c ON p.idCategoria = c.idCategoria";
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria cat = new Categoria(
                            rs.getInt("idCategoria"), rs.getString("nombreCategoria"));
                    lista.add(new producto(
                            rs.getInt("idProducto"), rs.getString("nombre"),
                            rs.getString("descripcion"), rs.getBoolean("estado"),
                            rs.getDouble("precio"), cat));
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
            return lista;
        }

        public boolean insertar(producto prod) {
            String sql = "INSERT INTO producto (nombre, descripcion, estado, precio, idCategoria) VALUES (?,?,?,?,?)";
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, prod.getNombre());
                ps.setString(2, prod.getDescripcion());
                ps.setBoolean(3, prod.isEstado());
                ps.setDouble(4, prod.getPrecio());
                ps.setInt(5, prod.getCategoria().getIdCategoria());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { return false; }
        }

        public boolean actualizar(producto prod) {
            String sql = "UPDATE producto SET nombre=?, descripcion=?, estado=?, precio=?, idCategoria=? WHERE idProducto=?";
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, prod.getNombre());
                ps.setString(2, prod.getDescripcion());
                ps.setBoolean(3, prod.isEstado());
                ps.setDouble(4, prod.getPrecio());
                ps.setInt(5, prod.getCategoria().getIdCategoria());
                ps.setInt(6, prod.getIdProducto());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { return false; }
        }

        public boolean eliminar(int idProducto) {
            String sql = "DELETE FROM producto WHERE idProducto=?";
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idProducto);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { return false; }
        }
}
