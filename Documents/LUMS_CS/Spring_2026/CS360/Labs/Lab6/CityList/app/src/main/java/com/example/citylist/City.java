package com.example.citylist;

import java.util.Objects;

public class City implements Comparable<City> {
    private final String city;
    private final String province;

    public City(String city, String province) {
        if (city == null || province == null) {
            throw new IllegalArgumentException("city and province must not be null");
        }
        this.city = city;
        this.province = province;
    }

    public String getCityName() {
        return city;
    }

    public String getProvinceName() {
        return province;
    }

    @Override
    public int compareTo(City other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        return this.city.compareTo(other.getCityName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City other = (City) o;
        return city.equals(other.city) && province.equals(other.province);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, province);
    }
}