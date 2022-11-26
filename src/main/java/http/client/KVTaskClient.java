package http.client;

import http.server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class KVTaskClient {
    private final URI uri;
    private final HttpClient httpClient;
    private String apiToken;

    public KVTaskClient(String url) {
        startKVServer();
        this.uri = URI.create(url);
        this.httpClient = HttpClient.newHttpClient();
        register();
    }

    private void startKVServer() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            System.out.println("Ошибка запуска сервера");
        }
    }

    private void register() {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri + "register"))
                .build();
        try {
            var response = httpClient
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());
            this.apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.printf("Во время выполнения регистрации ресурса по url-адресу: '%s' возникла ошибка.\n", uri);
        }
    }

    public void put(String key, String value) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(value))
                .uri(URI.create(uri + "save/" + key + "?API_TOKEN=" + apiToken))
                .build();
        try {
            httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.printf("Во время выполнения добавления ресурса по url-адресу: '%s' возникла ошибка.\n", uri);
        }
    }

    public String load(String key) {
        Optional<String> json = Optional.empty();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri + "load/" + key + "?API_TOKEN=" + apiToken))
                .build();
        try {
            var response = httpClient
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());
            json = Optional.ofNullable(response.body());
        } catch (IOException | InterruptedException e) {
            System.out.printf("Во время выполнения запроса ресурса по url-адресу: '%s' возникла ошибка.\n", uri);
        }

        return json.orElseThrow(NullPointerException::new);
    }
}
