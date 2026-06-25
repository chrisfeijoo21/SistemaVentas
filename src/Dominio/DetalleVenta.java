package Dominio;

public class DetalleVenta {
    private int idDetalle;
    private Venta venta;
    private producto Producto;
    private int cantidad;
    private double  subtotal;

    public DetalleVenta(){

    }
    public DetalleVenta(int idDetalle,Venta venta, producto Producto, int cantidad, double subtotal){
        this.idDetalle = idDetalle;
        this.venta = venta;
        this.Producto= Producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }
    public int getIdDetalle(){
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public producto getProducto() {
        return Producto;
    }

    public void setProducto(producto producto) {
        Producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
