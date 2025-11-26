package vasyurin.work;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vasyurin.work.configurations.CommonConfiguration;

@WebListener
public class Initializer implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {

        CommonConfiguration.getInstance().runLiquibase();

    }
}
