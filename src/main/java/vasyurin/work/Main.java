package vasyurin.work;

import vasyurin.work.configurations.CommonConfiguration;
import vasyurin.work.configurations.ConfigurationReader;
import vasyurin.work.consoleutility.ConsoleUtility;

public class Main {
    public static void main(String[] args) {

        System.out.println(ConfigurationReader.readConfiguration());
        CommonConfiguration.getInstance().runLiquibase();
        ConsoleUtility.getInstance().start();

    }
}