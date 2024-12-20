package es.unizar.eina.T243_camping.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "parcela_reserva", primaryKeys = {"reservaID", "parcelaID"})
public class ParcelaReserva {

    @ColumnInfo (name = "reservaID")
    long reservaID;

    @ColumnInfo (name = "parcelaID")
    int parcelaID;

    @ColumnInfo (name = "numOcupantes")
    int numOcupantes;


    public ParcelaReserva(long reservaID, int parcelaID, int numOcupantes) {
        this.reservaID = reservaID;
        this.parcelaID = parcelaID;
        this.numOcupantes = numOcupantes;
    }

    /** Devuelve el identificador de la nota */
    public long getReservaID(){
        return this.reservaID;
    }

    /** Permite actualizar el identificador de una nota */
    public void setReservaID(long id) {
        this.reservaID = id;
    }

    /** Devuelve el identificador de la nota */
    public int getParcelaID(){
        return this.parcelaID;
    }

    /** Permite actualizar el identificador de una nota */
    public void setParcelaID(int id) {
        this.parcelaID = id;
    }

    /** Devuelve el nombre de la nota */
    public int getNumOcupantes(){
        return this.numOcupantes;
    }

    /** Permite actualizar el identificador de una nota */
    public void setNumOcupantes(int numOcupantes) {
        this.numOcupantes = numOcupantes;
    }
}
