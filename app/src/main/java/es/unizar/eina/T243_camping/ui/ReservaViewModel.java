    package es.unizar.eina.T243_camping.ui;

    import android.app.Application;

    import androidx.lifecycle.AndroidViewModel;
    import androidx.lifecycle.LiveData;

    import java.util.List;

    import es.unizar.eina.T243_camping.database.Reserva;
    import es.unizar.eina.T243_camping.database.ReservaRepository;

    public class ReservaViewModel extends AndroidViewModel {

        private ReservaRepository mRepository;

        private final LiveData<List<Reserva>> mAllReservas;

        public ReservaViewModel(Application application) {
            super(application);
            mRepository = new ReservaRepository(application);
            mAllReservas = mRepository.getAllReservas();
        }

        LiveData<List<Reserva>> getAllReservas() { return mAllReservas; }

        public long insert(Reserva reserva) { return mRepository.insert(reserva); }

        public void update(Reserva reserva) { mRepository.update(reserva); }

        public void delete(Reserva reserva) { mRepository.delete(reserva); }
    }
