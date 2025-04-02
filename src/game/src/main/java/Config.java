import com.diogonunes.jcdp.color.api.Ansi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Config {

    private final String configPath;
    private Properties properties = new Properties();

    public Config(String profile) throws IOException {
        this.configPath = "application-" + profile + ".properties";
        loadConfigs();
    }

    public void loadConfigs() throws IOException {
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(configPath)) {
            properties.load(fis);
        }
    }

    public char getSymbol(String name) {
        return Objects.equals(properties.getProperty(name + ".char"), "") ? ' ' : properties.getProperty(name + ".char").charAt(0);
    }

    public Ansi.BColor getColor(String name) {
        return Ansi.BColor.valueOf(properties.getProperty(name + ".color"));
    }

    public Properties getProperties() {
        return properties;
    }
}
