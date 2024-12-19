package es.unizar.eina.T243_camping.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.T243_camping.database.Reserva;

public class ReservaListAdapter extends ListAdapter<Reserva, ReservaViewHolder> {
    private final OnItemClickListener itemClickListener;
    private final OnDeleteClickListener deleteListener;
    private final OnInfoClickListener infoListener;

    public ReservaListAdapter(@NonNull DiffUtil.ItemCallback<Reserva> diffCallback,
                              OnItemClickListener itemClickListener, OnDeleteClickListener deleteListener, OnInfoClickListener infoListener) {
        super(diffCallback);
        this.itemClickListener = itemClickListener;
        this.deleteListener = deleteListener;
        this.infoListener = infoListener;
    }

    @Override
    public ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ReservaViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ReservaViewHolder holder, int position) {
        Reserva current = getItem(position);
        holder.bind(current, itemClickListener, deleteListener, infoListener);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnInfoClickListener {
        void onInfoClick(int position);
    }

    static class ReservaDiff extends DiffUtil.ItemCallback<Reserva> {
        @Override
        public boolean areItemsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            return oldItem.getID() == newItem.getID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            return oldItem.getNombreCliente().equals(newItem.getNombreCliente()) &&
                    oldItem.getTelefonoCliente().equals(newItem.getTelefonoCliente()) &&
                    oldItem.getFechaEntrada().equals(newItem.getFechaEntrada()) &&
                    oldItem.getFechaSalida().equals(newItem.getFechaSalida());
        }
    }
}