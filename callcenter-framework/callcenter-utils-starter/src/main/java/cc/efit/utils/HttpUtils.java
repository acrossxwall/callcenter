package cc.efit.utils;

import cc.efit.exception.HttpException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * HTTP 请求工具类
 * 支持 GET/POST 请求，支持 application/json 和 form-data 格式
 */
public class HttpUtils {
    
    private static final HttpClient DEFAULT_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    
    private HttpUtils() {
        // 工具类，防止实例化
    }
    
    /**
     * GET 请求
     */
    public static String get(String url) throws Exception {
        return get(url, null);
    }
    
    /**
     * GET 请求（带请求头）
     */
    public static String get(String url, Map<String, String> headers) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(10));
        
        addHeaders(requestBuilder, headers);
        
        HttpRequest request = requestBuilder.build();
        
        HttpResponse<String> response = DEFAULT_CLIENT.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
        );
        
        return handleResponse(response);
    }
    
    /**
     * POST 请求 - JSON 格式
     */
    public static String postJson(String url, String jsonBody) throws Exception {
        return postJson(url, jsonBody, null);
    }
    
    /**
     * POST 请求 - JSON 格式（带请求头）
     */
    public static String postJson(String url, String jsonBody, Map<String, String> headers) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json");
        
        addHeaders(requestBuilder, headers);
        
        HttpRequest request = requestBuilder.build();
        
        HttpResponse<String> response = DEFAULT_CLIENT.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
        );
        
        return handleResponse(response);
    }
    
    /**
     * POST 请求 - Form Data 格式
     */
    public static String postForm(String url, Map<String, Object> formData) throws Exception {
        return postForm(url, formData, null);
    }
    
    /**
     * POST 请求 - Form Data 格式（带请求头）
     */
    public static String postForm(String url, Map<String, Object> formData, Map<String, String> headers) throws Exception {
        String formBody = buildFormData(formData);
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(formBody))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/x-www-form-urlencoded");
        
        addHeaders(requestBuilder, headers);
        
        HttpRequest request = requestBuilder.build();
        
        HttpResponse<String> response = DEFAULT_CLIENT.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
        );
        
        return handleResponse(response);
    }
    
    /**
     * 异步 GET 请求
     */
    public static CompletableFuture<String> getAsync(String url) {
        return getAsync(url, null);
    }
    
    public static CompletableFuture<String> getAsync(String url, Map<String, String> headers) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(10));
        
        addHeaders(requestBuilder, headers);
        
        HttpRequest request = requestBuilder.build();
        
        return DEFAULT_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpUtils::handleResponse);
    }
    
    /**
     * 异步 POST 请求 - JSON 格式
     */
    public static CompletableFuture<String> postJsonAsync(String url, String jsonBody) {
        return postJsonAsync(url, jsonBody, null);
    }
    
    public static CompletableFuture<String> postJsonAsync(String url, String jsonBody, Map<String, String> headers) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json");
        
        addHeaders(requestBuilder, headers);
        
        HttpRequest request = requestBuilder.build();
        
        return DEFAULT_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpUtils::handleResponse);
    }
    
    /**
     * 构建 Form Data 字符串
     */
    private static String buildFormData(Map<String, Object> formData) {
        if (formData == null || formData.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            if (!sb.isEmpty()) {
                sb.append("&");
            }
            sb.append(encode(entry.getKey()))
              .append("=")
              .append(encode(entry.getValue()));
        }
        return sb.toString();
    }
    
    /**
     * URL 编码（简化版）
     */
    private static String encode(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString().replace(" ", "%20")
                   .replace("&", "%26")
                   .replace("=", "%3D");
    }
    
    /**
     * 添加请求头
     */
    private static void addHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * 处理响应
     */
    private static String handleResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {
            return response.body();
        } else {
            throw new HttpException("HTTP 请求失败，状态码: " + statusCode, statusCode, response.body());
        }
    }
    

}