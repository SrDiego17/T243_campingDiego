package es.unizar.eina.T243_camping.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.unizar.eina.T243_camping.R;

/** Pantalla utilizada para la creación o edición de una parcela */
public class ParcelaEdit extends AppCompatActivity {

    public static final String PARCELA_NOMBRE = "nombre";
    public static final String PARCELA_DESCRIPCION = "descripcion";
    public static final String PARCELA_ID = "id";
    public static final String PARCELA_MAXOCUPANTES = "maxOcupantes";
    public static final String PARCELA_PRECIOPOROCUPANTE = "precioPorOcupante";

    private EditText mTitleText;
    private EditText mDescripcionText;
    private EditText mParcelaMaxOcupantesText;
    private EditText mParcelaPrecioPorOcupanteText;

    private Integer mRowId;

    Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelaedit);

        // Inicializar los campos EditText
        mTitleText = findViewById(R.id.nombre);
        mDescripcionText = findViewById(R.id.descripcion);
        mParcelaMaxOcupantesText = findViewById(R.id.maxOcupantes);
        mParcelaPrecioPorOcupanteText = findViewById(R.id.precioPorOcupante);

        // Inicializar el botón de guardar
        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> saveParcela());

        // Cargar datos si se está editando una parcela existente
        populateFields();
    }

    private void saveParcela() {
        Intent replyIntent = new Intent();

        if (TextUtils.isEmpty(mTitleText.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        } else {
            // Obtener los valores de los campos
            String nombre = mTitleText.getText().toString();
            String descripcion = mDescripcionText.getText().toString();
            int maxOcupantes = Integer.parseInt(mParcelaMaxOcupantesText.getText().toString());
            double precioPorOcupante = Double.parseDouble(mParcelaPrecioPorOcupanteText.getText().toString());

            replyIntent.putExtra(PARCELA_NOMBRE, nombre);
            replyIntent.putExtra(PARCELA_DESCRIPCION, descripcion);
            replyIntent.putExtra(PARCELA_MAXOCUPANTES, maxOcupantes);
            replyIntent.putExtra(PARCELA_PRECIOPOROCUPANTE, precioPorOcupante);

            if (mRowId != null) {
                replyIntent.putExtra(PARCELA_ID, mRowId);
            }

            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    private void populateFields() {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTitleText.setText(extras.getString(PARCELA_NOMBRE, ""));
            mDescripcionText.setText(extras.getString(PARCELA_DESCRIPCION, ""));
            mParcelaMaxOcupantesText.setText(String.valueOf(extras.getInt(PARCELA_MAXOCUPANTES, 0)));
            mParcelaPrecioPorOcupanteText.setText(String.valueOf(extras.getDouble(PARCELA_PRECIOPOROCUPANTE, 0.0)));
            mRowId = extras.getInt(PARCELA_ID, -1);
            if (mRowId == -1) {
                mRowId = null;
            }
        }
    }
}
