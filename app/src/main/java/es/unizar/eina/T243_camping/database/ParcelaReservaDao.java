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
public interface ParcelaReservaDao {

    // Inserta una relación Parcela-Reserva
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ParcelaReserva parcelaReserva);

    // Actualiza una relación existente
    @Update
    int update(ParcelaReserva parcelaReserva);

    // Elimina una relación específica
    @Delete
    int delete(ParcelaReserva parcelaReserva);

    // Obtiene todas las parcelas asociadas a una reserva específica
    @Query("SELECT * FROM parcela_reserva WHERE reservaID = :reservaId")
    LiveData<List<ParcelaReserva>> getParcelasByReserva(long reservaId);

    // Consulta para verificar si una parcela está reservada en fechas específicas
    @Query("SELECT * FROM parcela_reserva " +
            "INNER JOIN reserva ON parcela_reserva.reservaID = reserva.id " +
            "WHERE parcela_reserva.parcelaID = :parcelaId " +
            "AND (reserva.fecha_entrada <= :fechaSalida AND reserva.fecha_salida >= :fechaEntrada)")
    LiveData<List<ParcelaReserva>> getParcelasReservadas(int parcelaId, long fechaEntrada, long fechaSalida);

    // Obtiene todas las parcelas NO reservadas en un rango de fechas
    @Query("SELECT * FROM parcela " +
            "WHERE parcela.id NOT IN (" +
            "    SELECT parcela_reserva.parcelaID " +
            "    FROM parcela_reserva " +
            "    INNER JOIN reserva ON parcela_reserva.reservaID = reserva.id " +
            "    WHERE (reserva.fecha_entrada <= :fechaSalida AND reserva.fecha_salida >= :fechaEntrada)" +
            ")")
    LiveData<List<Parcela>> getParcelasDisponibles(long fechaEntrada, long fechaSalida);


    @Query("DELETE FROM parcela_reserva WHERE reservaID = :reservaID")
    void eliminarParcelasReserva(long reservaID);

    @Query("SELECT * FROM parcela WHERE id = :parcelaId")
    LiveData<Parcela> getParcelaById(int parcelaId);

}
