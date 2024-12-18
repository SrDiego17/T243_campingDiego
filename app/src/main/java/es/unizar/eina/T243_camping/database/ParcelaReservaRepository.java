package es.unizar.eina.T243_camping.database;

import android.app.Application;

public class ParcelaReservaRepository {
    private final ParcelaReservaDao PRDao;

    public ParcelaReservaRepository(Application application) {
        CampingRoomDatabase db = CampingRoomDatabase.getDatabase(application);
        PRDao = db.parcelaReservaDao();
    }
    // METER UPDATE, INSERT, DELETE
}
