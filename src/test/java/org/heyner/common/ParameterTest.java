package org.heyner.common;

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

    @Test
    void testFallbackLogic() {
        // Test que la logique de fallback fonctionne :
        // - D'abord essayer comme fichier externe
        // - Puis fallback vers le classpath

        // Ce test utilise le fichier config.properties qui existe dans src/test/resources
        // et devrait être chargé depuis le classpath
        Parameter param = new Parameter("config.properties");
        assertNotNull(param.getProperty("url"));
        assertEquals("testURL", param.getProperty("url"));
    }
}
