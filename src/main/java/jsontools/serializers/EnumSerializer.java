package jsontools.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import utilities.Utils;

import java.lang.reflect.Field;

/**
 * Store and load enum data into JSON.
 * Created by Kihz on 2020-10-31
 */
public class EnumSerializer extends Serializer<Enum> {

    public EnumSerializer() {
        super(Enum.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enum deserialize(JsonElement jsonElement, Class<Enum> loadClass, Field field) {
        return Utils.getEnum(jsonElement.getAsString(), loadClass);
    }

    @Override
    public JsonElement serialize(Enum value) {
        return new JsonPrimitive(value.name());
    }

    @Override
    public boolean canApplyTo(Class<?> clazz) {
        return super.canApplyTo(clazz) | clazz.equals(Boolean.TYPE);
    }
}
