package com.example.appconsultorio.ui.consulta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appconsultorio.R;

import java.util.List;

public class ConsultaAdapter extends RecyclerView.Adapter<ConsultaAdapter.ConsultaViewHolder> {

    private List<Consulta> consultas;

    public ConsultaAdapter(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    @NonNull
    @Override
    public ConsultaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consulta, parent, false);
        return new ConsultaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultaViewHolder holder, int position) {
        Consulta consulta = consultas.get(position);
        holder.dataTextView.setText(consulta.getData());
        holder.procedimentoTextView.setText(consulta.getProcedimento());
    }

    @Override
    public int getItemCount() {
        return consultas.size();
    }

    static class ConsultaViewHolder extends RecyclerView.ViewHolder {
        TextView dataTextView;
        TextView procedimentoTextView;

        public ConsultaViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTextView = itemView.findViewById(R.id.text_view_data);
            procedimentoTextView = itemView.findViewById(R.id.text_view_procedimento);
        }
    }
}

