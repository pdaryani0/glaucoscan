package com.glaucoma.ai.data.api;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> extends SimpleApiResponse {
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "data=" + data +
                '}';
    }
}