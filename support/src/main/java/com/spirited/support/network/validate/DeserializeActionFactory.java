package com.spirited.support.network.validate;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DeserializeActionFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        // 获取其他低优先级Factory创建的DelegateAdapter
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        // 如果type实现了DeserializeAction，则返回包裹后的TypeAdapter
        if (shouldWrap(type.getRawType())) {

            return new TypeAdapter<T>() {

                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                }

                public T read(JsonReader in) throws IOException {
                    T t = delegate.read(in);
                    if (isInvalidData(t)) {
                        return null;
                    }
                    doAfterDeserialize(t);
                    return t;
                }
            };

        } else {
            return delegate;
        }
    }

    public static boolean isInvalidData(Object t) {
        if (t instanceof IDataValidateAction) {
            if (!((IDataValidateAction) t).isDataValid()) {
                return true;
            }
        }
        return false;
    }

    public static <T> void doAfterDeserialize(Object t) {
        if (t instanceof IAfterDeserializeAction) {
            ((IAfterDeserializeAction) t).doAfterDeserialize();
        }
    }

    private boolean shouldWrap(Class clazz) {
        return IAfterDeserializeAction.class.isAssignableFrom(clazz) ||
                IDataValidateAction.class.isAssignableFrom(clazz);
    }
}
