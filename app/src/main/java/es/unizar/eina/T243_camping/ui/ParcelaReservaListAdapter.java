package es.unizar.eina.T243_camping.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.T243_camping.R;
import es.unizar.eina.T243_camping.database.ParcelaReserva;
import es.unizar.eina.T243_camping.ui.ParcelaViewModel;

public class ParcelaReservaListAdapter extends ListAdapter<ParcelaReserva, ParcelaReservaViewHolder> {

    private ParcelaViewModel parcelaViewModel;
    // Constructor que acepta un listener para cambios de ocupantes
    public ParcelaReservaListAdapter(@NonNull DiffUtil.ItemCallback<ParcelaReserva> diffCallback, ParcelaViewModel parcelaViewModel) {
        super(diffCallback);
        this.parcelaViewModel = parcelaViewModel;
    }

    @NonNull
    @Override
    public ParcelaReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_parcelareserva, parent, false);
        return new ParcelaReservaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelaReservaViewHolder holder, int position) {
        ParcelaReserva current = getItem(position);

        // Configura el nombre de la parcela y el ID de la reserva
        int maxOcupantes = parcelaViewModel.getMaxOcupantesParcela(current.getParcelaID());
        double precio = parcelaViewModel.getPrecioParcela(current.getParcelaID());
        String nombre = parcelaViewModel.getNombreParcela(current.getParcelaID());
        holder.parcelaNombreTextView.setText(String.format("%s", nombre));
        holder.parcelaPrecioTextView.setText(String.format("%.2f€", precio));
        holder.parcelaMaxOcupantesTextView.setText(String.format("%d", maxOcupantes));

        // Establece el número de ocupantes
        holder.parcelaOcupantesEditText.setText(String.format("%d", current.getNumOcupantes()));
    }

    // Comparador para ListAdapter
    public static class ParcelaReservaDiff extends DiffUtil.ItemCallback<ParcelaReserva> {
        @Override
        public boolean areItemsTheSame(@NonNull ParcelaReserva oldItem, @NonNull ParcelaReserva newItem) {
            return oldItem.getReservaID() == newItem.getReservaID() &&
                    oldItem.getParcelaID() == newItem.getParcelaID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ParcelaReserva oldItem, @NonNull ParcelaReserva newItem) {
            return oldItem.equals(newItem);
        }
    }
}
