package com.example.androidenklima.login.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.androidenklima.R;
import com.example.androidenklima.login.model.Police;
import com.example.androidenklima.principal.activity.MenuActivity;
import com.example.androidenklima.singleton.UserLogged;
import com.example.androidenklima.util.SHA1;
import com.example.androidenklima.webservice.CustomRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {

    private TextInputLayout code;
    private TextInputLayout password;
    private Button login;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        code = findViewById(R.id.codeLogin);
        password = findViewById(R.id.pwdLogin);
        login = findViewById(R.id.btnLogin);
        queue = CustomRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        login.setOnClickListener(login());
    }

    private View.OnClickListener login() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean logar = true;
                String codestr = code.getEditText().getText().toString();
                String pwd = password.getEditText().getText().toString();
                if (codestr == null || codestr.isEmpty()) {
                    code.setError("Favor, inserir a sua matrícula");
                    logar = false;
                }
                if (pwd == null || pwd.isEmpty()) {
                    password.setError("Favor, inserir sua senha");
                    logar = false;
                }
                if (logar) {
                    String pwdSha1 = SHA1.getSHA1(pwd);
                    openDialog();
                    addRequest("http://10.0.2.2:8080/login?code=" + codestr + "&password=" + pwdSha1);
                }
            }
        };
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
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Erro ao tentar logar")
                .setMessage("Ocorreu um erro ao tentar logar: " + error.getMessage())
                .setNeutralButton("OK", null)
                .show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            JSONArray jsonArray = new JSONArray(response.toString());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            Police police = new Police(jsonObject);
            UserLogged.getInstance().setPolice(police);
            try {
                Thread.sleep(1000);
                closeProgress();
            } catch (InterruptedException e) {
                Log.d(this.getClass().getSimpleName(), "error: " + e.getMessage());
            }
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Erro ao tentar logar")
                    .setMessage("Ocorreu um erro ao tentar logar: " + e.getMessage())
                    .setNeutralButton("OK", null)
                    .show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(this.getClass().getSimpleName());
        }
    }

    private Dialog progress;

    private void openDialog() {
        progress = new Dialog(this);
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progress.setContentView(R.layout.dialog_progress);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.setCancelable(false);
        final ProgressBar progressBar = ProgressBar.class.cast(progress.findViewById(R.id.progressBar));
        progressBar.setVisibility(View.VISIBLE);
        final TextView textView = progress.findViewById(R.id.textView);
        textView.setText("Entrando...");
        progress.show();
    }

    private void closeProgress() {
        progress.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
