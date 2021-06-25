package com.satvick.utils;

import com.satvick.model.UpdateProfileModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddRequestBody<T> {
    private MediaType mediaType = MediaType.parse("text/plain");
    private Map<String, RequestBody> requestBodyMap = new HashMap<>();

    public AddRequestBody(T data)
    {
        if(data instanceof UpdateProfileModel){
            requestBodyMap.put("email", RequestBody.create(mediaType, ((UpdateProfileModel) data).getEmail()));
            requestBodyMap.put("phone", RequestBody.create(mediaType, ((UpdateProfileModel) data).getPhoneNo()));
            requestBodyMap.put("fullname", RequestBody.create(mediaType, ((UpdateProfileModel) data).getName()));
            requestBodyMap.put("gender", RequestBody.create(mediaType, ((UpdateProfileModel) data).getGender()));
            requestBodyMap.put("dob", RequestBody.create(mediaType, ((UpdateProfileModel) data).getDOB()));
            requestBodyMap.put("user_id", RequestBody.create(mediaType, ((UpdateProfileModel) data).getId()));
            requestBodyMap.put("token", RequestBody.create(mediaType, ((UpdateProfileModel) data).getToken()));
        }
    }

    public Map<String, RequestBody> getBody()
    {
        return requestBodyMap;
    }
}