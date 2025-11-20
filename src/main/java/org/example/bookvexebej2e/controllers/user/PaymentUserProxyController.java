package org.example.bookvexebej2e.controllers.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;

@RestController
@RequestMapping("/user")
public class PaymentUserProxyController {

    private final RestTemplate restTemplate;

    @Value("${services.payment-service.url}")
    private String paymentServiceBaseUrl;

    public PaymentUserProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = {"/payments/**"},
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<byte[]> proxyToPaymentService(HttpServletRequest request,
                                                        @RequestBody(required = false) byte[] body) {
        try {
            String requestUri = request.getRequestURI();  
            String contextPath = request.getContextPath();
            if (requestUri.startsWith(contextPath)) {
                requestUri = requestUri.substring(contextPath.length());
            }
            String query = request.getQueryString();
            String targetUrl = paymentServiceBaseUrl + requestUri + (query != null ? ("?" + query) : "");

            HttpMethod method = HttpMethod.valueOf(request.getMethod());
            HttpHeaders headers = extractHeaders(request);

            HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(new URI(targetUrl), method, entity, byte[].class);

            HttpHeaders proxyHeaders = new HttpHeaders();
            MediaType contentType = response.getHeaders().getContentType();
            if (contentType != null) {
                proxyHeaders.setContentType(contentType);
            }

            return new ResponseEntity<>(response.getBody(), proxyHeaders, response.getStatusCode());
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(("Invalid target URL").getBytes());
        } catch (HttpStatusCodeException ex) {
            HttpHeaders proxyHeaders = new HttpHeaders();
            MediaType contentType = ex.getResponseHeaders() != null ? ex.getResponseHeaders().getContentType() : null;
            if (contentType != null) {
                proxyHeaders.setContentType(contentType);
            }
            return new ResponseEntity<>(ex.getResponseBodyAsByteArray(), proxyHeaders, ex.getStatusCode());
        } catch (RestClientException ex) {
            return ResponseEntity.status(502).body(("Upstream payment-service error: " + ex.getMessage()).getBytes());
        }
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if ("host".equalsIgnoreCase(headerName) ||
                        "content-length".equalsIgnoreCase(headerName) ||
                        "transfer-encoding".equalsIgnoreCase(headerName)) {
                    continue;
                }
                Enumeration<String> values = request.getHeaders(headerName);
                if (values != null) {
                    headers.put(headerName, Collections.list(values));
                }
            }
        }
        return headers;
    }
}
