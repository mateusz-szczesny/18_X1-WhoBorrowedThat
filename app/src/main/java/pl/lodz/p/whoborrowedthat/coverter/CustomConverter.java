package pl.lodz.p.whoborrowedthat.coverter;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.lodz.p.whoborrowedthat.model.Data;
import retrofit2.Converter;

public class CustomConverter<T> implements Converter<ResponseBody, T> {
    private final Converter<ResponseBody, Data<T>> delegate;

    public CustomConverter(Converter<ResponseBody, Data<T>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Data<T> data = delegate.convert(value);
        return data.getData();
    }
}
