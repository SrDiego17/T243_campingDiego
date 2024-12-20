package es.unizar.eina.T243_camping.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import es.unizar.eina.T243_camping.R;
import es.unizar.eina.T243_camping.database.CampingRoomDatabase;
import es.unizar.eina.T243_camping.database.ParcelaReserva;
import es.unizar.eina.T243_camping.database.Parcela;
import es.unizar.eina.T243_camping.database.Reserva;

public class ListarParcelasReserva extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParcelaViewModel parcelViewModel;
    private ParcelaReservaViewModel parcelaReservaViewModel;
    private ReservaViewModel reservaViewModel;
    private ParcelaReservaListAdapter adapter;
    private List<ParcelaReserva> listaParcelasReserva = new ArrayList<>();
    private List<Parcela> listaParcelas = new ArrayList<>();

    // Datos recibidos del Intent
    public static final String RESERVA_FECHA_ENTRADA = "fecha_entrada";
    public static final String RESERVA_FECHA_SALIDA = "fecha_salida";
    public static final String RESERVA_NOMBRE_CLIENTE = "nombre_cliente";
    public static final String RESERVA_TELEFONO_CLIENTE = "telefono_cliente";
    public static final String RESERVA_ID = "id";

    private long fechaEntrada;
    private long fechaSalida;
    private String nombreCliente;
    private String telefonoCliente;
    private long reservaID;

    private boolean validacionCorrecta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listarparcelasreserva);

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerviewParcelasReserva);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Inicializar ViewModels
        parcelaReservaViewModel = new ViewModelProvider(this).get(ParcelaReservaViewModel.class);
        reservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);  // CORRECCIÓN
        ParcelaViewModel parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);
        parcelViewModel = parcelaViewModel;
        // Configurar el adaptador
        adapter = new ParcelaReservaListAdapter (new ParcelaReservaListAdapter.ParcelaReservaDiff(), parcelaViewModel);
        recyclerView.setAdapter(adapter);

        // Inicializar el botón de guardar
        Button buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(v -> {
            if(validarDatos() == true) {
                saveParcelasReserva();
            }
        });

        // Obtener datos del Intent
        fechaEntrada = getIntent().getLongExtra(RESERVA_FECHA_ENTRADA, -1);
        fechaSalida = getIntent().getLongExtra(RESERVA_FECHA_SALIDA, -1);
        nombreCliente = getIntent().getStringExtra(RESERVA_NOMBRE_CLIENTE);
        telefonoCliente = getIntent().getStringExtra(RESERVA_TELEFONO_CLIENTE);
        reservaID = getIntent().getLongExtra(RESERVA_ID, -1);
        Log.d("ID DE RESERVA", "La reserva tiene id: " + reservaID);


        // Validación de datos recibidos
        if (fechaEntrada == -1 || fechaSalida == -1 || nombreCliente == null || telefonoCliente == null) {
            Toast.makeText(this, "Datos de la reserva inválidos", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Obtener las parcelas disponibles
        cargarParcelasDisponibles();
    }


    private void cargarParcelasDisponibles() {
        parcelaReservaViewModel.getParcelasDisponibles(fechaEntrada, fechaSalida).observe(this, parcelasDisponibles -> {
            parcelaReservaViewModel.getParcelasByReserva(reservaID).observe(this, parcelasReservadas -> {

                List<Parcela> parcelasUnidas = new ArrayList<>(parcelasDisponibles);
                AtomicInteger consultasPendientes = new AtomicInteger(0);
                List<ParcelaReserva> parcelasReservasDisponibles = new ArrayList<>();

                // Mostrar parcelas disponibles
                for (Parcela parcela : parcelasDisponibles) {
                    Log.d("ParcelasDisponibles", "Parcela ID: " + parcela.getId());
                    // Parcela disponible sin reserva previa
                    parcelasReservasDisponibles.add(new ParcelaReserva(
                            reservaID,
                            parcela.getId(),
                            0  // Asume 0 ocupantes si no estaba reservada
                    ));
                }

                // Agregar parcelas reservadas que no están en la lista de disponibles
                for (ParcelaReserva reserva : parcelasReservadas) {
                    boolean yaIncluida = false;
                    for (Parcela parcela : parcelasDisponibles) {
                        if (parcela.getId() == reserva.getParcelaID()) {
                            yaIncluida = true;
                            break;
                        }
                    }

                    if (!yaIncluida) {
                        consultasPendientes.incrementAndGet();  // Incrementa antes de consultar
                        parcelaReservaViewModel.getParcelaById(reserva.getParcelaID()).observe(this, parcelaReservada -> {
                            if (parcelaReservada != null) {
                                Log.d("Poblar parcelas", "VAMO A METERLO " + reserva.getParcelaID());
                                parcelasUnidas.add(parcelaReservada);
                                parcelasReservasDisponibles.add(new ParcelaReserva(
                                        reservaID,
                                        reserva.getParcelaID(),
                                        reserva.getNumOcupantes() // Recupera el número correcto de ocupantes
                                ));
                            }
                            if (consultasPendientes.decrementAndGet() == 0) {
                                procesarParcelas(parcelasUnidas, parcelasReservasDisponibles);
                            }
                        });
                    }
                }

                if (consultasPendientes.get() == 0) {
                    procesarParcelas(parcelasUnidas, parcelasReservasDisponibles);
                }
            });
        });
    }

    private void procesarParcelas(List<Parcela> parcelasUnidas, List<ParcelaReserva> parcelasReservasDisponibles) {
        // Registrar cada ID de parcela unida
        for (Parcela parcela : parcelasUnidas) {
            Log.d("parcelasUnidas", "ParcelaUnida ID: " + parcela.getId());
        }

        listaParcelas = parcelasUnidas;
        listaParcelasReserva = parcelasReservasDisponibles;
        adapter.submitList(new ArrayList<>(parcelasReservasDisponibles));

        Log.d("ListarParcelasReserva", "Parcelas cargadas: " + parcelasReservasDisponibles.size());
    }

    private double calcularPrecio() {
        double precioTotal = 0.0;

        for (ParcelaReserva parcelaReserva : listaParcelasReserva) {
            int parcelaId = parcelaReserva.getParcelaID();
            int numOcupantes = parcelaReserva.getNumOcupantes();

            // Consulta el precio de la parcela
            double precioPorNoche = parcelViewModel.getPrecioParcela(parcelaId);

            // Calcula el número de noches
            long diff = fechaSalida - fechaEntrada;
            long numeroNoches = diff / (1000 * 60 * 60 * 24);

            // Suma al precio total
            precioTotal += precioPorNoche * numOcupantes * numeroNoches;
        }

        return precioTotal;
    }



    private boolean validarDatos(){
        CampingRoomDatabase.getDatabaseWriteExecutor().execute(() -> {
            // Comprobar que todos los campos del número de ocupantes sean válidos
            if (adapter.getCurrentList() == null || adapter.getCurrentList().isEmpty()) {
                runOnUiThread(() -> Toast.makeText(this, "No hay parcelas seleccionadas", Toast.LENGTH_SHORT).show());
                validacionCorrecta = false;
            } else {
                validacionCorrecta = true;
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    ParcelaReservaViewHolder holder = (ParcelaReservaViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    if (holder != null) {
                        String ocupantesStr = holder.parcelaOcupantesEditText.getText().toString().trim();
                        if (!ocupantesStr.isEmpty()) {
                            int numOcupantes = Integer.parseInt(ocupantesStr);
                            int ocupantesMax = Integer.parseInt(holder.parcelaMaxOcupantesTextView.getText().toString().trim());
                            if (numOcupantes < 0 || numOcupantes > ocupantesMax) {
                                validacionCorrecta = false;
                                runOnUiThread(() -> Toast.makeText(this, "Número de ocupantes incorrecto en una parcela", Toast.LENGTH_SHORT).show());
                                break; // Detener la validación si un campo no es correcto
                            }
                        } else {
                            validacionCorrecta = false;
                            runOnUiThread(() -> Toast.makeText(this, "Debe rellenar todos los campos de ocupantes", Toast.LENGTH_SHORT).show());
                            break;
                        }
                    }
                }
            }
        });
        return validacionCorrecta;
    }

    /**
     * Método para guardar la reserva y sus parcelas asociadas
     */
    private void saveParcelasReserva() {
        if (listaParcelasReserva.isEmpty()) {
            Toast.makeText(this, "No hay parcelas seleccionadas.", Toast.LENGTH_LONG).show();
            return;
        }
        if(validacionCorrecta) {
            long reservaId;
            //Log.d("ID DE RESERVA", "La reserva tiene id: " + reservaID);
            if(reservaID == -1) {
                // Crear y guardar la nueva reserva
                Reserva nuevaReserva = new Reserva(
                        nombreCliente,
                        telefonoCliente,
                        new Date(fechaEntrada),
                        new Date(fechaSalida),
                        0
                );

                // Insertar la reserva y obtener su ID
                reservaId = reservaViewModel.insert(nuevaReserva);

            } else {
                parcelaReservaViewModel.eliminarParcelasReserva(reservaID);
                Reserva reservaModificada = new Reserva(
                        nombreCliente,
                        telefonoCliente,
                        new Date(fechaEntrada),
                        new Date(fechaSalida),
                        0
                );
                reservaModificada.setID(reservaID);
                reservaViewModel.update(reservaModificada);
                reservaId = reservaID;
            }
            //Log.d("ListarParcelasReserva", "Reserva creada con ID: " + reservaId);

            // Procesar parcelas seleccionadas
            for (int i = 0; i < adapter.getItemCount(); i++) {
                ParcelaReservaViewHolder holder = (ParcelaReservaViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                if (holder != null) {
                    String ocupantesStr = holder.parcelaOcupantesEditText.getText().toString().trim();
                    int ocupantes = Integer.parseInt(ocupantesStr);
                    ParcelaReserva parcelaReserva = adapter.getCurrentList().get(i);
                    //Log.d("GetNumOcupantes", "La parcela tiene : " + ocupantes + " ocupantes");

                    if (ocupantes > 0) {
                        parcelaReserva.setReservaID((int) reservaId);
                        parcelaReserva.setNumOcupantes(ocupantes);
                        // Insertar o actualizar la parcela-reserva
                        long parcelaReservaId = parcelaReservaViewModel.insert(parcelaReserva);
                        //Log.d("ParcelaReservaViewModel", "ID de inserción: " + parcelaReservaId);
                        if (parcelaReservaId == -1) {
                            //Log.e("ListarParcelasReserva", "Error al guardar ParcelaReserva con ParcelaID: " + parcelaReserva.getParcelaID());
                        } else {
                            //Log.d("ListarParcelasReserva", "ParcelaReserva guardada con ParcelaID: " + parcelaReserva.getParcelaID());
                        }
                    } else {
                        // Eliminar parcelas no seleccionadas si existen
                        if (parcelaReserva.getParcelaID() != 0) {
                            parcelaReservaViewModel.delete(parcelaReserva);
                            //Log.d("ListarParcelasReserva", "ParcelaReserva eliminada con ID: " + parcelaReserva.getParcelaID());
                        }
                    }
                }

            }

            double precio = calcularPrecio();
            Reserva reservaFinal = new Reserva(
                    nombreCliente,
                    telefonoCliente,
                    new Date(fechaEntrada),
                    new Date(fechaSalida),
                    precio
            );

            reservaFinal.setID(reservaID);
            reservaFinal.setPrecioTotal(precio);
            reservaViewModel.update(reservaFinal);

            Log.d("ListarParcelasReserva", "Reserva tiene precio de " + reservaFinal.getPrecioTotal() + "€");
            Toast.makeText(this, "Reserva guardada correctamente.", Toast.LENGTH_LONG).show();
            finish(); // Cerrar la actividad
        }
    }
}
