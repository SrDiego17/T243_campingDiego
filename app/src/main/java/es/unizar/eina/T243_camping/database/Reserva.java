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

    public Reserva(String nombreCliente, String telefonoCliente, Date fechaEntrada, Date fechaSalida) {
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
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

    public double calcularPrecio() {
        /*
        * 1. Obtener todas las parcelas asociadas a esta reserva      ParcelaReservaDAO
        * 2. Obtener el precio por ocupante de cada parcela asociada a esta reserva    ParcelaDAO
        * 3. Obtener el numero de noches que se pasan en la parcela     Reserva
        * 4. Calcular el precio total de la reserva por noche(suma precio por noche todas parcelas)     Ya tenemos datos
        * 5. Calcular el precio total de la reserva(precio total por noche multiplicado numero noches)  Ya tenemos datos
        * */
        return 0.0;
    }

    public boolean verificarCorreccion() {
        /*
        * 1. Verificar que fecha de salida sea posterior a fecha de entrada
        * 2. Obtener todas las parcelas asociadas a esta reserva      ParcelaReservaDAO
        * 3. Obtener el maximo de ocupantes de cada parcela asociada a esta reserva    ParcelaDAO
        * 4. Verificar que el numero de ocupantes que se ha introducido en cada parcela no sea mayor
        *    que el maximo de ocupantes de la parcela
        * 5. Comprobar que, para cada parcela reservada en esta reserva, no haya otra reserva
        *    existente que la reserve en esas fechas(revisar solapes). Esto se puede hacer a nivel de sql con las fechas puestas por el user, mirar como
        * */
        boolean correccion = false;
        correccion = this.fechaEntrada.before(this.fechaSalida);
        return correccion;
    }
}
