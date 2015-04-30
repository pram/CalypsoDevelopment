package com.naughtyzombie.inmemds.calypso;

/**
 * Created by IntelliJ IDEA.
 * User: attalep
 * Date: 09/11/11
 * Time: 10:56
 */
import com.calypso.tk.util.cache.*;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class InMemCache implements Cache {

    private String name = "InMemDSCache";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxSize() {
        return 0;
    }

    public void setMaxSize(int i) {
    }

    public Object peek(Object object) {
        return object;
    }

    public void put(Cacheable cacheable) {
    }

    public Object put(Object object, Object object1) {
        return object;
    }

    public void putAll(Map map) {
    }

    public CacheMetrics getCacheMetrics() {
        return new CacheMetrics();
    }

    public void addCacheListener(CacheListener cacheListener) {
    }

    public boolean removeCacheListener(CacheListener cacheListener) {
        return false;
    }

    public void setCacheValidator(CacheValidator cacheValidator) {
    }

    public CacheValidator getCacheValidator() {
        return null;
    }

    public Object get(Object object) {
        return null;
    }

    public void clear() {
    }

    public Set keySet() {
        return new HashSet();
    }

    public int size() {
        return 0;
    }

    public boolean containsKey(Object object) {
        return false;
    }

    public boolean isEmpty() {
        return true;
    }

    public Object remove(Object object) {
        return null;
    }
}
