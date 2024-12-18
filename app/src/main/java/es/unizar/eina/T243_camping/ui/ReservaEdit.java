package es.unizar.eina.T243_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Locale;
import java.util.Calendar;
import android.app.DatePickerDialog;


import java.text.ParseException;
import java.text.SimpleDateFormat;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.util.Date;

import es.unizar.eina.T243_camping.R;

public class ReservaEdit extends AppCompatActivity {

    public static final String RESERVA_NOMBRE_CLIENTE = "nombre_cliente";
    public static final String RESERVA_TELEFONO_CLIENTE = "telefono_cliente";
    public static final String RESERVA_ID = "id";
    public static final String RESERVA_FECHA_ENTRADA = "fecha_entrada";
    public static final String RESERVA_FECHA_SALIDA = "fecha_salida";


    private EditText mReservaNombreClienteText;
    private EditText mReservaTelefonoClienteText;
    private EditText mReservaFechaEntradaText;
    private EditText mReservaFechaSalidaText;


    private Integer mRowId; // Diego : No se si se usará o no

    Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservaedit);

        // Inicializar los campos EditText
        mReservaNombreClienteText = findViewById(R.id.nombreCliente);
        mReservaTelefonoClienteText = findViewById(R.id.telefono);
        mReservaFechaEntradaText = findViewById(R.id.fechaEntrada);
        mReservaFechaSalidaText = findViewById(R.id.fechaSalida);

        // Inicializar el botón de guardar
        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> saveReserva());

        mReservaFechaEntradaText.setOnClickListener(v -> mostrarDatePicker(mReservaFechaEntradaText));
        mReservaFechaSalidaText.setOnClickListener(v -> mostrarDatePicker(mReservaFechaSalidaText));

        // Cargar datos si se está editando una parcela existente
        populateFields();
    }

    private void saveReserva() {
        Intent replyIntent = new Intent();

        if (TextUtils.isEmpty(mReservaNombreClienteText.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show(); // DESCONOZCO COMO CAMBIAR LO DEL R EMPTY NOT SAVED
        } else {
            // Obtener los valores de los campos
            String nombreCliente = mReservaNombreClienteText.getText().toString();
            String telefonoCliente = mReservaTelefonoClienteText.getText().toString();
            String fechaEntradaStr = mReservaFechaEntradaText.getText().toString();
            String fechaSalidaStr = mReservaFechaSalidaText.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fechaEntrada = null;
            Date fechaSalida = null;
            try {
                fechaEntrada = sdf.parse(fechaEntradaStr);
                fechaSalida = sdf.parse(fechaSalidaStr);
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Formato de fecha inválido", Toast.LENGTH_LONG).show();
                return;
            }


            replyIntent.putExtra(RESERVA_NOMBRE_CLIENTE, nombreCliente);
            replyIntent.putExtra(RESERVA_TELEFONO_CLIENTE, telefonoCliente);
            replyIntent.putExtra(RESERVA_FECHA_ENTRADA, fechaEntrada.getTime());
            replyIntent.putExtra(RESERVA_FECHA_SALIDA, fechaSalida.getTime());

            if (mRowId != null) {
                replyIntent.putExtra(RESERVA_ID, mRowId);
            }

            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    private void populateFields() {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Recuperar datos de texto
            mReservaNombreClienteText.setText(extras.getString(RESERVA_NOMBRE_CLIENTE, ""));
            mReservaTelefonoClienteText.setText(extras.getString(RESERVA_TELEFONO_CLIENTE, ""));

            // Recuperar fechas usando getLong()
            long fechaEntradaMillis = extras.getLong(RESERVA_FECHA_ENTRADA, -1);
            long fechaSalidaMillis = extras.getLong(RESERVA_FECHA_SALIDA, -1);


            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            // Comprobar y mostrar fechas
            if (fechaEntradaMillis != -1) {
                Date fechaEntrada = new Date(fechaEntradaMillis);
                mReservaFechaEntradaText.setText(sdf.format(fechaEntrada));
            }

            if (fechaSalidaMillis != -1) {
                Date fechaSalida = new Date(fechaSalidaMillis);
                mReservaFechaSalidaText.setText(sdf.format(fechaSalida));
            }

            // Recuperar ID como int
            int id = extras.getInt(RESERVA_ID, -1);
            if (id != -1) {
                mRowId = id;
            }
        }
    }


    private void mostrarDatePicker(EditText campoFecha) {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
            campoFecha.setText(fechaSeleccionada);
        }, anio, mes, dia);

        datePicker.show();
    }

}
