package com.smartqueue2.mainpage.gson;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2018/6/26.
 */

public class QueueResponseConverter<T> implements Converter<ResponseBody, T>
{
    private Type type;
    Gson gson = new Gson();

    public QueueResponseConverter(Type type)
    {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException
    {
        String result = responseBody.string();
        T queue = gson.fromJson(result, type);
        return queue;
    }
}
