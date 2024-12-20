package es.unizar.eina.T243_camping.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "reserva")
public class Reserva {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    long ID;

    @ColumnInfo(name = "nombre_cliente")
    String nombreCliente;

    @ColumnInfo(name = "telefono_cliente")
    String telefonoCliente;

    @ColumnInfo(name = "fecha_entrada")
    Date fechaEntrada;

    @ColumnInfo(name = "fecha_salida")
    Date fechaSalida;

    @ColumnInfo(name = "precio_total")
    double precioTotal;

    public Reserva(String nombreCliente, String telefonoCliente, Date fechaEntrada, Date fechaSalida, double precioTotal) {
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.precioTotal = precioTotal;
    }

    public long getID() { return this.ID; }
    public void setID(long id) { this.ID = id; }

    public String getNombreCliente() { return this.nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getTelefonoCliente() { return this.telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public String getFechaEntrada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.fechaEntrada);
    }
    public void setFechaEntrada(Date fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public String getFechaSalida() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.fechaSalida);
    }
    public void setFechaSalida(Date fechaSalida) { this.fechaSalida = fechaSalida; }

    public void setPrecioTotal(double precioTotal) {this.precioTotal = precioTotal;}

    public double getPrecioTotal() {return this.precioTotal;}

}
