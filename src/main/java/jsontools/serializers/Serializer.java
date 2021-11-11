package jsontools.serializers;

import com.google.gson.JsonElement;
import lombok.Getter;

import java.lang.reflect.Field;

/**
 * Templated serializer used for loading and storing JSON.
 * Created by Kihz on 2020-10-31
 */
@Getter
public abstract class Serializer<T> {
    private Class<? extends T> applyTo;

    public Serializer(Class<T> apply) {
        this.applyTo = apply;
    }

    /**
     * Deserialize json element into an object.
     * @param jsonElement The element to deserialize
     * @param loadClass The class to load the data into
     * @return value
     */
    public abstract T deserialize(JsonElement jsonElement, Class<T> loadClass, Field field);

    /**
     * Serialize a java object into a JsonElement
     * @param value The value to serialize
     * @return jsonElement
     */
    public abstract JsonElement serialize(T value);

    /**
     * Verify that the given class can be handled by the current handler.
     * @param clazz The class to test
     * @return canApply
     */
    public boolean canApplyTo(Class<?> clazz) {
        return getApplyTo() != null && getApplyTo().isAssignableFrom(clazz);
    }

}
