package com.cirrustech.events.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests for {@link SystemPropertiesUtils} class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemPropertiesUtils.class, System.class})
public class SystemPropertiesUtilsTest {

    @Test
    public void testHasPropertySetReturnsFalse() {

        //setup
        mockStatic(System.class);
        when(System.getProperties()).thenReturn(new Properties());

        //call
        boolean hasValue = SystemPropertiesUtils.hasPropertySet("anyValue");

        //verify
        assertFalse(hasValue);
        verifyStatic();
    }

    @Test
    public void testHasPropertySetReturnsTrue() {

        //setup
        Properties properties = new Properties();
        properties.setProperty("anyKey", "aValue");

        mockStatic(System.class);
        when(System.getProperties()).thenReturn(properties);

        //call
        boolean hasValue = SystemPropertiesUtils.hasPropertySet("anyKey");

        //verify
        assertTrue(hasValue);
        verifyStatic();
    }

    @Test
    public void testGetPropertyValueReturnsNull() {

        //setup
        mockStatic(System.class);
        when(System.getProperty("anyKey")).thenReturn(null);

        //call
        String value = SystemPropertiesUtils.getPropertyValue("anyValue");

        //verify
        assertNull(value);
        verifyStatic();
    }

    @Test
    public void testGetPropertyValueReturnsValue() {

        //setup
        mockStatic(System.class);
        when(System.getProperty("anyKey")).thenReturn("foo");

        //call
        String value = SystemPropertiesUtils.getPropertyValue("anyKey");

        //verify
        assertNotNull(value);
        assertEquals(value, "foo");
        verifyStatic();
    }
}
