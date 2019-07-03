package com.example.androidenklima.consultar.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.androidenklima.R;
import com.example.androidenklima.consultar.adapter.OcurrencyAdapter;
import com.example.androidenklima.consultar.model.Ocorrencia;
import com.example.androidenklima.webservice.CustomRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsultarFragment extends Fragment implements Response.Listener, Response.ErrorListener {

    private Context context;
    private RecyclerView recyclerView;
    private OcurrencyAdapter ocurrencyAdapter;
    private RequestQueue queue;
    private List<Ocorrencia> ocorrencias;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        openDialog();
        queue = CustomRequestQueue.getInstance(view.getContext().getApplicationContext()).getRequestQueue();
        recyclerView = view.findViewById(R.id.rvOcurrency);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        ocurrencyAdapter = new OcurrencyAdapter(context, getResources());
        recyclerView.setAdapter(ocurrencyAdapter);
        ocorrencias = new ArrayList<>();
        addRequest("http://10.0.2.2:8080/ocurrencyAll");
    }

    public static ConsultarFragment newInstance() {
        return new ConsultarFragment();
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void addRequest(String url) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, new JSONArray(), this, this);
        jsonObjectRequest.setTag(this.getClass().getSimpleName());
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            Thread.sleep(1000);
            closeProgress();
        } catch (InterruptedException e) {
            Log.d(this.getClass().getSimpleName(), "error: " + e.getMessage());
        }
        new AlertDialog.Builder(context)
                .setTitle("Erro")
                .setMessage("Ocorreu um erro ao tentar obter as ocorrências " + error.getMessage())
                .setNeutralButton("OK", null)
                .show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            JSONArray jsonArray = (JSONArray) response;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Ocorrencia ocorrencia = new Ocorrencia(jsonObject);
                ocorrencias.add(ocorrencia);
            }
            ocurrencyAdapter.update(ocorrencias);
            try {
                Thread.sleep(1000);
                closeProgress();
            } catch (InterruptedException e) {
                Log.d(this.getClass().getSimpleName(), "error: " + e.getMessage());
            }
        } catch (JSONException e) {
            new AlertDialog.Builder(context)
                    .setTitle("Erro")
                    .setMessage("Ocorreu um erro ao tentar obter as ocorrências " + e.getMessage())
                    .setNeutralButton("OK", null)
                    .show();
        }
    }

    private Dialog progress;

    private void openDialog() {
        progress = new Dialog(context);
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progress.setContentView(R.layout.dialog_progress);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.setCancelable(false);
        final ProgressBar progressBar = ProgressBar.class.cast(progress.findViewById(R.id.progressBar));
        progressBar.setIndeterminate(true);
        final TextView textView = progress.findViewById(R.id.textView);
        textView.setText("Download...");
        progress.show();
    }

    private void closeProgress() {
        progress.dismiss();
    }
}
