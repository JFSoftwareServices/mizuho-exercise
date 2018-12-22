package com.mizuho.io.dao;

import java.util.List;

/**
 *
 * @param <T>
 */
public interface Dao<T> {

    T get(long id);

    List<T> findByVendor(String vendor);

    List<T> findByTicker(String name);

    void save(T t);

    void update(T t, String[] params);

    void deleteOlderThanDays(int days);
}