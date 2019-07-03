package com.example.androidenklima.cadastrar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.androidenklima.R;
import com.example.androidenklima.singleton.UserLogged;
import com.example.androidenklima.util.FileUtil;
import com.example.androidenklima.webservice.CustomRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class CadastrarFragment extends Fragment implements Response.Listener, Response.ErrorListener {

    private static final int REQUEST_IMAGE_CAPURE = 1;
    private TextInputLayout typeOcurrency;
    private TextInputLayout localOcurrency;
    private TextInputLayout description;
    private CheckBox isVitmin;
    private Button photo;
    private TextInputLayout policialName;
    private ImageView imageOcorrencia;
    private Button cadastrar;
    private RequestQueue queue;
    private Context context;
    private String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastrar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = CustomRequestQueue.getInstance(view.getContext().getApplicationContext()).getRequestQueue();
        typeOcurrency = view.findViewById(R.id.typeOcurrency);
        localOcurrency = view.findViewById(R.id.localOcurrency);
        isVitmin = view.findViewById(R.id.isVitmin);
        description = view.findViewById(R.id.description);
        photo = view.findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePicture.resolveActivity(context.getPackageManager()) != null) {
                    try {
                        File file = FileUtil.createFile(context, ".jpg");
                        currentPhotoPath = file.getAbsolutePath();
                        Uri uri = FileProvider.getUriForFile(context, "com.example.androidenklima.util.FileUtil", file);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(takePicture, REQUEST_IMAGE_CAPURE);
                    } catch (IOException e) {
                        new AlertDialog.Builder(context)
                                .setTitle("Erro")
                                .setMessage("Ocorreu um erro: " + e.getMessage())
                                .setNeutralButton("OK", null)
                                .show();
                    }
                }
            }
        });
        policialName = view.findViewById(R.id.policialOcurrency);
        imageOcorrencia = view.findViewById(R.id.imageOcurrency);
        cadastrar = view.findViewById(R.id.cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer cont = UserLogged.getInstance().getPolice().getNumberOcorrencia();
                if (cont >= 3) {
                    new AlertDialog.Builder(context)
                            .setTitle("Aviso")
                            .setMessage("Só é possível cadastrar apenas três ocorrências por dia!")
                            .setNeutralButton("OK", null)
                            .show();
                    return;
                }
                clearErrors();
                String tipoOcorrencia = typeOcurrency.getEditText().getText().toString();
                String local = localOcurrency.getEditText().getText().toString();
                String namePolice = policialName.getEditText().getText().toString();
                String descricao = description.getEditText().getText().toString();
                boolean cadastro = true;
                if (tipoOcorrencia == null || tipoOcorrencia.isEmpty()) {
                    typeOcurrency.setError("Favor, preencher o tipo da ocorrência");
                    cadastro = false;
                }
                if (local == null || local.isEmpty()) {
                    localOcurrency.setError("Favor, preencher o local da ocorrência");
                    cadastro = false;
                }
                if (namePolice == null || namePolice.isEmpty()) {
                    policialName.setError("Favor, prencher o nome do policial");
                    cadastro = false;
                }
                if (currentPhotoPath == null || currentPhotoPath.isEmpty()) {
                    Toast.makeText(context, "Favor, tirar uma foto da ocorrência", Toast.LENGTH_LONG).show();
                    cadastro = false;
                }
                if (descricao == null || descricao.isEmpty()) {
                    description.setError("Favor, preencher a descrição do crime");
                    cadastro = false;
                }
                if (cadastro) {
                    boolean vitmin = isVitmin.isChecked();
                    StringBuilder urlData = new StringBuilder();
                    urlData.append("?");
                    urlData.append("ocurrency_type=" + tipoOcorrencia);
                    urlData.append("&local=" + local);
                    urlData.append("&is_vitmin=" + vitmin);
                    urlData.append("&photo=" + currentPhotoPath);
                    urlData.append("&name_police=" + namePolice);
                    urlData.append("&description=" + descricao);
                    urlData.append("&cont=" + (cont + 1));
                    urlData.append("&id_police=" + UserLogged.getInstance().getPolice().getId());
                    openDialog();
                    addRequest("http://10.0.2.2:8080/saveOcurrency" + urlData.toString());
                }
            }
        });
    }

    private void clearErrors() {
        policialName.setError("");
        localOcurrency.setError("");
        typeOcurrency.setError("");
        description.setError("");
    }

    private void clearAll() {
        typeOcurrency.getEditText().setText("");
        localOcurrency.getEditText().setText("");
        policialName.getEditText().setText("");
        description.getEditText().setText("");
        imageOcorrencia.setImageDrawable(null);
    }

    private void addRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(), this, this);
        jsonObjectRequest.setTag(this.getClass().getSimpleName());
        queue.add(jsonObjectRequest);
    }

    public static CadastrarFragment newInstance() {
        return new CadastrarFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imageOcorrencia.setImageBitmap(bitmap);
        }
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
                .setMessage("Ocorreu um erro ao tentar cadastrar: " + error.getMessage())
                .setNeutralButton("OK", null)
                .show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            String id = jsonObject.getString("_id");
            Integer cont = UserLogged.getInstance().getPolice().getNumberOcorrencia();
            UserLogged.getInstance().getPolice().setNumberOcorrencia(cont + 1);
            try {
                Thread.sleep(1000);
                closeProgress();
            } catch (InterruptedException e) {
                Log.d(this.getClass().getSimpleName(), "error: " + e.getMessage());
            }
            new AlertDialog.Builder(context)
                    .setTitle("Cadastrado")
                    .setMessage("Cadastrado com sucesso com o id: " + id)
                    .setNeutralButton("OK", null)
                    .show();
            clearAll();
        } catch (JSONException e) {
            new AlertDialog.Builder(context)
                    .setTitle("Erro")
                    .setMessage("Ocorreu um erro ao tentar cadastrar: " + e.getMessage())
                    .setNeutralButton("OK", null)
                    .show();
        }
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(this.getClass().getSimpleName());
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
        textView.setText("Enviando...");
        progress.show();
    }

    private void closeProgress() {
        progress.dismiss();
    }
}
