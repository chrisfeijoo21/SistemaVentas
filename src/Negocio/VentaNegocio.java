package Negocio;

import Datos.DetalleVentaDAO;
import Datos.StockDAO;
import Datos.VentaDAO;
import Dominio.DetalleVenta;
import Dominio.Stock;
import Dominio.Venta;
import java.util.Date;
import java.util.List;

public class VentaNegocio {
    private final VentaDAO ventaDAO = new VentaDAO();
    private final DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
    private final StockDAO stockDAO = new StockDAO();

    public List<Venta> listarTodas() {
        return ventaDAO.listarTodas();
    }

    public boolean registrarVenta(List<DetalleVenta> detalles) {
        if (detalles == null || detalles.isEmpty())
            return false;
        double total = detalles.stream()
                .mapToDouble(d -> d.getProducto().getPrecio() * d.getCantidad())
                .sum();
        Venta venta = new Venta(0, new Date(), total);
        int idVenta = ventaDAO.insertar(venta);
        if (idVenta < 0) return false;
        venta.setIdVenta(idVenta);
        List<Stock> stocks = stockDAO.listarTodos();
        for (DetalleVenta d : detalles) {
            d.setVenta(venta);
            detalleDAO.insertar(d);
            for (Stock s : stocks) {
                if (s.getProducto().getIdProducto() == d.getProducto().getIdProducto()) {
                    s.setCantidadDisponible(s.getCantidadDisponible() - d.getCantidad());
                    stockDAO.actualizar(s);
                    break;
                }
            }
        }
        return true;
    }

    public boolean anularVenta(int idVenta) {
        // 1. Obtener detalles y devolver stock
        List<DetalleVenta> detalles = detalleDAO.listarPorVenta(idVenta);
        List<Stock> stocks = stockDAO.listarTodos();
        for (DetalleVenta d : detalles) {
            for (Stock s : stocks) {
                if (s.getProducto().getIdProducto() == d.getProducto().getIdProducto()) {
                    s.setCantidadDisponible(s.getCantidadDisponible() + d.getCantidad());
                    stockDAO.actualizar(s);
                    break;
                }
            }
        }
        // 2. Eliminar detalles (FK) luego la venta
        ventaDAO.eliminarDetalles(idVenta);
        return ventaDAO.eliminar(idVenta);
    }

    public List<DetalleVenta> listarDetalles(int idVenta) {
        return detalleDAO.listarPorVenta(idVenta);
    }

    public List<Object[]> reportePorDia() {
        return ventaDAO.reportePorDia();
    }

    public List<Object[]> reportePorProducto() {
        return ventaDAO.reportePorProducto();
    }
}