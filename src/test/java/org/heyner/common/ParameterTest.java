package org.heyner.common;

import org.heyner.common.Parameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParameterTest {
    private final Parameter parameter= new Parameter("config.properties");

    @Test
    void testGetProperty() {
        String tempString = parameter.getProperty("url");
        assertNotNull(tempString);
        assertEquals("testURL",tempString);
        tempString = parameter.getProperty("Parameter");
        assertNotNull(tempString);
        assertEquals("My Parameter",tempString);
        tempString = parameter.getProperty("Parameter2");
        assertNull(tempString);
    }
    @Test
    void testGetVersion() {
        String tempString= parameter.getVersion();
        assertEquals("version",tempString);
    }
    @Test()
    void testGetNomFichierBase2() {
        assertDoesNotThrow(()->new Parameter("config2properties"));
    }
}
