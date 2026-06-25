package Dominio;
import java.util.Date;

public class Venta {
    private int idVenta;
    private Date fecha;
    private double total;

    public Venta(){
    }
    public Venta(int idVenta, Date fecha, double total){
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.total = total;
    }

    public int getIdVenta(){

        return idVenta;
    }
    public void setIdVenta(int idVenta){

        this.idVenta = idVenta;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha) {

        this.fecha = fecha;
    }

    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
    }
}
