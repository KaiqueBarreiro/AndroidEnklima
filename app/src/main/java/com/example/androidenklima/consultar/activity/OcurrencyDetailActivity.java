package com.example.androidenklima.consultar.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.androidenklima.R;
import com.example.androidenklima.consultar.model.Ocorrencia;

public class OcurrencyDetailActivity extends AppCompatActivity {

    private ImageView photo;
    private TextInputLayout tipo;
    private TextInputLayout descricao;
    private TextInputLayout local;
    private TextInputLayout data;
    private TextInputLayout nome;
    private TextInputLayout vitima;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ocurrency);
        Ocorrencia ocorrencia = (Ocorrencia) getIntent().getSerializableExtra("Ocorrencia");
        photo = findViewById(R.id.photoDetail);
        tipo = findViewById(R.id.typeDetail);
        descricao = findViewById(R.id.descriptionDetail);
        local = findViewById(R.id.localDetail);
        data = findViewById(R.id.dataDetail);
        nome = findViewById(R.id.namePoliceDetail);
        vitima = findViewById(R.id.vitminDetail);

        Bitmap bitmap = BitmapFactory.decodeFile(ocorrencia.getPhoto());
        Drawable d = new BitmapDrawable(getResources(), bitmap);
        photo.setImageDrawable(d);

        tipo.getEditText().setText(ocorrencia.getTypeOcurreny());
        descricao.getEditText().setText(ocorrencia.getDescription());
        local.getEditText().setText(ocorrencia.getLocal());
        data.getEditText().setText(ocorrencia.getData());
        nome.getEditText().setText(ocorrencia.getNamePolice());
        if (ocorrencia.isExistsVitmin()) {
            vitima.getEditText().setText("Sim");
        } else {
            vitima.getEditText().setText("Não");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
