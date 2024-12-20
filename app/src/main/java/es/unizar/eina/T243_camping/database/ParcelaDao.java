package es.unizar.eina.T243_camping.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** Definición de un Data Access Object para las notas */
@Dao
public interface ParcelaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Parcela parcela);

    @Update
    int update(Parcela parcela);

    @Delete
    int delete(Parcela parcela);

    @Query("DELETE FROM parcela")
    void deleteAll();

    @Query("SELECT * FROM parcela ORDER BY nombre ASC")
    LiveData<List<Parcela>> getOrderedParcelas();

    @Query("SELECT * FROM parcela ORDER BY maxOcupantes ASC")
    LiveData<List<Parcela>> getOrderedParcelasOcupantes();

    @Query("SELECT * FROM parcela ORDER BY precioPorOcupante ASC")
    LiveData<List<Parcela>> getOrderedParcelasPrecio();

    // Devuelve el nombre de la parcela por su ID
    @Query("SELECT nombre FROM parcela WHERE id = :parcelaId")
    String getNombreParcela(int parcelaId);

    // Devuelve el número máximo de ocupantes de la parcela por su ID
    @Query("SELECT maxOcupantes FROM parcela WHERE id = :parcelaId")
    int getMaxOcupantesParcela(int parcelaId);

    // Devuelve el precio de la parcela por su ID
    @Query("SELECT precioPorOcupante FROM parcela WHERE id = :parcelaId")
    double getPrecioParcela(int parcelaId);
}

