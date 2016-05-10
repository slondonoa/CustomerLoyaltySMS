package com.lawyer.customerloyaltysms.services;

import com.lawyer.customerloyaltysms.entities.Customer_entity;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by stiven on 5/9/2016.
 */
public interface CustomerLoyalty_service {
    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    //i.e. http://localhost/api/institute/Students
    @GET("/CostumerLoyalty")
    public void getCostumers(Callback<List<Customer_entity>> callback);

}
