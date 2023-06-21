package xyz.flapjack.fractal.modules.impl.settings;

/* Custom. */
import xyz.flapjack.fractal.interfaces.clickgui.components.UIComponent;
import xyz.flapjack.fractal.interfaces.clickgui.components.impl.Frame;
import xyz.flapjack.fractal.modules.impl.settings.impl.configs.*;
import xyz.flapjack.fractal.modules.Setting;
import xyz.flapjack.fractal.modules.Module;

/* Open. */
import java.util.ArrayList;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class Configs extends Module {
    private String lastConfig = "";

    public Configs() {
        super("Config", "The selected config.", Category.Settings, "menu", "none");

        this.registerSetting(new Setting("Config", this, null, this.updateListing()));
        this.registerSetting(new Setting("Config name", this, null, "Config 1"));

        this.registerSetting(new Setting("Save current config", this, null, new Save()));
        this.registerSetting(new Setting("Update current config", this, null, new Update()));
        this.registerSetting(new Setting("Delete current config", this, null, new Delete()));
    }

    @Override
    public void init() {
        new Thread(() -> {
            try {
                while (true) {
                    if (!this.getVal("Config").equals(this.lastConfig)) {
                        try {
                            this.update(new File(Utils.getLocalMinecraftPath() + "/fractal/" + (String) this.getVal("Config") + ".txt"), (String) this.getVal("Config"));

                        } catch (Exception ignored) { }

                        this.lastConfig = (String) this.getVal("Config");
                    }

                    this.settings.get(0).options = this.updateListing();

                    Thread.sleep(100);
                }

            } catch (Exception ignored) { }
        }).start();
    }

    /**
     * Updates the client to a target config.
     * @param config the target config.
     * @param name the name of the config.
     */
    private void update(final File config, final String name) {
        try {
            FileReader fileReader = new FileReader(config);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();

            while (line != null) {
                if (!line.equals("")) {
                    String[] parts = line.split(":");

                    String object = parts[0];
                    String property = parts[1];
                    String value = parts[2];
                    String extra = "";

                    if (parts.length > 3) {
                        extra = parts[3];
                    }

                    try {
                        switch (property) {
                            case "state" -> {
                                getModule(object).enabled = Boolean.parseBoolean(value);
                            }
                            case "keybind" -> {
                                if (!extra.equalsIgnoreCase("")) {
                                    getModule(object).getSetting(value).keyBind = Integer.parseInt(extra);
                                } else {
                                    getModule(object).keyBind = Integer.parseInt(value);
                                }
                            }
                            case "keychar" -> {
                                if (!extra.equalsIgnoreCase("")) {
                                    getModule(object).getSetting(value).displayBind = extra;
                                } else {
                                    getModule(object).displayBind = value;
                                }
                            }
                            default -> {
                                getModule(object).setVal(property, value);
                            }
                        }

                    } catch (Exception ignored) { }
                }

                line = bufferedReader.readLine();
            }

            bufferedReader.close();

            getModule("Config").setVal("Config", name);

            for (Frame frame: Instance.getInstance().getClickGui().frames) {
                for (UIComponent component: frame.modules) {
                    try {
                        component.config();

                    } catch (Exception ignored) { }
                }
            }

        } catch (Exception ignored) { }
    }

    /**
     * Updates the configs by iterating over all available modules in the directory.
     * @return an arraylist of configs.
     */
    private ArrayList<String> updateListing() {
        ArrayList<String> options = new ArrayList<>();

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(Utils.getLocalMinecraftPath() + "/fractal"));

            for (Path path: stream) {
                String name = path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf('.'));
                options.add(name);
            }

        } catch (Exception ignored) { }

        return options;
    }
}
