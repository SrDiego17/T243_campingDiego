package es.unizar.eina.T243_camping.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ParcelaReservaRepository {
    private final ParcelaReservaDao PRDao;
    private final long TIMEOUT = 15000;

    public ParcelaReservaRepository(Application application) {
        CampingRoomDatabase db = CampingRoomDatabase.getDatabase(application);
        PRDao = db.parcelaReservaDao();
    }

    // Inserta una nueva relación parcela-reserva
    public long insert(ParcelaReserva parcelaReserva) {
        Future<Long> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> PRDao.insert(parcelaReserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    // Actualiza una relación existente
    public int update(ParcelaReserva parcelaReserva) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> PRDao.update(parcelaReserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    // Elimina una relación específica
    public int delete(ParcelaReserva parcelaReserva) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> PRDao.delete(parcelaReserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    // Consulta todas las parcelas asociadas a una reserva específica
    public LiveData<List<ParcelaReserva>> getParcelasByReserva(long reservaId) {
        return PRDao.getParcelasByReserva(reservaId);
    }

    // Consulta para verificar si una parcela está reservada en fechas específicas
    public LiveData<List<ParcelaReserva>> getParcelasReservadas(int parcelaId, long fechaEntrada, long fechaSalida) {
        return PRDao.getParcelasReservadas(parcelaId, fechaEntrada, fechaSalida);
    }

    // Consulta las parcelas disponibles para fechas específicas
    public LiveData<List<Parcela>> getParcelasDisponibles(long fechaEntrada, long fechaSalida) {
        return PRDao.getParcelasDisponibles(fechaEntrada, fechaSalida);
    }

    public void eliminarParcelasReserva(long reservaID) {
        CampingRoomDatabase.getDatabaseWriteExecutor().execute(() ->
                PRDao.eliminarParcelasReserva(reservaID)
        );
    }

    // ParcelaReservaRepository.java
    public LiveData<Parcela> getParcelaById(int parcelaId) {
        return PRDao.getParcelaById(parcelaId);
    }

}
