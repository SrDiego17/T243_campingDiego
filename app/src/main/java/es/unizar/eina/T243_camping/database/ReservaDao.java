package es.unizar.eina.T243_camping.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReservaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Reserva reserva);

    @Update
    int update(Reserva reserva);

    @Delete
    int delete(Reserva reserva);

    @Query("DELETE FROM reserva")
    void deleteAll();

    @Query("SELECT * FROM reserva ORDER BY nombre_cliente ASC")
    LiveData<List<Reserva>> getOrderedReservas();

    @Query("SELECT * FROM reserva ORDER BY telefono_cliente ASC")
    LiveData<List<Reserva>> getOrderedReservasTlf();

    @Query("SELECT * FROM reserva ORDER BY fecha_entrada ASC")
    LiveData<List<Reserva>> getOrderedReservasEntrada();
}
