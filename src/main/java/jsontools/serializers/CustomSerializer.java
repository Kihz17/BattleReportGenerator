package jsontools.serializers;

import com.google.gson.JsonElement;
import utilities.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * A custom serializer that will mostly be used for java objects (Integer, Double, Float, etc)
 * This class makes use of the Function<T, R> interface so that we can take an argument(T) and produce a result (R) all in one line.
 * Created by Kihz on 2020-10-31
 */
public class CustomSerializer<T> extends Serializer<T> {
    private Function<JsonElement, T> deserialize;
    private Function<T, JsonElement> serialize;
    private Function<String, T> parse;
    private T fallbackValue;

    public CustomSerializer(Class<T> clazz, Function<JsonElement, T> deserialize, Function<T, JsonElement> serialize, Function<String, T> parse, T fallbackValue) {
        super(clazz);
        this.deserialize = deserialize;
        this.serialize = serialize;
        this.parse = parse;
        this.fallbackValue = fallbackValue;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Class<T> loadClass, Field field) {
        return deserialize.apply(jsonElement);
    }

    @Override
    public JsonElement serialize(T value) {
        return serialize.apply(value);
    }

    @Override
    public boolean canApplyTo(Class<?> clazz) {
        return super.canApplyTo(clazz) || (clazz.isPrimitive() && ReflectionUtil.getPrimitive(getApplyTo()).isAssignableFrom(clazz));
    }
}
