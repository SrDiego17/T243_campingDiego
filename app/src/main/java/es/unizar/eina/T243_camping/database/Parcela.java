package es.unizar.eina.T243_camping.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Clase anotada como entidad que representa una nota y que consta de título y cuerpo */
@Entity(tableName = "parcela")
public class Parcela {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "maxOcupantes")
    private int maxOcupantes;

    @ColumnInfo(name = "precioPorOcupante")
    private double precioPorOcupante;


    public Parcela(@NonNull String nombre, String descripcion, int maxOcupantes, double precioPorOcupante) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.maxOcupantes = maxOcupantes;
        this.precioPorOcupante = precioPorOcupante;
    }

    /** Devuelve el identificador de la nota */
    public int getId(){
        return this.id;
    }

    /** Permite actualizar el identificador de una nota */
    public void setId(int id) {
        this.id = id;
    }

    /** Devuelve el nombre de la nota */
    public String getNombre(){
        return this.nombre;
    }

    public void setNombre(String nombre) { this. nombre = nombre; }

    /** Devuelve el cuerpo de la nota */
    public String getDescripcion(){ return this.descripcion; }

    public int getMaxOcupantes() { return this.maxOcupantes; }

    public void setMaxOcupantes(int maxOcupantes) { this.maxOcupantes = maxOcupantes; }

    public double getPrecioPorOcupante() { return this.precioPorOcupante; }

    public void setPrecioPorOcupante(double precioPorOcupante) { this.precioPorOcupante = precioPorOcupante; }

}
