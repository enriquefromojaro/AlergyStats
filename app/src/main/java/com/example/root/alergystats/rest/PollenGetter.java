package com.example.root.alergystats.rest;

import com.example.root.alergystats.rest.models.PollenData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PollenGetter {

    @GET("/dataset/{dataset}/resource/{res}/download/{data_file}")
    Call<List<PollenData>> getPollenData(@Path("dataset") String dataSet, @Path("res") String Resource,
                              @Path("data_file") String file);
}
