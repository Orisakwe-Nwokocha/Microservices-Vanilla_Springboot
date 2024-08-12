//package dev.orisha.gateway.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequest;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//
//@Configuration
//public class RestTemplateConfig {
//
//    @Value("${auth.token}")
//    private String authToken;
//
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getInterceptors().add(new AuthorizationHeaderModifierInterceptor(authToken));
//        return restTemplate;
//    }
//
//    private static class AuthorizationHeaderModifierInterceptor implements ClientHttpRequestInterceptor {
//        private final String token;
//
//        public AuthorizationHeaderModifierInterceptor(String token) {
//            this.token = token;
//        }
//
//        @Override
//        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
//            return execution.execute(request, body);
//        }
//    }
//}
