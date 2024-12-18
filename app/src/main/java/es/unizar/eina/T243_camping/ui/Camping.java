package es.unizar.eina.T243_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;

import es.unizar.eina.T243_camping.R;
import es.unizar.eina.T243_camping.database.Parcela;
import es.unizar.eina.T243_camping.database.Reserva; // Nuevo

public class Camping extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camping);

        // Inicializar ViewModels
        ListarParcelas.parcelaViewModel = new ParcelaViewModel(getApplication());
        ListarReservas.reservaViewModel = new ReservaViewModel(getApplication());

        // Botones para parcelas
        Button addParcelaButton = findViewById(R.id.button_add_parcela);
        addParcelaButton.setOnClickListener(v -> createParcela());

        Button viewListButton = findViewById(R.id.button_view_list);
        viewListButton.setOnClickListener(v -> {
            Intent intent = new Intent(Camping.this, ListarParcelas.class);
            startActivity(intent);
        });

        // Botones para reservas
        Button addReservaButton = findViewById(R.id.button_add_reserva);
        addReservaButton.setOnClickListener(v -> createReserva());

        Button listarReservasButton = findViewById(R.id.button_view_reservas);
        listarReservasButton.setOnClickListener(v -> {
            Intent intent = new Intent(Camping.this, ListarReservas.class);
            startActivity(intent);
        });
    }

    private void createParcela() {
        mStartCreateParcela.launch(new Intent(this, ParcelaEdit.class));
    }

    private void createReserva() {
        mStartCreateReserva.launch(new Intent(this, ReservaEdit.class));
    }


    // ActivityResultLaunchers para Parcelas y Reservas
    ActivityResultLauncher<Intent> mStartCreateParcela = newActivityResultLauncher(
            extras -> {
                Parcela parcela = new Parcela(
                        extras.getString(ParcelaEdit.PARCELA_NOMBRE),
                        extras.getString(ParcelaEdit.PARCELA_DESCRIPCION),
                        extras.getInt(ParcelaEdit.PARCELA_MAXOCUPANTES),
                        extras.getDouble(ParcelaEdit.PARCELA_PRECIOPOROCUPANTE)
                );
                ListarParcelas.parcelaViewModel.insert(parcela);
            });

    ActivityResultLauncher<Intent> mStartCreateReserva = newActivityResultLauncher(
            extras -> {
                Reserva reserva = new Reserva(
                        extras.getString(ReservaEdit.RESERVA_NOMBRE_CLIENTE),
                        extras.getString(ReservaEdit.RESERVA_TELEFONO_CLIENTE),
                        new Date(extras.getLong(ReservaEdit.RESERVA_FECHA_ENTRADA)),
                        new Date(extras.getLong(ReservaEdit.RESERVA_FECHA_SALIDA))
                );
                ListarReservas.reservaViewModel.insert(reserva);
            });

    // Método genérico para crear ActivityResultLauncher
    ActivityResultLauncher<Intent> newActivityResultLauncher(ActivityResultProcessor processor) {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            processor.process(extras);
                        }
                    }
                });
    }

    // Interfaz genérica para procesar resultados
    interface ActivityResultProcessor {
        void process(Bundle extras);
    }
}
