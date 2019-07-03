package com.example.androidenklima.consultar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidenklima.R;
import com.example.androidenklima.consultar.activity.OcurrencyDetailActivity;
import com.example.androidenklima.consultar.model.Ocorrencia;

import java.util.ArrayList;
import java.util.List;

public class OcurrencyAdapter extends RecyclerView.Adapter<OcurrencyAdapter.OcurrencyViewHolder> {

    private List<Ocorrencia> ocorrencias;
    private Resources resources;
    private Context context;

    public OcurrencyAdapter(Context context, Resources resources) {
        ocorrencias = new ArrayList<>();
        this.resources = resources;
        this.context = context;
    }

    public void update(List<Ocorrencia> ocorrencias) {
        this.ocorrencias.clear();
        this.ocorrencias.addAll(ocorrencias);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OcurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final int pos = i;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ocorrencia, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OcurrencyDetailActivity.class);
                intent.putExtra("Ocorrencia", ocorrencias.get(pos));
                context.startActivity(intent);
            }
        });
        return new OcurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OcurrencyViewHolder ocurrencyViewHolder, int i) {
        Ocorrencia ocorrencia = ocorrencias.get(i);
        Bitmap bitmap = BitmapFactory.decodeFile(ocorrencia.getPhoto());
        Drawable d = new BitmapDrawable(resources, bitmap);
        ocurrencyViewHolder.imageView.setImageDrawable(d);
        ocurrencyViewHolder.title.setText(ocorrencia.getTypeOcurreny());
        ocurrencyViewHolder.description.setText(ocorrencia.getDescription());
    }

    @Override
    public int getItemCount() {
        return ocorrencias.size();
    }

    public class OcurrencyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView title;
        public TextView description;

        public OcurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photoItem);
            title = itemView.findViewById(R.id.titleItem);
            description = itemView.findViewById(R.id.descriptionItem);
        }
    }
}
