package Negocio;

import Datos.CategoriaDAO;
import Dominio.Categoria;
import java.util.List;

public class CategoriaNegocio {
    private final CategoriaDAO dao = new CategoriaDAO();

    public List<Categoria> listarTodas() {
        return dao.listarTodas(); //trae de la base de datos la lista de todas las categorias
    }

    public boolean guardar(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            return false;
        return dao.insertar(new Categoria(0, nombre.trim()));
    }

    public boolean actualizar(Categoria cat) {
        if (cat.getNombre() == null || cat.getNombre().trim().isEmpty())
            return false;
        return dao.actualizar(cat);
    }

    public boolean eliminar(int idCategoria) {

        return dao.eliminar(idCategoria);
    }
}