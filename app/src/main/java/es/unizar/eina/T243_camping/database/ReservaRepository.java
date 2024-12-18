package es.unizar.eina.T243_camping.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ReservaRepository {
    private final ReservaDao mReservaDao;

    private final long TIMEOUT = 15000;

    private final LiveData<List<Reserva>> mAllReservas;
    public ReservaRepository(Application application) {
        CampingRoomDatabase db = CampingRoomDatabase.getDatabase(application);
        mReservaDao = db.reservaDao();
        mAllReservas = mReservaDao.getOrderedReservas();
    }

    public LiveData<List<Reserva>> getAllReservas() { return mAllReservas; }

    public LiveData<List<Reserva>> getOrderedReservas(String orden) {
        LiveData<List<Reserva>> reservasOrdenadas = mAllReservas;
        switch (orden) {
            case "nombre":
                reservasOrdenadas = mReservaDao.getOrderedReservas();
                break;
            case "tlf":
                reservasOrdenadas = mReservaDao.getOrderedReservasTlf();
                break;
            case "fecha":
                reservasOrdenadas = mReservaDao.getOrderedReservasEntrada();
                break;
        }
        return reservasOrdenadas;
    }

    public long insert(Reserva reserva) {
        Future<Long> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mReservaDao.insert(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    public int update(Reserva reserva) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mReservaDao.update(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    public int delete(Reserva reserva) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mReservaDao.delete(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }
}
