package com.example.androidenklima.login.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Police implements Serializable {
    private String id;
    private String name;
    private String code;
    private Integer numberOcorrencia;
    private boolean isAdmin;

    public Police(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("_id");
        this.name = jsonObject.getString("name");
        this.code = jsonObject.getString("code");
        this.numberOcorrencia = jsonObject.getInt("number_ocorrencia");
        this.isAdmin = jsonObject.getBoolean("is_admin");
    }

    public Police(String id, String name, String code, boolean isAdmin, Integer numberOcorrencia) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isAdmin = isAdmin;
        this.numberOcorrencia = numberOcorrencia;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumberOcorrencia() {
        return numberOcorrencia;
    }

    public void setNumberOcorrencia(Integer numberOcorrencia) {
        this.numberOcorrencia = numberOcorrencia;
    }
}
