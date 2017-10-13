package com.lawyer.customerloyaltysms.services;

/**
 * Created by stiven on 5/9/2016.
 */
public class RestService {
    //You need to change the IP if you testing environment is not local machine
    //or you may have different URL than we have here
    private static final String URL = "http://abogadospensionesapi.azurewebsites.net/api/";
    private retrofit.RestAdapter restAdapter;
    private CustomerLoyalty_service apiService;

    public RestService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(CustomerLoyalty_service.class);
    }

    public CustomerLoyalty_service getService()
    {
        return apiService;
    }

}
