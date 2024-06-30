package parameter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Parameter {
    private final Properties prop;
    private static final Logger logger = LogManager.getLogger(Parameter.class);

    public Parameter(String nomFichier) {
        prop = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(nomFichier)) {
            prop.load(input);
        } catch (IOException | NullPointerException e) {
            logger.error(e.getMessage());
        }
    }

    public String getProperty(String nomProperty) {return prop.getProperty(nomProperty);}
}
