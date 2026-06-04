package apb.co.la.moh.api.mobile.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${swagger.server.dev.url}")
    private String devUrl;

    @Value("${swagger.server.uat.url}")
    private String uatUrl;

    @Value("${swagger.server.prd.url}")
    private String prdUrl;

    @Value("${swagger.server.dev.enabled}")
    private boolean devEnabled;

    @Value("${swagger.server.uat.enabled}")
    private boolean uatEnabled;

    @Value("${swagger.server.prd.enabled}")
    private boolean prdEnabled;

    @Bean
    public OpenAPI openAPI() {

        List<Server> servers = new ArrayList<>();

        if (devEnabled) {
            servers.add(buildServer(devUrl, "🛠 Development Server"));
        }

        if (uatEnabled) {
            servers.add(buildServer(uatUrl, "🧪 UAT Server"));
        }

        if (prdEnabled) {
            servers.add(buildServer(prdUrl, "🚀 Production Server"));
        }

        if (servers.isEmpty()) {
            servers.add(buildServer(devUrl, "🛠 Development Server"));
        }

        return new OpenAPI()
                .info(apiInfo())
                .servers(servers);
    }

    private Server buildServer(String url, String description) {
        return new Server()
                .url(url + normalizeContextPath())
                .description(description);
    }

    private String normalizeContextPath() {
        if (contextPath == null || contextPath.isBlank() || "/".equals(contextPath)) {
            return "";
        }
        return contextPath;
    }

    private Info apiInfo() {
        return new Info()
                .title("MOH Mobile Service API")
                .version("1.0.0")
                .description("""
                        API documentation for Hospital Location Service.
                        
                        **🔐 Security Requirements**
                        This API requires HMAC SHA-256 signature verification.
                        Formula: `HMAC-SHA256(URI + QueryString + Timestamp + Body, SecretKey)`
                        
                        **Example Headers:**
                        - `X-Timestamp`: 1715856000
                        
                        - `X-Signature`: 87463eb7e2ee09b19c2a2315a4b50963611969619e9be89dc0eade7c58635595


                        **Environments**
                        - 🛠 DEV  : http://localhost:8002
                        - 🧪 UAT  : https://owa.apb.com.la:442
                        - 🚀 PRD  : https://apb.services.pro
                        """)
                .contact(new Contact()
                        .name("API Team")
                        .email("info@apb.com.la"))
                .license(new License()
                        .name("Proprietary"));
    }
}


//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class OpenApiConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("MOH Mobile Service API")
//                        .version("1.0")
//                        .description("API Documentation for MOH Mobile Service"));
//    }
//}
