package es.unizar.eina.T243_camping.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.T243_camping.database.Parcela;
import es.unizar.eina.T243_camping.database.ParcelaRepository;

public class ParcelaViewModel extends AndroidViewModel {

    private ParcelaRepository mRepository;

    private final LiveData<List<Parcela>> mAllParcelas;

    public ParcelaViewModel(Application application) {
        super(application);
        mRepository = new ParcelaRepository(application);
        mAllParcelas = mRepository.getAllParcelas();
    }

    LiveData<List<Parcela>> getAllParcelas() { return mAllParcelas; }

    public long insert(Parcela parcela) { return mRepository.insert(parcela); }

    public void update(Parcela parcela) { mRepository.update(parcela); }
    public void delete(Parcela parcela) { mRepository.delete(parcela); }
    public LiveData<List<Parcela>> getParcelasOrdenadas(int orden) {
        return mRepository.getOrderedParcelas(orden);
    }

    // Obtener el nombre de la parcela
    public String getNombreParcela(int parcelaId) {
        return mRepository.getNombreParcela(parcelaId);
    }

    // Obtener el número máximo de ocupantes de la parcela
    public int getMaxOcupantesParcela(int parcelaId) {
        return mRepository.getMaxOcupantesParcela(parcelaId);
    }

    // Obtener el precio de la parcela
    public double getPrecioParcela(int parcelaId) {
        return mRepository.getPrecioParcela(parcelaId);
    }
}
