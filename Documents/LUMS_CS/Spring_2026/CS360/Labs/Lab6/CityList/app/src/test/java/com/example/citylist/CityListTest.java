package com.example.citylist;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CityListTest {
    private CityList cityList;

    private City mockCity() {
        return new City("Edmonton", "Alberta");
    }

    @Before
    public void setUp() {
        cityList = new CityList();
        cityList.add(mockCity());
    }

    @Test
    public void testAdd() {
        assertEquals(1, cityList.getCities().size());
        City city = new City("Regina", "Saskatchewan");
        cityList.add(city);
        assertEquals(2, cityList.getCities().size());
        assertTrue(cityList.getCities().contains(city));
    }

    @Test
    public void testAddException_whenDuplicate() {
        City city = new City("Yellowknife", "Northwest Territories");
        cityList.add(city);
        try {
            cityList.add(city);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testGetCities_sortedByCityName() {
        assertEquals(0, mockCity().compareTo(cityList.getCities().get(0)));
        City city = new City("Charlottetown", "Prince Edward Island");
        cityList.add(city);
        assertEquals(0, city.compareTo(cityList.getCities().get(0)));
        assertEquals(0, mockCity().compareTo(cityList.getCities().get(1)));
    }

    @Test
    public void testHasCity_trueWhenPresent() {
        assertTrue(cityList.hasCity(new City("Edmonton", "Alberta")));
    }

    @Test
    public void testHasCity_falseWhenNotPresent() {
        assertFalse(cityList.hasCity(new City("Lahore", "Punjab")));
    }

    @Test
    public void testDelete_removesExistingCity() {
        City toDelete = new City("Edmonton", "Alberta");
        assertTrue(cityList.hasCity(toDelete));
        cityList.delete(toDelete);
        assertFalse(cityList.hasCity(toDelete));
        assertEquals(0, cityList.countCities());
    }

    @Test
    public void testDelete_throwsWhenCityMissing() {
        City missing = new City("Karachi", "Sindh");
        try {
            cityList.delete(missing);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testCountCities_updatesCorrectly() {
        assertEquals(1, cityList.countCities());
        cityList.add(new City("Islamabad", "ICT"));
        assertEquals(2, cityList.countCities());
        cityList.delete(new City("Islamabad", "ICT"));
        assertEquals(1, cityList.countCities());
    }
}