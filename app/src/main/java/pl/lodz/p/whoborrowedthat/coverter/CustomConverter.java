package pl.lodz.p.whoborrowedthat.coverter;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.lodz.p.whoborrowedthat.model.Data;
import retrofit2.Converter;

class CustomConverter<T> implements Converter<ResponseBody, T> {
    private final Converter<ResponseBody, Data<T>> delegate;

    CustomConverter(Converter<ResponseBody, Data<T>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        Data<T> data = delegate.convert(value);
        return data.getData();
    }
}
