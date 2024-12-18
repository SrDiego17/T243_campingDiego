package es.unizar.eina.T243_camping.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Parcela.class, Reserva.class, ParcelaReserva.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class CampingRoomDatabase extends RoomDatabase {

    public abstract ParcelaDao parcelaDao();

    public abstract ReservaDao reservaDao();

    public abstract ParcelaReservaDao parcelaReservaDao();

    private static volatile CampingRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static CampingRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CampingRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CampingRoomDatabase.class, "camping_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            /*databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more notes, just add them.
                ParcelaDao mParcelaDao = INSTANCE.parcelaDao();
                ReservaDao mReservaDao = INSTANCE.reservaDao();
                ParcelaReservaDao mParcelaReservaDao = INSTANCE.parcelaReservaDao();
                mParcelaDao.deleteAll();
                mReservaDao.deleteAll();

                Parcela parcela = new Parcela("Parcela de Diego", "Persona 5 Royale", 5, 20.0);
                mParcelaDao.insert(parcela);
                parcela = new Parcela("Parcela de Nico", "Hollow Knight", 4, 17.0);
                mParcelaDao.insert(parcela);

                Reserva reserva = new Reserva("Diego", "+34618565124", new Date(125,1,1), new Date(125, 1,7));
                mReservaDao.insert(reserva);
                reserva = new Reserva("Nico", "+34646613324", new Date(125,1,1), new Date(125, 1,7));
                mReservaDao.insert(reserva);

                ParcelaReserva parcelaReserva = new ParcelaReserva(0, 0, 5);
                mParcelaReservaDao.insert(parcelaReserva);
                parcelaReserva = new ParcelaReserva(1,1,4);
                mParcelaReservaDao.insert(parcelaReserva);
            });

             */
        }
    };

}
