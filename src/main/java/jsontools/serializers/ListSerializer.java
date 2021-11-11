package jsontools.serializers;

import com.google.gson.JsonElement;
import jsontools.containers.JsonList;
import utilities.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * Store and load list data into JSON.
 * Created by Kihz on 2020-10-31
 */
public class ListSerializer extends Serializer<JsonList> {

    public ListSerializer() {
        super(JsonList.class);
    }

    @Override
    public JsonList deserialize(JsonElement jsonElement, Class<JsonList> loadClass, Field field) {
        JsonList jsonList = new JsonList(ReflectionUtil.getGenericType(field));
        jsonList.load(jsonElement);
        return jsonList;
    }

    @Override
    public JsonElement serialize(JsonList value) {
        return value.save();
    }
}
