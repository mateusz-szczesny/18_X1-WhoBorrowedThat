package pl.lodz.p.whoborrowedthat.coverter;

import android.support.annotation.Nullable;

import com.squareup.moshi.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import pl.lodz.p.whoborrowedthat.model.Data;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CustomConverterFactory extends Converter.Factory {
    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Type dataType = Types.newParameterizedType(Data.class, type);
        Converter<ResponseBody, Data> delegate = retrofit.nextResponseBodyConverter(this, dataType, annotations);
        return new CustomConverter(delegate);
    }
}
