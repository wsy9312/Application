package com.sd.storage.common;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by MrZhou on 2016/10/15.
 */
public class SDGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    SDGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonParser parser = new JsonParser();
         String ddd= HTMLSpirit.delHTMLTag(value.string());
        JsonObject jsonObject = parser.parse(ddd).getAsJsonObject();
       // JsonObject jsonObject = parser.parse(HTMLSpirit.delHTMLTag(value.string())).getAsJsonObject();
        Log.i("","");
        // JsonObject jsonObject = parser.parse(value.string()).getAsJsonObject();

      /*  JsonElement dataElement = jsonObject.get(Constants.DATA) ;
        if(null != dataElement  && dataElement.isJsonArray()){
            JsonArray jsonArray = dataElement.getAsJsonArray();
            if(jsonArray.size() == 0){
                jsonObject.remove(Constants.DATA);
            }
        }else if(dataElement.isJsonObject()){
            JsonObject object = dataElement.getAsJsonObject();
            if(object.entrySet().isEmpty()){
                jsonObject.remove(Constants.DATA);
            }
        }*/

        try {
            return adapter.fromJsonTree(jsonObject);
        } finally {
            value.close();
        }
    }
}
