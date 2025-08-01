package com.scaler.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.scaler.provider.PatientProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Configuration
public class FhirServerConfig {

    private final PatientProvider patientProvider;

    public FhirServerConfig(PatientProvider patientProvider) {
        this.patientProvider = patientProvider;
    }

    @Bean
    @Primary
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }

    @Bean
    public ServletRegistrationBean<Servlet> fhirServlet(FhirContext fhirContext, PatientProvider patientProvider) {
        RestfulServer restfulServer = new RestfulServer(fhirContext);
        restfulServer.setResourceProviders(List.of(patientProvider));
        restfulServer.setDefaultPrettyPrint(true);

        ServletRegistrationBean<Servlet> servletRegistrationBean =
                new ServletRegistrationBean<>(restfulServer, "/fhir/*"); // base path
        servletRegistrationBean.setName("FhirServlet");
        return servletRegistrationBean;
    }
}


