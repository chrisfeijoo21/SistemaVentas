package Negocio;

import Datos.ProductoDAO;
import Dominio.Categoria;
import Dominio.producto;
import java.util.List;

public class ProductoNegocio {
    private final ProductoDAO dao = new ProductoDAO();

    public List<producto> listarTodos() {
        return dao.listarTodos();
    }

    public boolean guardar(String nombre, String descripcion, boolean estado, double precio, Categoria categoria) {
        if (nombre == null || nombre.trim().isEmpty())
            return false;
        if (precio <= 0)
            return false;
        if (categoria == null)
            return false;
        producto p = new producto(0, nombre.trim(), descripcion.trim(), estado, precio, categoria);
        return dao.insertar(p);
    }

    public boolean actualizar(producto p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) return false;
        if (p.getPrecio() <= 0) return false;
        return dao.actualizar(p);
    }

    public boolean eliminar(int idProducto) {
        return dao.eliminar(idProducto);
    }
}