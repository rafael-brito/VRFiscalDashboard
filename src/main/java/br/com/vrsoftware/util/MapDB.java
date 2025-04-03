package br.com.vrsoftware.util;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class MapDB<K, V> implements Closeable {

    private static final String DB_FILE = "config/mapdb.db";
    private static DB db;
    private final Map<K, V> map;

    public MapDB(String mapName, Serializer<K> keySerializer, Serializer<V> valueSerializer) throws IOException {
        ensureConfigDirectoryExists();

        db = DBMaker.fileDB(DB_FILE).transactionEnable().make();

        map = db.hashMap(mapName)
            .keySerializer(keySerializer)
            .valueSerializer(valueSerializer)
            .createOrOpen();
    }

    public void put(K key, V value) {
        if (map != null) {
            map.put(key, value);
        }
    }

    public void remove(K key) {
        if (map != null) {
            map.remove(key);
        }
    }

    public V get(K key) {
        return map != null ? map.get(key) : null;
    }

    public boolean containsKey(K key) {
        return map != null && map.containsKey(key);
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public static void commit() {
        if (db != null) {
            db.commit();
        }
    }

    public static void rollback() {
        if (db != null) {
            db.rollback();
        }
    }

    private void ensureConfigDirectoryExists() throws IOException {
        Path configDir = Paths.get(System.getProperty("user.dir"), "config");
        Files.createDirectories(configDir);
    }
}
