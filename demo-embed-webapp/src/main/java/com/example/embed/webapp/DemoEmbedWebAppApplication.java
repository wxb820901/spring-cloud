package com.example.embed.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DemoEmbedWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoEmbedWebAppApplication.class, args);
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setPort(49080);
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html"));
        return factory;
    }

    @Bean
    public ServletRegistrationBean myServlet() {
        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(new myservice.mynamespace.web.DemoServlet(), "/DemoService.svc/*");
        return servletRegistrationBean;
    }
}
