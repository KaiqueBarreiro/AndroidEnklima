package com.example.androidenklima.consultar.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Ocorrencia implements Serializable {

    private String id;
    private String typeOcurreny;
    private String local;
    private boolean existsVitmin;
    private String namePolice;
    private String photo;
    private String description;
    private String data;

    public Ocorrencia(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("_id");
        typeOcurreny = jsonObject.getString("ocurrency_type");
        local = jsonObject.getString("local");
        existsVitmin = jsonObject.getBoolean("is_vitmin");
        namePolice = jsonObject.getString("name_police");
        photo = jsonObject.getString("photo");
        description = jsonObject.getString("description");
        data = jsonObject.getString("data");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeOcurreny() {
        return typeOcurreny;
    }

    public void setTypeOcurreny(String typeOcurreny) {
        this.typeOcurreny = typeOcurreny;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public boolean isExistsVitmin() {
        return existsVitmin;
    }

    public void setExistsVitmin(boolean existsVitmin) {
        this.existsVitmin = existsVitmin;
    }

    public String getNamePolice() {
        return namePolice;
    }

    public void setNamePolice(String namePolice) {
        this.namePolice = namePolice;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
