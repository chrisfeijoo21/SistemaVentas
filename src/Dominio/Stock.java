package Dominio;
import java.time.LocalDate;

public class Stock {
    private int idStock;
    private producto producto;
    private int cantidadDisponible;
    private int stockMinimo;
    private LocalDate ultimaActualizacion;

    public Stock() {}

    public Stock(int idStock, producto producto, int cantidadDisponible,
                 int stockMinimo, LocalDate ultimaActualizacion) {
        this.idStock = idStock;
        this.producto = producto;
        this.cantidadDisponible = cantidadDisponible;
        this.stockMinimo = stockMinimo;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public int getIdStock() {
        return idStock;
    }
    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public producto getProducto() {
        return producto;
    }
    public void setProducto(producto producto) {
        this.producto = producto;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
    public void setCantidadDisponible(int c) {
        this.cantidadDisponible = c;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }
    public void setStockMinimo(int s) {
        this.stockMinimo = s;
    }

    public LocalDate getUltimaActualizacion() {
        return ultimaActualizacion;
    }
    public void setUltimaActualizacion(LocalDate u) {
        this.ultimaActualizacion = u;
    }
}