package com.cirrustech.events.utils;

/**
 * Helper class for handling system properties.
 */
public class SystemPropertiesUtils {

    /**
     * Private constructor to avoid class init.
     */
    private SystemPropertiesUtils() {
    }

    /**
     * Checks if a property was set.
     *
     * @param propertyName property name.
     * @return <strong>true</strong> if property is set, <strong>false</strong> otherwise
     */
    public static boolean hasPropertySet(String propertyName) {

        return System.getProperties().containsKey(propertyName);
    }

    /**
     * Return the string value for a property.
     *
     * @param propertyName property name.
     * @return the property value if is set, or null if is not set
     */
    public static String getPropertyValue(String propertyName) {

        return System.getProperty(propertyName);
    }
}
