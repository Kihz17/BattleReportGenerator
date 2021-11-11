package jsontools.containers;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A concurrent map that is able to be serialized and deserialized to JSON. (thread safe)
 * Created by Kihz on 2020-11-02
 */
public class ConcurrentJsonMap<V> extends JsonMap<V> {

    public ConcurrentJsonMap() {
        super(new ConcurrentHashMap<>());
    }
}
