package Datos;

import Dominio.Categoria;
import java.sql.*;
import java.util.*;

public class CategoriaDAO {
    public List<Categoria> listarTodas() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT idCategoria, nombre FROM categoria";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("idCategoria"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Categoria cat) {
        String sql = "INSERT INTO categoria (nombre) VALUES (?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cat.getNombre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean actualizar(Categoria cat) {
        String sql = "UPDATE categoria SET nombre=? WHERE idCategoria=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cat.getNombre());
            ps.setInt(2, cat.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean eliminar(int idCategoria) {
        String sql = "DELETE FROM categoria WHERE idCategoria=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
