package xyz.flapjack;

/* Custom. */
import xyz.flapjack.fractal.events.EventBus;
import xyz.flapjack.fractal.modules.Setting;
import xyz.flapjack.fractal.modules.Module;
import xyz.flapjack.fractal.Fractal;

/* Open. */
import java.util.ArrayList;

public class Access {

    static {
        String os = System.getProperty("os.name").toLowerCase();

        User.os = os.contains("linux") ? User.OS.Linux
                : os.contains("unix") ? User.OS.Linux
                : os.contains("solaris") ? User.OS.Solaris
                : os.contains("sunos") ? User.OS.Solaris
                : os.contains("win") ? User.OS.Windows
                : os.contains("mac") ? User.OS.MacOS
                : User.OS.Unknown;
    }

    /**
     * User information.
     */
    public static final class User {
        public static OS os;

        public static enum OS {
            Linux,
            Solaris,
            Windows,
            MacOS,
            Unknown
        }
    }

    /**
     * Specifies the module categories.
     */
    public enum Category {
        Combat,
        Player,
        Movement,
        Visual,
        World,
        Settings;
    }

    /**
     * Create an object for each image, giving them the necessary image dimensions.
     */
    public enum Images {
        Neko("images/neko.png", 240, 346),
        CatGirl("images/neko-2.png", 300, 275),
        SchoolGirl("images/school-girl.png", 174, 350),
        Rem("images/rem.png", 200, 350),
        Cat("images/cat-image.png", 300, 300),
        Astolfo("images/astolfo.png", 200, 300),
        Arch("images/arch-uwu.png", 300, 300),
        Monki("images/monki.png", 191, 315);

        /*
         * Allocate attributes to each object:
         *
         * location -> the location of the image within the resources' directory.
         * width -> the width of the image that it will be rendered at.
         * height -> the height of the image that it will be rendered at.
         */
        public final String location;
        public final int width;
        public final int height;

        Images(final String location, final int width, final int height) {
            this.location = location;
            this.width = width;
            this.height = height;
        }
    }

    /**
     * GitHub's information.
     */
    public static final class Github {
        public static String github = "https://github.com/AriaJackie/Fractal";
        public static String version = "https://raw.githubusercontent.com/AriaJackie/Fractal/main/version";
    }

    /**
     * Client control.
     */
    public static final class Client {
        public static String version = "1.2";

        public static boolean debugger = false;
        public static boolean enabled = true;
        public static boolean firstLaunch = false;

        /**
         * Generates a config file.
         * @return the config txt.
         */
        public static String generateConfig() {
            StringBuilder config = new StringBuilder();

            /*
             * Iterates through all the modules, and generates a config line per setting.
             * Configs are laid out like this:
             *
             * Module Title:State/Keybinding/Value Name:Value
             *
             * EG:
             * Aim Assist:state:false
             *
             * -> Aim Assist module : The toggle state of the module : false (off)
             */
            for (Module targetModule: Instance.getModules()) {
                if (!targetModule.title.equals("Config")) {
                    config.append(targetModule.title).append(":").append("state").append(":").append(targetModule.enabled);
                    config.append("\n");

                    config.append(targetModule.title).append(":").append("keybind").append(":").append(targetModule.keyBind);
                    config.append("\n");
                    config.append(targetModule.title).append(":").append("keychar").append(":").append(targetModule.displayBind);
                    config.append("\n");
                    for (Setting setting: targetModule.settings) {
                        switch (setting.getType()) {
                            case "toggle" -> {
                                config.append(buildString(targetModule.title, setting.getName(), String.valueOf(setting.booleanValue)));
                            }
                            case "input" -> {
                                config.append(buildString(targetModule.title, setting.getName(), setting.stringValue));
                            }
                            case "slider-i" -> {
                                config.append(buildString(targetModule.title, setting.getName(), String.valueOf(setting.intValue)));
                            }
                            case "slider-d" -> {
                                config.append(buildString(targetModule.title, setting.getName(), String.valueOf(setting.doubleValue)));
                            }
                            case "dropdown" -> {
                                config.append(buildString(targetModule.title, setting.getName(), setting.optionValue));
                            }
                            case "bind" -> {
                                config.append(buildString(targetModule.title, "keybind", setting.getName(), String.valueOf(setting.keyBind)));
                                config.append("\n");
                                config.append(buildString(targetModule.title, "keychar", setting.getName(), String.valueOf(setting.displayBind)));
                            }
                        }

                        if (!setting.getType().equals("button")) {
                            config.append("\n");
                        }
                    }
                }
            }

            return config.toString();
        }

        /**
         * Builds the configuration string in format.
         * @param title the title of the module.
         * @param name  the named type of setting.
         * @param value the value of the setting.
         * @return      the formatted string.
         */
        private static String buildString(final String title, final String name, final String value) {
            return String.format("%s:%s:%s", title, name, value);
        }

        /**
         * Builds the configuration string in format.
         * @param title         the title of the module.
         * @param bindComponent the bind component setting name.
         * @param name          the named type of setting.
         * @param value         the value of the setting.
         * @return              the formatted string.
         */
        private static String buildString(final String title, final String bindComponent, final String name, final String value) {
            return String.format("%s:%s:%s:%s", title, bindComponent, name, value);
        }
    }

    /**
     * Instance control.
     */
    public static final class Instance {
        public static final EventBus eventBus = new EventBus();

        /**
         * Returns the current Fractal instance.
         * @return the instance.
         */
        public static Fractal getInstance() {
            return Fractal.INSTANCE;
        }

        /**
         * Gets the instance from a static context.
         * @return the instance.
         */
        public static Module getModule(final String title) {
            return getInstance().getModuleManager().getModule(title);
        }

        /**
         * Gets a list of all the modules.
         * @return the list.
         */
        public static ArrayList<Module> getModules() {
            return Fractal.INSTANCE.getModuleManager().modules;
        }

        /**
         * Gets a list of all the modules in a category.
         * @return the list.
         */
        public static ArrayList<Module> getModules(final Category category) {
            return Fractal.INSTANCE.getModuleManager().getModules(category);
        }

        /**
         * Returns the current event bus instance.
         * @return the event bus.
         */
        public static EventBus getEventBus() {
            return eventBus;
        }
    }

    /**
     * General pathing utilities.
     */
    public static final class Utils {
        /**
         * Gets the current OS's minecraft path.
         * @return the path.
         */
        public static String getLocalMinecraftPath() {
            /*
             * Fractal files are stored in .minecraft rather than a Lunar Client specific directory.
             * This is for future proofing when a duel event bus and shading system is implemented.
             */
            if (User.os == User.OS.MacOS)
                return System.getenv("HOME") + "/Library/Application Support/minecraft";

            if (User.os == User.OS.Linux)
                return System.getenv("HOME") + "/.minecraft";

            if (User.os == User.OS.Windows)
                return System.getenv("appdata") + "/.minecraft";

            return null;
        }
    }
}
