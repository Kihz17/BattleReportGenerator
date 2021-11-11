package jsontools.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import jsontools.JsonSerializer;
import utilities.ReflectionUtil;
import utilities.Utils;

import java.lang.reflect.Field;

/**
 * Store and load object data into JSON.
 * Created by Kihz on 2020-10-31
 */
public class ObjectSerializer extends Serializer<Object> {

    public ObjectSerializer() {
        super(Object.class);
    }

    @Override
    public Object deserialize(JsonElement jsonElement, Class<Object> loadClass, Field field) {
        Utils.verify(jsonElement.isJsonObject(), "JsonElement is not a JsonObject!");
        return deserializeFields(jsonElement.getAsJsonObject(), ReflectionUtil.forceConstruct(loadClass));
    }

    @Override
    public JsonElement serialize(Object value) {
        return serializeFields(value);
    }

    /**
     * Convert JSON data into a Java object
     * @param jsonObject The JsonObject to get the data from
     * @param object The object to convert to
     * @param <T> The object type
     * @return object
     */
    public static <T> T deserializeFields(JsonObject jsonObject, T object) {
        for(Field field : JsonSerializer.getFields(object)) {
            try {
                JsonElement jsonElement = jsonObject.get(field.getName());
                if(!Utils.isJsonNull(jsonElement))
                    field.set(object, JsonSerializer.deserialize(field.getType(), jsonElement, field));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return object;
    }

    /**
     * Convert object fields into JSON.
     * @param object The object to convert the fields of
     * @return jsonObject
     */
    public static JsonObject serializeFields(Object object) {
        JsonObject jsonObject = new JsonObject();
        for(Field field : JsonSerializer.getFields(object)) {
            try {
                JsonElement jsonElement = JsonSerializer.save(field.get(object));
                if(shouldStoreValue(jsonElement)) // If this value should be stored, add it to the object
                    jsonObject.add(field.getName(), jsonElement);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }

    /**
     * Should this json element be stored? Returns false if the json element equals Java's default value.
     * @param jsonElement The json element to test
     * @return shouldStoreValue
     */
    public static boolean shouldStoreValue(JsonElement jsonElement) {
        if(Utils.isJsonNull(jsonElement))  // Don't need to store null values
            return false;

        JsonPrimitive jsonPrimitive = jsonElement.isJsonPrimitive() ? jsonElement.getAsJsonPrimitive() : null;
        boolean isBooleanDefaultValue = jsonPrimitive != null && (jsonPrimitive.isBoolean() && !jsonPrimitive.getAsBoolean()); // Default bool value is false
        boolean isNumberDefaultValue = jsonPrimitive != null &&  (jsonPrimitive.isNumber() && jsonPrimitive.getAsNumber().doubleValue() == 0
                && jsonPrimitive.getAsNumber().longValue() == 0); // Default number value is 0
        boolean isArrayDefaultValue = jsonPrimitive != null && (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() == 0); // Empty array AKA: []
        boolean isObjectDefaultValue = jsonPrimitive != null && (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().size() == 0); // Empty object AKA: {}

        return !isBooleanDefaultValue || !isNumberDefaultValue || !isArrayDefaultValue || !isObjectDefaultValue;
    }
}
