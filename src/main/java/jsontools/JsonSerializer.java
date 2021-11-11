package jsontools;

import com.google.gson.*;
import jsontools.serializers.*;
import utilities.GeneralException;
import utilities.ReflectionUtil;
import utilities.Utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A tool to save and load data into Json.
 * We will cache all fields for later use because reflection is extremely slow (approximately 2x slower than direct access)
 * Created by Kihz on 2020-10-31
 */
public class JsonSerializer {
    private static List<Serializer<?>> handlers = new LinkedList<>();
    private static Map<Class<?>, List<Field>> fieldCache = new ConcurrentHashMap<>(); // Cache fields to improve performance
    public static final String CLASS_KEY = "class";

    public static final Gson GSON_PRETTY_PRINT = new GsonBuilder().setPrettyPrinting().create();

    // Register all of our Serializers on startup
    static {
        // SEE: CustomSerializer.class for an explanation of its use

        // PRIMITIVES
        addSerializer(new CustomSerializer<>(Byte.class, JsonElement::getAsByte, JsonPrimitive::new, Byte::parseByte, (byte) 0));
        addSerializer(new CustomSerializer<>(Short.class, JsonElement::getAsShort, JsonPrimitive::new, Short::parseShort, (short) 0));
        addSerializer(new CustomSerializer<>(Integer.class, JsonElement::getAsInt, JsonPrimitive::new, Integer::parseInt,  0));
        addSerializer(new CustomSerializer<>(Long.class, JsonElement::getAsLong, JsonPrimitive::new, Long::parseLong, (long) 0));
        addSerializer(new CustomSerializer<>(Float.class, JsonElement::getAsFloat, JsonPrimitive::new, Float::parseFloat, (float) 0));
        addSerializer(new CustomSerializer<>(Double.class, JsonElement::getAsDouble, JsonPrimitive::new, Double::parseDouble, (double) 0));

        addSerializer(new BooleanSerializer());

        // JAVA OBJECTS
        addSerializer(new CustomSerializer<>(String.class, JsonElement::getAsString, JsonPrimitive::new, null, null));
        addSerializer(new CustomSerializer<>(UUID.class, jsonElement -> UUID.fromString(jsonElement.getAsString()), id -> new JsonPrimitive(id.toString()), null, null));

        addSerializer(new MapSerializer());
        addSerializer(new EnumSerializer());
        addSerializer(new QueueSerializer());
        addSerializer(new PriorityQueueSerializer());
        addSerializer(new ListSerializer());
        addSerializer(new ArraySerializer());

        addSerializer(new JsonableSerializer());
        addSerializer(new GsonSerializer());

        addSerializer(new ObjectSerializer()); // This should be last
    }

    private static void addSerializer(Serializer<?> serializer) {
        handlers.add(serializer);
    }

    public static <T> T deserializeFromFile(Class<T> loadClass, File file) {
        return deserialize(loadClass, Utils.readJsonFile(file));
    }

    /**
     * Deserialize an object from JSON.
     * @param clazz The class of the object to load from.
     * @param jsonElement The jsonElement to load from.
     * @param <T> The class type
     * @return deserializedObject
     */
    public static <T> T deserialize(Class<T> clazz, JsonElement jsonElement) {
        return deserialize(clazz, jsonElement, null);
    }

    /**
     * Deserialize an object from JSON.
     * @param clazz The class of the object to load from.
     * @param jsonElement The jsonElement to load from.
     * @param field The field this object belongs to
     * @param <T> The class type
     * @return deserializedObject
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(Class<T> clazz, JsonElement jsonElement, Field field) {
        Utils.verifyNotNull(jsonElement, "JSON Element is null!");

        // If our data member is an object, grab its sub-class instead of the parent class
        if(jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has(CLASS_KEY)) {
            String classPath = jsonElement.getAsJsonObject().get(CLASS_KEY).getAsString();
            Class<T> clss = (Class<T>) ReflectionUtil.getClass(classPath);

            if(clss == null) {
                System.out.println("Class '" + classPath + "' is null. Defaulting to '" + clazz + "'.");
            } else {
                clazz = clss;
            }
        }

        Utils.verifyNotNull(clazz, "Attempted to load into json without a class!");

        try {
            Serializer<T> handler = getHandler(clazz);
            return handler.deserialize(jsonElement, clazz, field);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Serialize a Java object into JSON.
     * @param object The object to serialize
     * @return jsonElement
     */
    @SuppressWarnings("unchecked")
    public static JsonElement save(Object object) {
        if(object == null) // Avoid nullpointers, while still being able to save data as "null"
            return JsonNull.INSTANCE;

        // Get the object's corresponding Serializer class and turn it into a JsonElement
        Serializer serializer = getHandler(object.getClass());
        JsonElement jsonElement = serializer.serialize(object);
        return jsonElement != null ? jsonElement : JsonNull.INSTANCE;
    }

    /**
     * Return the proper serializer for the given class.
     * @param clazz The class to get the serializer for.
     * @return handler
     */
    public static Serializer getHandler(Class<?> clazz) {
        for(Serializer<?> handler : handlers)
            if(handler.canApplyTo(clazz))
                return handler;
        throw new GeneralException("Failed to get a handler for " + clazz.getSimpleName() + "!");
    }

    /**
     * Identify the json object's class, where we are sure the object has no parent.
     * @param object The object we want to register tha class from.
     * @param jsonElement The json element to register the class in.
     * @return jsonElement
     */
    public static JsonElement addClassNoParent(Object object, JsonElement jsonElement) {
        return addClass(object, null, jsonElement);
    }

    /**
     * Identify a json object's class. Tries to get its parent class.
     * @param object The object we want to register tha class from.
     * @param json The json element to register the class in.
     * @return jsonElement
     */
    public static JsonElement addClass(Object object, JsonElement json) {
        return addClass(object, getJsonParent(object), json);
    }

    /**
     * Identify the json object's class. Register its sub class if applicable.
     * @param object The object we want to register tha class from.
     * @param parent The parent class. If this is null then we will register the object's class.
     * @param jsonElement The json element to register the class in.
     * @return jsonElement
     */
    public static JsonElement addClass(Object object, Class<?> parent, JsonElement jsonElement) {
        // If our element is an object and the object's class isn't equal to the parent class then record the object as a sub class
        if(jsonElement.isJsonObject() && (parent == null || !object.getClass().getName().equals(parent.getName())))
            jsonElement.getAsJsonObject().addProperty(CLASS_KEY, object.getClass().getName());

        return jsonElement;
    }

    /**
     * Find the highest class in the given objects hierarchy.
     * @param object The object to find the superclass for
     * @return parentClass
     */
    public static Class<?> getJsonParent(Object object) {
        Class<?> parentClass = object.getClass();
        while (Jsonable.class.isInstance(parentClass.getSuperclass())) // Find the highest superclass in this hierarchy
            parentClass = parentClass.getSuperclass();

        return parentClass;
    }

    /**
     * Get all relevant fields from an object. Filter out static and transient members.
     * This method will cache the fields if they do not exist in fieldCache already using computeIfAbsent
     * @param object The object to get all fields for.
     * @return fields
     */
    public static List<Field> getFields(Object object) {
        return fieldCache.computeIfAbsent(object.getClass(), objectClass ->
                ReflectionUtil.getAllFields(objectClass)).stream()
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> !Modifier.isTransient(field.getModifiers()))
                .collect(Collectors.toList());
    }
}
