package jsontools.serializers;

import com.google.gson.JsonElement;
import jsontools.Jsonable;
import utilities.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * Serialize and deserialzie Jsonable classes
 * Created by Kihz on 2020-10-31
 */
public class JsonableSerializer extends Serializer<Jsonable> {

    public JsonableSerializer() {
        super(Jsonable.class);
    }

    @Override
    public Jsonable deserialize(JsonElement jsonElement, Class<Jsonable> loadClass, Field field) {
        Jsonable jsonable = ReflectionUtil.construct(loadClass);
        jsonable.load(jsonElement);
        return jsonable;
    }

    @Override
    public JsonElement serialize(Jsonable value) {
        return value.save();
    }
}
