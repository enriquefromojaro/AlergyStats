package com.example.root.alergystats.rest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class PollenData {

    @SerializedName("Tipo polínico")
    @Expose
    private String type;

    @SerializedName("Fecha")
    @Expose
    private Date date;

    @SerializedName("Granos de polen por metro cúbico de aire y día")
    @Expose
    private int concentration;

    public HashMap<String, String> toHashMap(String dateFormat){
        DateFormat df = new SimpleDateFormat(dateFormat);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        map.put("concentration", String.valueOf(concentration));
        map.put("date", df.format(date));
        return map;
    }

    public HashMap<String, String> toHashMap(){
        return this.toHashMap("dd - MM - yyyy");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getConcentration() {
        return concentration;
    }

    public void setConcentration(int concentration) {
        this.concentration = concentration;
    }
}
