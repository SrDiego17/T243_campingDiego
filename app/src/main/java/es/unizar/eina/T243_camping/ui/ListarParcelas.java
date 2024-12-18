package es.unizar.eina.T243_camping.ui;

import  android.content.Intent;
import android.os.Bundle;
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
import java.util.List;
import android.util.Log;

import es.unizar.eina.T243_camping.database.Parcela;
import es.unizar.eina.T243_camping.R;

public class ListarParcelas extends AppCompatActivity  {
    public static ParcelaViewModel parcelaViewModel;
    private ParcelaListAdapter adapter;
    private int spinnerPos;
    private Spinner mSpinnerOrd;
    private List<Parcela> listaParcelas = new ArrayList<>();
    private Parcela mParcela;

    // Declare the ActivityResultLauncher for editing a Parcela
    ActivityResultLauncher<Intent> mStartEditParcela = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Parcela parcela = new Parcela(
                            extras.getString(ParcelaEdit.PARCELA_NOMBRE),
                            extras.getString(ParcelaEdit.PARCELA_DESCRIPCION),
                            extras.getInt(ParcelaEdit.PARCELA_MAXOCUPANTES),
                            extras.getDouble(ParcelaEdit.PARCELA_PRECIOPOROCUPANTE)
                    );
                    parcela.setId(extras.getInt(ParcelaEdit.PARCELA_ID)); // Assuming the ID is passed in the extras
                    parcelaViewModel.update(parcela);
                    Toast.makeText(this, "Parcela actualizada", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listarparcelas);

        // Inicialización del RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);

        adapter = new ParcelaListAdapter(new ParcelaListAdapter.ParcelaDiff(),
                this::editParcela, this::deleteParcela);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configuración del Spinner
        mSpinnerOrd = findViewById(R.id.spinnerOrdenar);

        // Observa los datos iniciales
        parcelaViewModel.getAllParcelas().observe(this, parcelas -> {
            listaParcelas = parcelas;
            switch (spinnerPos) {
                case 0: // Ordenar por nombre ascendente
                    parcelas.sort(Comparator.comparing(Parcela::getNombre, String.CASE_INSENSITIVE_ORDER));
                    break;

                case 1: // Ordenar por nombre descendente
                    parcelas.sort(Comparator.comparing(Parcela::getNombre, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;

                case 2: // Ordenar por máximo de ocupantes ascendente
                    parcelas.sort(Comparator.comparingInt(Parcela::getMaxOcupantes));
                    break;

                case 3: // Ordenar por máximo de ocupantes descendente
                    parcelas.sort(Comparator.comparingInt(Parcela::getMaxOcupantes).reversed());
                    break;

                case 4: // Ordenar por precio por ocupante ascendente
                    parcelas.sort(Comparator.comparingDouble(Parcela::getPrecioPorOcupante));
                    break;

                case 5: // Ordenar por precio por ocupante descendente
                    parcelas.sort(Comparator.comparingDouble(Parcela::getPrecioPorOcupante).reversed());
                    break;
            }
            adapter.submitList(parcelas);  // Actualiza el RecyclerView
        });

        iniciarSpinner();

    }

    private void editParcela(int position) {
        mParcela = adapter.getCurrentList().get(position);
        Intent intent = new Intent(this, ParcelaEdit.class);
        intent.putExtra(ParcelaEdit.PARCELA_ID, mParcela.getId());
        intent.putExtra(ParcelaEdit.PARCELA_NOMBRE, mParcela.getNombre());
        intent.putExtra(ParcelaEdit.PARCELA_DESCRIPCION, mParcela.getDescripcion());
        intent.putExtra(ParcelaEdit.PARCELA_MAXOCUPANTES, mParcela.getMaxOcupantes());
        intent.putExtra(ParcelaEdit.PARCELA_PRECIOPOROCUPANTE, mParcela.getPrecioPorOcupante());
        mStartEditParcela.launch(intent); // Launch the activity for result
    }

    private void deleteParcela(int position) {
        Parcela parcela = adapter.getCurrentList().get(position);
        parcelaViewModel.delete(parcela);
        Toast.makeText(this, "Parcela eliminada", Toast.LENGTH_SHORT).show();
    }

    private void iniciarSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.ordenacionParcelas,
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
                if (listaParcelas == null || listaParcelas.isEmpty()) {
                    Log.d("SpinnerOrdenar", "Lista vacía");
                    return;
                }

                // Crear una copia de la lista actual
                List<Parcela> parcelasOrdenadas = new ArrayList<>(listaParcelas);
                // Aplicar la ordenación según la posición seleccionada
                switch (spinnerPos) {
                    case 0: // Ordenar por nombre ascendente
                        parcelasOrdenadas.sort(Comparator.comparing(Parcela::getNombre, String.CASE_INSENSITIVE_ORDER));
                        break;

                    case 1: // Ordenar por nombre descendente
                        parcelasOrdenadas.sort(Comparator.comparing(Parcela::getNombre, String.CASE_INSENSITIVE_ORDER).reversed());
                        break;

                    case 2: // Ordenar por máximo de ocupantes ascendente
                        parcelasOrdenadas.sort(Comparator.comparingInt(Parcela::getMaxOcupantes));
                        break;

                    case 3: // Ordenar por máximo de ocupantes descendente
                        parcelasOrdenadas.sort(Comparator.comparingInt(Parcela::getMaxOcupantes).reversed());
                        break;

                    case 4: // Ordenar por precio por ocupante ascendente
                        parcelasOrdenadas.sort(Comparator.comparingDouble(Parcela::getPrecioPorOcupante));
                        break;

                    case 5: // Ordenar por precio por ocupante descendente
                        parcelasOrdenadas.sort(Comparator.comparingDouble(Parcela::getPrecioPorOcupante).reversed());
                        break;

                    default:
                        Log.d("SpinnerOrdenar", "Selección no válida");
                        return;
                }

                // Actualizar la lista ordenada en el adaptador
                Log.d("SpinnerOrdenar", "Lista ordenada correctamente");
                adapter.submitList(parcelasOrdenadas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no hay selección
            }
        });


    }
}
