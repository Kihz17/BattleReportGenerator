package jsontools.containers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import jsontools.JsonSerializer;
import jsontools.Jsonable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utilities.Utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A queue that can be serialized and deserialized to JSON
 * Created by Kihz on 2021-02-01
 */
@NoArgsConstructor @Getter
public class JsonQueue<T> implements Jsonable, Iterable {
    private transient Queue<T> queue = new LinkedList<>();
    @Setter private Class<T> typeClass;

    public JsonQueue(Iterable<T> values) {
        values.forEach(getQueue()::offer);
    }

    public JsonQueue(Class<T> classType) {
        this.typeClass = classType;
    }

    @Override
    public void load(JsonElement array) {
        for (JsonElement element : array.getAsJsonArray()) {
            if (Utils.isJsonNull(element))
                continue;

            T loadedValue = JsonSerializer.deserialize(getTypeClass(), element);
            if (loadedValue != null)
                getQueue().offer(loadedValue);
        }
    }

    @Override
    public JsonElement save() {
        JsonArray array = new JsonArray();
        for (T value : getQueue()) {
            JsonElement savedValue = JsonSerializer.addClassNoParent(value, JsonSerializer.save(value));
            if (!Utils.isJsonNull(savedValue))
                array.add(savedValue);
        }
        return array;
    }

    /**
     * Offer a value to this queue
     * @param value The value to add
     * @return success
     */
    public boolean offer(T value) {
        return getQueue().offer(value);
    }

    /**
     * is this queue empty?
     * @return isEmpty
     */
    public boolean isEmpty() {
        return getQueue().isEmpty();
    }

    /**
     * Remove the head of the queue
     * @return removedItem
     */
    public T remove() {
        return getQueue().remove();
    }

    @Override
    public Iterator iterator() {
        return getQueue().iterator();
    }
}
