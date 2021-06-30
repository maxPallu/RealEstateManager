package com.openclassrooms.realestatemanager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.util.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NetworkConnectionTest {

    @Test
    public void checkInternetConnection() {
        assertEquals(true, Utils.isInternetAvailable(InstrumentationRegistry.getInstrumentation().getContext()));
    }
}
