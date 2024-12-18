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
}

