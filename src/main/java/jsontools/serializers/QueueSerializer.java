package jsontools.serializers;

import com.google.gson.JsonElement;
import jsontools.containers.JsonQueue;
import utilities.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * A serializer for a JSON Queue
 * Created by Kihz on 2021-02-01
 */
public class QueueSerializer extends Serializer<JsonQueue> {

    public QueueSerializer() {
        super(JsonQueue.class);
    }

    @Override
    public JsonQueue deserialize(JsonElement jsonElement, Class<JsonQueue> loadClass, Field field) {
        JsonQueue jsonQueue = new JsonQueue(ReflectionUtil.getGenericType(field));
        jsonQueue.load(jsonElement);
        return jsonQueue;
    }

    @Override
    public JsonElement serialize(JsonQueue value) {
        return value.save();
    }
}
