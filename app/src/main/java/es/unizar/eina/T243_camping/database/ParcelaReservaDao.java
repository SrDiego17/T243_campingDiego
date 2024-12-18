package es.unizar.eina.T243_camping.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface ParcelaReservaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(ParcelaReserva parcelaReserva);

    @Update
    int update(ParcelaReserva parcelaReserva);

    @Delete
    int delete(ParcelaReserva parcelaReserva);

    @Query("DELETE FROM parcela_reserva")
    void deleteAll();

    @Query("SELECT parcelaID FROM parcela_reserva WHERE reservaID = :reservaId")
    List<Long> selectParcelasIdByReservaId(long reservaId);

    @Query("SELECT * FROM parcela WHERE id IN (SELECT parcelaID FROM parcela_reserva WHERE reservaID = :reservaId)")
    List<Parcela> selectParcelasByReservaId(long reservaId);

    @Query("SELECT * FROM reserva WHERE id IN (SELECT reservaID FROM parcela_reserva WHERE parcelaID = :parcelaId)")
    List<Reserva> selectReservasByParcelaId(long parcelaId);
}
