package es.unizar.eina.T243_camping.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.T243_camping.R;
import es.unizar.eina.T243_camping.database.Reserva;

class ReservaViewHolder extends RecyclerView.ViewHolder {
    private final TextView mClienteTextView;
    private final TextView mTelefonoTextView;
    private final TextView mFechaTextView;

    private final TextView mPrecioTextView;
    private final Button editButton;
    private final Button deleteButton;

    private ReservaViewHolder(View itemView) {
        super(itemView);
        mClienteTextView = itemView.findViewById(R.id.textView_cliente);
        mTelefonoTextView = itemView.findViewById(R.id.textView_telefono);
        mFechaTextView = itemView.findViewById(R.id.textView_fecha);
        mPrecioTextView = itemView.findViewById(R.id.textView_precio);
        editButton = itemView.findViewById(R.id.button_edit_reserva);
        deleteButton = itemView.findViewById(R.id.button_delete_reserva);
    }

    public void bind(Reserva reserva, ReservaListAdapter.OnEditClickListener editListener, ReservaListAdapter.OnDeleteClickListener deleteListener) {
        mClienteTextView.setText(reserva.getNombreCliente());
        mTelefonoTextView.setText(reserva.getTelefonoCliente());
        mFechaTextView.setText(reserva.getFechaEntrada().toString());
        mPrecioTextView.setText(Double.toString(reserva.calcularPrecio()) + "€");
        editButton.setOnClickListener(v -> editListener.onEditClick(getAdapterPosition()));
        deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(getAdapterPosition()));
    }

    static ReservaViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_reserva, parent, false);
        return new ReservaViewHolder(view);
    }


}
