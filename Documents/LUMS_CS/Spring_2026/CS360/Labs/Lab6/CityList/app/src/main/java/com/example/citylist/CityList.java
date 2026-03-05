package com.example.citylist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CityList {
    private final List<City> cities = new ArrayList<>();

    public void add(City city) {
        if (city == null) {
            throw new IllegalArgumentException("city must not be null");
        }
        if (cities.contains(city)) {
            throw new IllegalArgumentException("City already exists");
        }
        cities.add(city);
    }

    public List<City> getCities() {
        List<City> copy = new ArrayList<>(cities);
        Collections.sort(copy);
        return copy;
    }

    public boolean hasCity(City city) {
        if (city == null) {
            throw new IllegalArgumentException("city must not be null");
        }
        return cities.contains(city);
    }

    public void delete(City city) {
        if (city == null) {
            throw new IllegalArgumentException("city must not be null");
        }
        boolean removed = cities.remove(city);
        if (!removed) {
            throw new IllegalArgumentException("City not found");
        }
    }

    public int countCities() {
        return cities.size();
    }
}