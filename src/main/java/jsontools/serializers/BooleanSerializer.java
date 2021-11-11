package jsontools.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * Store and load boolean data into JSON.
 * Created by Kihz on 2020-10-31
 */
public class BooleanSerializer extends Serializer<Boolean> {

    public BooleanSerializer() {
        super(Boolean.class);
    }

    @Override
    public Boolean deserialize(JsonElement jsonElement, Class<Boolean> loadClass, Field field) {
        return jsonElement.getAsBoolean();
    }

    @Override
    public JsonElement serialize(Boolean value) {
        return new JsonPrimitive(value);
    }

    @Override
    public boolean canApplyTo(Class<?> clazz) {
        return super.canApplyTo(clazz) || clazz.equals(Boolean.TYPE);
    }
}
