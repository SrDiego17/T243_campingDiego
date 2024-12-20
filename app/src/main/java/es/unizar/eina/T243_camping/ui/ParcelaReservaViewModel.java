package es.unizar.eina.T243_camping.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.T243_camping.database.Parcela;
import es.unizar.eina.T243_camping.database.ParcelaReserva;
import es.unizar.eina.T243_camping.database.ParcelaReservaRepository;

public class ParcelaReservaViewModel extends AndroidViewModel {

    private final ParcelaReservaRepository repository;

    // Constructor para inicializar el repositorio
    public ParcelaReservaViewModel(@NonNull Application application) {
        super(application);
        repository = new ParcelaReservaRepository(application);
    }

    // Operación de inserción
    public long insert(ParcelaReserva parcelaReserva) {
        return repository.insert(parcelaReserva);
    }

    // Operación de actualización
    public void update(ParcelaReserva parcelaReserva) {
        repository.update(parcelaReserva);
    }

    // Operación de eliminación
    public void delete(ParcelaReserva parcelaReserva) {
        repository.delete(parcelaReserva);
    }

    // Obtener parcelas asociadas a una reserva específica
    public LiveData<List<ParcelaReserva>> getParcelasByReserva(long reservaId) {
        return repository.getParcelasByReserva(reservaId);
    }

    // Consultar si una parcela está reservada en un rango de fechas
    public LiveData<List<ParcelaReserva>> getParcelasReservadas(int parcelaId, long fechaEntrada, long fechaSalida) {
        return repository.getParcelasReservadas(parcelaId, fechaEntrada, fechaSalida);
    }

    // Consultar parcelas disponibles para fechas específicas
    public LiveData<List<Parcela>> getParcelasDisponibles(long fechaEntrada, long fechaSalida) {
        return repository.getParcelasDisponibles(fechaEntrada, fechaSalida);
    }

    public void eliminarParcelasReserva(long reservaID) {
       repository.eliminarParcelasReserva(reservaID);
    }

    public LiveData<Parcela> getParcelaById(int parcelaId) {
        return repository.getParcelaById(parcelaId);
    }
}
