package jsontools.serializers;

import com.google.gson.JsonElement;

import java.lang.reflect.Field;

/**
 * Store and load JSON data into JSON data.
 * Created by Kihz on 2020-10-31
 */
public class GsonSerializer extends Serializer<JsonElement> {

    public GsonSerializer() {
        super(JsonElement.class);
    }

    @Override
    public JsonElement deserialize(JsonElement jsonElement, Class<JsonElement> loadClass, Field field) {
        return jsonElement;
    }

    @Override
    public JsonElement serialize(JsonElement value) {
        return value;
    }
}
