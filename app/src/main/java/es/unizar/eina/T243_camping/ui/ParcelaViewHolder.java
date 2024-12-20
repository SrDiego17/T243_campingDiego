package es.unizar.eina.T243_camping.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.T243_camping.R;
import es.unizar.eina.T243_camping.database.Parcela;

class ParcelaViewHolder extends RecyclerView.ViewHolder {
    private final TextView mNombreTextView;
    private final TextView mMaxOcupantesTextView;
    private final TextView mPrecioOcupanteTextView;
    private final Button deleteButton;
    private final CardView cardView;

    private ParcelaViewHolder(View itemView) {
        super(itemView);
        mNombreTextView = itemView.findViewById(R.id.textView);
        mMaxOcupantesTextView = itemView.findViewById(R.id.maxOcupantes);
        mPrecioOcupanteTextView = itemView.findViewById(R.id.precioPorOcupante);
        deleteButton = itemView.findViewById(R.id.button_delete);
        cardView = (CardView) itemView;
    }

    public void bind(
            Parcela parcela,
            ParcelaListAdapter.OnItemClickListener itemClickListener,
            ParcelaListAdapter.OnDeleteClickListener deleteListener) {

        // Asigna valores a los campos
        mNombreTextView.setText(parcela.getNombre());
        mMaxOcupantesTextView.setText(parcela.getMaxOcupantes() + " ocupantes máximo");
        mPrecioOcupanteTextView.setText(parcela.getPrecioPorOcupante() + "€ por ocupante");

        // Listener para clic en la tarjeta
        cardView.setOnClickListener(v -> itemClickListener.onItemClick(getAdapterPosition()));

        // Listener para el botón de eliminar
        deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(getAdapterPosition()));
    }

    static ParcelaViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ParcelaViewHolder(view);
    }
}
