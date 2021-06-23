package com.openclassrooms.realestatemanager;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;
import static org.junit.Assert.assertEquals;

public class UtilsUnitTest {

    private SimpleDateFormat mDate = new  SimpleDateFormat("dd/MM/yyyy");

    @Test
    public void convertEuroToDollarTest() {
        assertEquals(1190, Utils.convertEuroToDollar(1000));
    }

    @Test
    public void convertDollarToEuroTest() {
        assertEquals(812, Utils.convertDollarToEuro(1000));
    }

    @Test
    public void getTodayDateTest() {
        String date = Utils.getTodayDate();
        assertEquals(mDate.format(new Date()), date);
    }
}
