package jsontools.serializers;

import com.google.gson.JsonElement;
import jsontools.containers.JsonPriorityQueue;
import utilities.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * Store and load queue data into JSON
 * Created by Kihz on 2021-01-26
 */
public class PriorityQueueSerializer extends Serializer<JsonPriorityQueue> {

    public PriorityQueueSerializer() {
        super(JsonPriorityQueue.class);
    }

    @Override
    public JsonPriorityQueue deserialize(JsonElement jsonElement, Class<JsonPriorityQueue> loadClass, Field field) {
        JsonPriorityQueue jsonPriorityQueue = new JsonPriorityQueue(ReflectionUtil.getGenericType(field));
        jsonPriorityQueue.load(jsonElement);
        return jsonPriorityQueue;
    }

    @Override
    public JsonElement serialize(JsonPriorityQueue value) {
        return value.save();
    }
}
