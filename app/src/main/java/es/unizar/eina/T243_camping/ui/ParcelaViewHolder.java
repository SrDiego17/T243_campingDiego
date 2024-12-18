package es.unizar.eina.T243_camping.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.T243_camping.R;
import es.unizar.eina.T243_camping.database.Parcela;

class ParcelaViewHolder extends RecyclerView.ViewHolder {
    private final TextView mNombreTextView;
    private final TextView mMaxOcupantesTextView;
    private final TextView mPrecioOcupanteTextView;
    private final Button editButton;
    private final Button deleteButton;

    private ParcelaViewHolder(View itemView) {
        super(itemView);
        mNombreTextView = itemView.findViewById(R.id.textView);
        mMaxOcupantesTextView = itemView.findViewById(R.id.maxOcupantes);
        mPrecioOcupanteTextView = itemView.findViewById(R.id.precioPorOcupante);
        editButton = itemView.findViewById(R.id.button_edit);
        deleteButton = itemView.findViewById(R.id.button_delete);
    }

    public void bind(Parcela parcela, ParcelaListAdapter.OnEditClickListener editListener, ParcelaListAdapter.OnDeleteClickListener deleteListener) {
        mNombreTextView.setText(parcela.getNombre());
        mMaxOcupantesTextView.setText(parcela.getMaxOcupantes() + " ocupantes máximo");
        mPrecioOcupanteTextView.setText(parcela.getPrecioPorOcupante() + "€ por ocupante");

        editButton.setOnClickListener(v -> editListener.onEditClick(getAdapterPosition()));
        deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(getAdapterPosition()));
    }

    static ParcelaViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ParcelaViewHolder(view);
    }


}
