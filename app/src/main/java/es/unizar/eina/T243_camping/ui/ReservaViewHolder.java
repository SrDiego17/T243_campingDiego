package es.unizar.eina.T243_camping.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.T243_camping.R;
import es.unizar.eina.T243_camping.database.Reserva;

class ReservaViewHolder extends RecyclerView.ViewHolder {
    final CardView cardView;
    private final TextView mClienteTextView;
    private final TextView mTelefonoTextView;
    private final TextView mFechaTextView;
    private final TextView mPrecioTextView;
    private final Button deleteButton;
    private final Button infoButton;

    private ReservaViewHolder(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.cardView_reserva);
        mClienteTextView = itemView.findViewById(R.id.textView_cliente);
        mTelefonoTextView = itemView.findViewById(R.id.textView_telefono);
        mFechaTextView = itemView.findViewById(R.id.textView_fecha);
        mPrecioTextView = itemView.findViewById(R.id.textView_precio);
        deleteButton = itemView.findViewById(R.id.button_delete_reserva);
        infoButton = itemView.findViewById(R.id.button_info_reserva);
    }

    public void bind(Reserva reserva, ReservaListAdapter.OnItemClickListener itemClickListener, ReservaListAdapter.OnDeleteClickListener deleteListener, ReservaListAdapter.OnInfoClickListener infoListener) {
        mClienteTextView.setText(reserva.getNombreCliente());
        mTelefonoTextView.setText(reserva.getTelefonoCliente());
        mFechaTextView.setText(reserva.getFechaEntrada().toString());
        mPrecioTextView.setText(String.format("%.2f€", reserva.getPrecioTotal()));

        // Manejar clic en toda la tarjeta
        cardView.setOnClickListener(v -> itemClickListener.onItemClick(getAdapterPosition()));

        // Manejar clic en el botón eliminar
        deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(getAdapterPosition()));

        // Manejar clic en el botón Info
        infoButton.setOnClickListener(v -> infoListener.onInfoClick(getAdapterPosition()));
    }

    static ReservaViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_reserva, parent, false);
        return new ReservaViewHolder(view);
    }
}
