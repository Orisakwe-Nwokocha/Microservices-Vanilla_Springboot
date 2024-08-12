package dev.orisha.gateway.controllers.utils;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class ControllerUtils {

    public static HttpEntity<String> getHttpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, token);
        return new HttpEntity<>(headers);
    }

    public static List<ServiceInstance> getServiceInstances(DiscoveryClient discoveryClient, String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }
}
