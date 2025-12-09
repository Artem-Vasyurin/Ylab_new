//package vasyurin.work;
//
//import jakarta.servlet.FilterRegistration;
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletRegistration;
//import jakarta.servlet.annotation.WebListener;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.servlet.DispatcherServlet;
//import vasyurin.work.configurations.CommonConfiguration;
//import vasyurin.work.configurations.LiquibaseConfig;
//
//@WebListener
//@Slf4j
//public class Initializer implements WebApplicationInitializer {
//
//
//    @Override
//    public void onStartup(ServletContext servletContext) {
//
//        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.register(CommonConfiguration.class, LiquibaseConfig.class);
//        log.info("configuring web application context");
//
//        context.setServletContext(servletContext);
//        log.info("setServletContext");
//        context.refresh();
//
//        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
//        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);
//        servlet.setLoadOnStartup(1);
//        servlet.addMapping("/");
//
//        log.info("configuring DispatcherServlet");
//
//        FilterRegistration.Dynamic filter = servletContext.addFilter(
//                "authFilter",
//                new org.springframework.web.filter.DelegatingFilterProxy("authFilter", context)
//        );
//        filter.addMappingForUrlPatterns(null, false, "/product/*");
//        log.info("configuring AuthFilter");
//    }
//
//}
