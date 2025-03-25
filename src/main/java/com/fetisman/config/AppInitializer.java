package com.fetisman.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@Slf4j
public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (servletContext.getServletRegistration("dispatcher") != null) {
            throw new IllegalStateException("!!! DispatcherServlet already exists");
        }
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        context.setServletContext(servletContext);
        context.refresh();

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
                new DispatcherServlet(context));
        if (dispatcher != null) {
            dispatcher.setLoadOnStartup(1);
            dispatcher.addMapping("/");
        } else {
            throw new IllegalStateException("!!! Failed to register DispatcherServlet");
        }

    }
}

