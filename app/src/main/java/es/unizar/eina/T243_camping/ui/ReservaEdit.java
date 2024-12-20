package es.unizar.eina.T243_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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


    private Integer mRowId;

    private long reservaID = -1;

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

        reservaID = getIntent().getLongExtra(RESERVA_ID, -1);
        // Log.d("ID DE RESERVA", "La reserva tiene id: " + reservaID);
        // Inicializar el botón de guardar
        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> saveReserva());

        mReservaFechaEntradaText.setOnClickListener(v -> mostrarDatePicker(mReservaFechaEntradaText));
        mReservaFechaSalidaText.setOnClickListener(v -> mostrarDatePicker(mReservaFechaSalidaText));

        // Cargar datos si se está editando una parcela existente
        populateFields();
    }

    private void saveReserva() {
        if (TextUtils.isEmpty(mReservaNombreClienteText.getText())) {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
            return;  // Detener si el nombre está vacío
        }

        // Obtener los valores de los campos
        String nombreCliente = mReservaNombreClienteText.getText().toString();
        String telefonoCliente = mReservaTelefonoClienteText.getText().toString();
        String fechaEntradaStr = mReservaFechaEntradaText.getText().toString();
        String fechaSalidaStr = mReservaFechaSalidaText.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fechaEntrada, fechaSalida;

        try {
            fechaEntrada = sdf.parse(fechaEntradaStr);
            fechaSalida = sdf.parse(fechaSalidaStr);
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Formato de fecha inválido", Toast.LENGTH_LONG).show();
            return;  // Detener en caso de error
        }

        // Validar fechas
        if (fechaEntrada == null || fechaSalida == null || fechaSalida.before(fechaEntrada)) {
            Toast.makeText(getApplicationContext(), "La fecha de salida debe ser posterior a la de entrada.", Toast.LENGTH_LONG).show();
            return;  // Detener si las fechas no son válidas
        }

        // Comprobar si las fechas son anteriores a hoy
        Date fechaHoy = new Date(); // Fecha actual
        if (fechaEntrada.before(fechaHoy) || fechaSalida.before(fechaHoy)) {
            Toast.makeText(getApplicationContext(), "Las fechas no pueden ser anteriores a hoy.", Toast.LENGTH_LONG).show();
            return;  // Detener si alguna fecha es anterior a hoy
        }


        // Redirigir a ListarParcelasReserva pasando los datos
        Intent intent = new Intent(this, ListarParcelasReserva.class);
        intent.putExtra(RESERVA_NOMBRE_CLIENTE, nombreCliente);
        intent.putExtra(RESERVA_TELEFONO_CLIENTE, telefonoCliente);
        intent.putExtra(RESERVA_FECHA_ENTRADA, fechaEntrada.getTime());
        intent.putExtra(RESERVA_FECHA_SALIDA, fechaSalida.getTime());
        intent.putExtra(RESERVA_ID, reservaID);
        //Log.d("ID DE RESERVA", "La reserva QUE PONGO tiene id: " + reservaID);
        startActivity(intent);  // Iniciar la nueva actividad
        finish();  // Cerrar la actividad actual
    }




    private void populateFields() {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Recuperar datos de texto
            mReservaNombreClienteText.setText(extras.getString(RESERVA_NOMBRE_CLIENTE, ""));
            mReservaTelefonoClienteText.setText(extras.getString(RESERVA_TELEFONO_CLIENTE, ""));
            mReservaFechaEntradaText.setText(extras.getString(RESERVA_FECHA_ENTRADA));
            mReservaFechaSalidaText.setText(extras.getString(RESERVA_FECHA_SALIDA));
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
