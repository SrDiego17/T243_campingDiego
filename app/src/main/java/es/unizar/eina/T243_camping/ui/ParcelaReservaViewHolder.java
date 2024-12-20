package es.unizar.eina.T243_camping.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.T243_camping.R;

public class ParcelaReservaViewHolder extends RecyclerView.ViewHolder {

    public final TextView parcelaNombreTextView;
    public final TextView parcelaPrecioTextView;
    public final TextView parcelaMaxOcupantesTextView;
    public final EditText parcelaOcupantesEditText;

    public ParcelaReservaViewHolder(@NonNull View itemView) {
        super(itemView);

        // Referencias a los elementos de la vista
        parcelaNombreTextView = itemView.findViewById(R.id.parcelaTextView);
        parcelaPrecioTextView = itemView.findViewById(R.id.parcelaPriceTextView);
        parcelaOcupantesEditText = itemView.findViewById(R.id.parcelaNumberEditText);
        parcelaMaxOcupantesTextView = itemView.findViewById(R.id.parcelaMaxOcupantesTextView);
    }
}
