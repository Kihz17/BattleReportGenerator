package jsontools.serializers;

import com.google.gson.JsonElement;
import jsontools.containers.JsonMap;
import utilities.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * Store and load map data into JSON.
 * Created by Kihz on 2020-11-02
 */
public class MapSerializer extends Serializer<JsonMap> {

    public MapSerializer() {
        super(JsonMap.class);
    }

    @Override
    public JsonMap deserialize(JsonElement jsonElement, Class<JsonMap> loadClass, Field field) {
        JsonMap jsonMap = new JsonMap(ReflectionUtil.getGenericType(field));
        jsonMap.load(jsonElement);
        return jsonMap;
    }

    @Override
    public JsonElement serialize(JsonMap value) {
        return value.save();
    }
}
