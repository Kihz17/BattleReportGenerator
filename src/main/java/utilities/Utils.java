package utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Cleanup;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Kihz on 2021-11-07
 */
public class Utils {

    public static final Random RANDOM = new Random();
    private static final Map<Class, Method> enumResolveMap = new ConcurrentHashMap<>();

    /**
     * Is this json element null?
     * @param jsonElement The element to test
     * @return isJsonNull
     */
    public static boolean isJsonNull(JsonElement jsonElement) {
        return jsonElement == null || jsonElement.isJsonNull();
    }

    /**
     * Read JSON data from a file.
     * @param file The file to read from.
     * @return jsonElement
     */
    public static JsonElement readJsonFile(@NonNull File file) {
        try {
            @Cleanup FileReader fileReader = new FileReader(file);
            @Cleanup BufferedReader bufferedReader = new BufferedReader(fileReader);
            return JsonParser.parseReader(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read JSON file " + file.getName() + ".");
            return null;
        }
    }

    /**
     * Verify a condition is true. If false, it will throw a GeneralException. This is safe to use, it won't allocate objects.
     * @param condition The condition to verify.
     * @param error     The error description.
     */
    @SuppressWarnings("ConstantConditions") // Explained below.
    public static void verify(boolean condition, String error) {
        assert condition; // Our IDE will not give warnings handled by this method if use assert. However, we tell the JVM to disable assert so our own custom exceptions are used at run-time.
        if (!condition)
            throw new GeneralException(error);
    }

    /**
     * Verify an object is not null. Otherwise, it will throw a GeneralException.
     * This is preferable to using var-args because it won't create an array object each time.
     * @param object The object to confirm is not null.
     * @param error  The error
     */
    @SuppressWarnings("ConstantConditions")
    public static void verifyNotNull(Object object, String error) {
        assert object != null; // Our IDE will not give warnings handled by this method if use assert. However, we tell the JVM to disable assert so our own custom exceptions are used at run-time.
        if (object == null)
            throw new GeneralException(error);
    }

    /**
     * Gets an enum value from the given class. Returns null if not found.
     * @param value The enum's raw name.
     * @param clazz The enum's class.
     */
    public static <T extends Enum<T>> T getEnum(String value, Class<T> clazz) {
        return getEnum(value, clazz, null);
    }

    /**
     * Gets an enum value, falling back on a default value.
     * @param value        The enum's raw name.
     * @param defaultValue The default enum value, if value was null.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getEnum(String value, T defaultValue) {
        return getEnum(value, (Class<T>) defaultValue.getClass(), defaultValue);
    }

    @SuppressWarnings({"unchecked", "EmptyCatchBlock"})
    public static <T extends Enum<T>> T getEnum(String enumName, Class<T> enumClass, T defaultValue) {
        verifyNotNull(enumClass, "Tried to get an enum value with a null enum class.");

        if (enumName != null && enumName.length() > 0) {
            try {
                Method resolveMethod = enumResolveMap.get(enumClass);
                if (resolveMethod == null)
                    enumResolveMap.put(enumClass, resolveMethod = enumClass.getMethod("valueOf", String.class));

                return (T) resolveMethod.invoke(null, enumName);
            } catch (Exception e) {

            }
        }

        if (defaultValue == null) // Such as if an enum was removed.
            System.out.println("Unknown enum value '" + enumName + "' in " + enumClass.getSimpleName() + ".");
        return defaultValue;
    }

    /**
     * Gets the first key whose indexed value matches the passed value.
     * @param map The map to search keys from.
     * @param value The value to search with.
     * @return indexKey
     */
    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (K key : map.keySet())
            if (value.equals(map.get(key)))
                return key;
        return null;
    }

    /**
     * Get the Random object.
     * @return random
     */
    public static Random getRandom() {
        return RANDOM;
    }

    /**
     * Get a random number between the given range.  The min and max parameters are inclusive.
     * @param min The minimum wanted number.
     * @param max The maximum wanted number.
     * @return int
     */
    public static int randInt(int min, int max) {
        if (min >= max)
            return max;
        return max + min > 0 ? nextInt(max - min + 1) + min : randInt(0, max - min) + min;
    }

    /**
     * Generate a random float between a float range.
     * @param min The minimum wanted float.
     * @param max The maximum wanted float.
     * @return point
     */
    public static float randFloat(float min, float max) {
        return getRandom().nextFloat() * (max - min) + min;
    }

    /**
     * A random double between 0 and 1.
     * @return randomDouble
     */
    public static double randDouble() {
        return getRandom().nextDouble();
    }

    /**
     * Generate a random number between two doubles.
     * @param min The minimum wanted double.
     * @param max The maximum wanted double.
     * @return rand
     */
    public static double randDouble(double min, double max) {
        return min == max ? min : ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * Generate a random number between two longs.
     * @param min The minimum wanted long.
     * @param max The maximum wanted long.
     * @return rand
     */
    public static long randLong(long min, long max) {
        return min == max ? min : ThreadLocalRandom.current().nextLong(min, max);
    }

    /**
     * Gets a random number between 0 and max - 1
     * @param max The maximum value + 1. For instance, nextInt(3) would give values in the range [0,3].
     * @return rand
     */
    public static int nextInt(int max) {
        return getRandom().nextInt(max);
    }

    /**
     * Check if a random chance succeeds.
     * @param chance 1 / chance = the % chance.
     * @return success
     */
    public static boolean randChance(int chance) {
        return chance <= 1 || nextInt(chance) == 0;
    }

    /**
     * Random chance based on a double value.
     * The closer the input is to zero, the lower the chance. The higher, the higher.
     * @param chance The chance between 0 and 1.
     * @return randChance
     */
    public static boolean randChance(double chance) {
        return nextDouble() <= chance;
    }

    /**
     * Get a random true/false value.
     * @return randomBool
     */
    public static boolean nextBool() {
        return randChance(2);
    }

    /**
     * Performs Random#nextDouble.
     * @return randomDouble
     */
    public static double nextDouble() {
        return getRandom().nextDouble();
    }

    /**
     * Performs Random#nextLong.
     * @return randomLong
     */
    public static double nextLong() {
        return getRandom().nextLong();
    }
}
