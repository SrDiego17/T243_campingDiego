package es.unizar.eina.T243_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import es.unizar.eina.T243_camping.database.Parcela;
import es.unizar.eina.T243_camping.R;
import es.unizar.eina.T243_camping.database.Reserva;
import es.unizar.eina.send.*;

public class ListarReservas extends AppCompatActivity {
    public static ReservaViewModel reservaViewModel;
    private ReservaListAdapter adapter;
    private int spinnerPos;
    private Spinner mSpinnerOrd;
    private List<Reserva> listaReservas = new ArrayList<>();
    private Reserva mReserva;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                try {
                    String nombreCliente = extras.getString(ReservaEdit.RESERVA_NOMBRE_CLIENTE, "");
                    String telefonoCliente = extras.getString(ReservaEdit.RESERVA_TELEFONO_CLIENTE, "");
                    long fechaEntradaMillis = extras.getLong(ReservaEdit.RESERVA_FECHA_ENTRADA, -1);
                    long fechaSalidaMillis = extras.getLong(ReservaEdit.RESERVA_FECHA_SALIDA, -1);

                    if (fechaEntradaMillis == -1 || fechaSalidaMillis == -1) {
                        throw new IllegalArgumentException("Fechas inválidas");
                    }

                    mReserva.setNombreCliente(nombreCliente);
                    mReserva.setTelefonoCliente(telefonoCliente);
                    mReserva.setFechaEntrada(new Date(fechaEntradaMillis));
                    mReserva.setFechaSalida(new Date(fechaSalidaMillis));

                    reservaViewModel.update(mReserva);
                    Toast.makeText(this, "Reserva actualizada correctamente", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(this, "Error al procesar los datos de la reserva", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listarreservas);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        reservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);

        adapter = new ReservaListAdapter(new ReservaListAdapter.ReservaDiff(),
                this::editReserva, this::deleteReserva, this::infoReserva);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configuración del Spinner
        mSpinnerOrd = findViewById(R.id.spinnerOrdenarReservas);

        // Observa los datos iniciales
        reservaViewModel.getAllReservas().observe(this, reservas -> {
            listaReservas = reservas;
            switch (spinnerPos) {
                case 0: // Ordenar por nombre de cliente ascendente
                    reservas.sort(Comparator.comparing(Reserva::getNombreCliente, String.CASE_INSENSITIVE_ORDER));
                    break;

                case 1: // Ordenar por nombre de cliente descendente
                    reservas.sort(Comparator.comparing(Reserva::getNombreCliente, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;

                case 2: // Ordenar por teléfono de cliente ascendente
                    reservas.sort(Comparator.comparing(Reserva::getTelefonoCliente));
                    break;

                case 3: // Ordenar por teléfono de cliente descendente
                    reservas.sort(Comparator.comparing(Reserva::getTelefonoCliente).reversed());
                    break;

                case 4: // Ordenar por fecha de entrada ascendente
                    reservas.sort(Comparator.comparing(Reserva::getFechaEntrada));
                    break;

                case 5: // Ordenar por fecha de entrada descendente
                    reservas.sort(Comparator.comparing(Reserva::getFechaEntrada).reversed());
                    break;
            }
            adapter.submitList(reservas);  // Actualiza el RecyclerView
        });

        iniciarSpinner();
    }

    private void editReserva(int position) {
        mReserva = adapter.getCurrentList().get(position);
        Intent intent = new Intent(this, ReservaEdit.class);
        intent.putExtra(ReservaEdit.RESERVA_NOMBRE_CLIENTE, mReserva.getNombreCliente());
        intent.putExtra(ReservaEdit.RESERVA_TELEFONO_CLIENTE, mReserva.getTelefonoCliente());
        intent.putExtra(ReservaEdit.RESERVA_ID, mReserva.getID());
        //Log.d("ID DE RESERVA EN LISTA RESERVAS", "La reserva tiene id: " + mReserva.getID());
        intent.putExtra(ReservaEdit.RESERVA_FECHA_ENTRADA, mReserva.getFechaEntrada());
        intent.putExtra(ReservaEdit.RESERVA_FECHA_SALIDA, mReserva.getFechaSalida());
        startActivityForResult(intent, 1);
    }

    private void deleteReserva(int position) {
        Reserva reserva = adapter.getCurrentList().get(position);
        reservaViewModel.delete(reserva);
        Toast.makeText(this, "Reserva eliminada", Toast.LENGTH_SHORT).show();
    }

    private void infoReserva(int position) {
        Reserva reservaInfo = adapter.getCurrentList().get(position);
        String phone = String.valueOf(reservaInfo.getTelefonoCliente());
        String message = "Reserva #" + reservaInfo.getID() +
                "\nNombre Cliente: " + reservaInfo.getNombreCliente() +
                "\nFecha Entrada: " + reservaInfo.getFechaEntrada() +
                "\nFecha Salida: " + reservaInfo.getFechaSalida() +
                "\nPrecio Reserva: " + reservaInfo.getPrecioTotal() + "€";

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Enviar Información de la Reserva")
                .setMessage("¿Cómo desea enviar los detalles?")
                .setPositiveButton("Enviar por SMS", (dialog, which) -> {
                    SendAbstraction sendAbstraction = new SendAbstractionImpl(this, "SMS");
                    sendAbstraction.send(phone, message);
                    Toast.makeText(this, "Enviando detalles por SMS...", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Enviar por WhatsApp", (dialog, which) -> {
                    SendAbstraction sendAbstraction = new SendAbstractionImpl(this, "WhatsApp");
                    sendAbstraction.send(phone, message);
                    Toast.makeText(this, "Enviando detalles por WhatsApp...", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void iniciarSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.ordenacionReservas,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOrd.setAdapter(spinnerAdapter);

        mSpinnerOrd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Guardar la posición seleccionada
                spinnerPos = position;

                // Verificar si la lista está vacía
                if (listaReservas == null || listaReservas.isEmpty()) {
                    Log.d("SpinnerOrdenar", "Lista vacía");
                    return;
                }

                // Crear una copia de la lista actual
                List<Reserva> reservasOrdenadas = new ArrayList<>(listaReservas);
                // Aplicar la ordenación según la posición seleccionada
                switch (spinnerPos) {
                    case 0: // Ordenar por nombre de cliente ascendente
                        reservasOrdenadas.sort(Comparator.comparing(Reserva::getNombreCliente, String.CASE_INSENSITIVE_ORDER));
                        break;

                    case 1: // Ordenar por nombre de cliente descendente
                        reservasOrdenadas.sort(Comparator.comparing(Reserva::getNombreCliente, String.CASE_INSENSITIVE_ORDER).reversed());
                        break;

                    case 2: // Ordenar por teléfono de cliente ascendente
                        reservasOrdenadas.sort(Comparator.comparing(Reserva::getTelefonoCliente));
                        break;

                    case 3: // Ordenar por teléfono de cliente descendente
                        reservasOrdenadas.sort(Comparator.comparing(Reserva::getTelefonoCliente).reversed());
                        break;

                    case 4: // Ordenar por fecha de entrada ascendente
                        reservasOrdenadas.sort(Comparator.comparing(Reserva::getFechaEntrada));
                        break;

                    case 5: // Ordenar por fecha de entrada descendente
                        reservasOrdenadas.sort(Comparator.comparing(Reserva::getFechaEntrada).reversed());
                        break;

                    default:
                        Log.d("SpinnerOrdenar", "Selección no válida");
                        return;
                }

                // Actualizar la lista ordenada en el adaptador
                Log.d("SpinnerOrdenar", "Lista ordenada correctamente");
                adapter.submitList(reservasOrdenadas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no hay selección
            }
        });


    }
}
