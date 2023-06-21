package xyz.flapjack.fractal.modules.impl.settings.impl.configs;

/* Custom. */
import xyz.flapjack.fractal.modules.impl.settings.impl.Method;
import xyz.flapjack.fractal.modules.Module;

/* Open. */
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.nio.file.Path;
import java.io.File;

public class Delete extends Method {
    @Override
    public void execute(Module module) {
        try {
            String dir = Utils.getLocalMinecraftPath();

            File configFile = new File(dir, "/fractal/" + (String) module.getVal("Config") + ".txt");

            if (configFile.exists()) {
                configFile.delete();
            }

            module.setVal("Config", this.updateListing().get(0));

        } catch (Exception ignored) { }
    }

    /**
     * Gets an updated list of all the configs.
     * @return the list of configs.
     */
    private ArrayList<String> updateListing() {
        ArrayList<String> options = new ArrayList<>();

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(Utils.getLocalMinecraftPath() + "\\fractal"));

            for (Path path: stream) {
                String name = path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf('.'));
                options.add(name);
            }

        } catch (Exception ignored) { }

        return options;
    }
}
