package es.unizar.eina.T243_camping.database;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase que gestiona el acceso la fuente de datos.
 * Interacciona con la base de datos a través de las clases CampingRoomDatabase y ParcelaDao.
 */
public class ParcelaRepository {

    private final ParcelaDao mParcelaDao;
    private LiveData<List<Parcela>> mAllParcelas;

    private final long TIMEOUT = 15000;

    /**
     * Constructor de ParcelaRepository utilizando el contexto de la aplicación para instanciar la base de datos.
     * Alternativamente, se podría estudiar la instanciación del repositorio con una referencia a la base de datos
     * siguiendo el ejemplo de
     * <a href="https://github.com/android/architecture-components-samples/blob/main/BasicSample/app/src/main/java/com/example/android/persistence/DataRepository.java">architecture-components-samples/.../persistence/DataRepository</a>
     */
    public ParcelaRepository(Application application) {
        CampingRoomDatabase db = CampingRoomDatabase.getDatabase(application);
        mParcelaDao = db.parcelaDao();
        mAllParcelas = mParcelaDao.getOrderedParcelas();
    }

    /** Devuelve un objeto de tipo LiveData con todas las notas.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Parcela>> getAllParcelas() {
        return mAllParcelas;
    }

    public LiveData<List<Parcela>> getOrderedParcelas(int orden) {
        switch (orden) {
            case 1:
                mAllParcelas = mParcelaDao.getOrderedParcelas();
                break;
            case 2:
                mAllParcelas = mParcelaDao.getOrderedParcelasOcupantes();
                break;
            case 3:
                mAllParcelas = mParcelaDao.getOrderedParcelasPrecio();
                break;
            default:
                Log.d("ParcelaRepository", "Orden no reconocido: " + orden);
                break;
        }
        return mAllParcelas;
    }

    /** Inserta una nota nueva en la base de datos
     * @param parcela La nota consta de: un título (parcela.getTitle()) no nulo (parcela.getTitle()!=null) y no vacío
     *             (parcela.getTitle().length()>0); y un cuerpo (parcela.getBody()) no nulo.
     * @return Si la nota se ha insertado correctamente, devuelve el identificador de la nota que se ha creado. En caso
     *         contrario, devuelve -1 para indicar el fallo.
     */
    public long insert(Parcela parcela) {
        /* Para que la App funcione correctamente y no lance una excepción, la modificación de la
         * base de datos se debe lanzar en un hilo de ejecución separado
         * (databaseWriteExecutor.submit). Para poder sincronizar la recuperación del resultado
         * devuelto por la base de datos, se puede utilizar un Future.
         */
        Future<Long> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.insert(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /** Actualiza una nota en la base de datos
     * @param parcela La nota que se desea actualizar y que consta de: un identificador (parcela.getId()) mayor que 0; un
     *             título (parcela.getTitle()) no nulo y no vacío; y un cuerpo (parcela.getBody()) no nulo.
     * @return Un valor entero con el número de filas modificadas: 1 si el identificador se corresponde con una nota
     *         previamente insertada; 0 si no existe previamente una nota con ese identificador, o hay algún problema
     *         con los atributos.
     */
    public int update(Parcela parcela) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.update(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }


    /** Elimina una nota en la base de datos.
     * @param parcela Objeto nota cuyo atributo identificador (parcela.getId()) contiene la clave primaria de la nota que se
     *             va a eliminar de la base de datos. Se debe cumplir: parcela.getId() > 0.
     * @return Un valor entero con el número de filas eliminadas: 1 si el identificador se corresponde con una nota
     *         previamente insertada; 0 si no existe previamente una nota con ese identificador o el identificador no es
     *         un valor aceptable.
     */
    public int delete(Parcela parcela) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.delete(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    // Obtener nombre de la parcela
    public String getNombreParcela(int parcelaId) {
        Future<String> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.getNombreParcela(parcelaId));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaRepository", "Error obteniendo nombre: " + ex.getMessage());
            return null;
        }
    }

    // Obtener el número máximo de ocupantes
    public int getMaxOcupantesParcela(int parcelaId) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.getMaxOcupantesParcela(parcelaId));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaRepository", "Error obteniendo maxOcupantes: " + ex.getMessage());
            return -1;
        }
    }

    // Obtener el precio de la parcela
    public double getPrecioParcela(int parcelaId) {
        Future<Double> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.getPrecioParcela(parcelaId));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaRepository", "Error obteniendo precio: " + ex.getMessage());
            return -1;
        }
    }
}
