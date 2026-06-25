package Negocio;

import Datos.StockDAO;
import Dominio.Stock;
import java.util.List;

public class StockNegocio {
    private final StockDAO dao = new StockDAO();

    public List<Stock> listarTodos() {
        return dao.listarTodos();
    }

    public boolean actualizar(Stock stock) {
        if (stock.getCantidadDisponible() < 0) return false;
        if (stock.getStockMinimo() < 0) return false;
        return dao.actualizar(stock);
    }

    public boolean insertar(Stock stock) {
        return dao.insertar(stock);
    }
}