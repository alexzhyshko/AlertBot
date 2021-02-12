package bot;

import application.startup.Application;
import bot.schema_init.SchemaInitializer;

public class Main {

    public static void main(String[] args) {
        Application.start();
        SchemaInitializer.initialize();
    }

}
