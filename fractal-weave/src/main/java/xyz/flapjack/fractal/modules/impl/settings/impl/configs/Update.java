package xyz.flapjack.fractal.modules.impl.settings.impl.configs;

/* Custom. */
import xyz.flapjack.fractal.modules.impl.settings.impl.Method;
import xyz.flapjack.fractal.modules.Module;

/* Open. */
import java.io.FileWriter;
import java.io.File;

public class Update extends Method {
    @Override
    public void execute(Module module) {
        try {
            String dir = Utils.getLocalMinecraftPath();

            File configFile = new File(dir, "/fractal/" + (String) module.getVal("Config") + ".txt");

            if (configFile.exists()) {
                FileWriter writer = new FileWriter(configFile);

                writer.write(Client.generateConfig());
                writer.close();
            }

        } catch (Exception ignored) { }
    }
}
