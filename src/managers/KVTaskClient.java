package managers;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.net.URL;
import static java.net.URI.create;

public class KVTaskClient {
    @Getter
    private final long API_TOKEN;
    private URI URI;
    private URL URL;
    HttpRequest.Builder requestBuilder;
    HttpClient client;
    HttpRequest request;
    HttpResponse.BodyHandler<String> handler;

    public KVTaskClient(URL url) throws IOException, InterruptedException {
        this.URL = url;
        this.URI = create(url+"/register");
        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        requestBuilder = HttpRequest.newBuilder();

        handler = HttpResponse.BodyHandlers.ofString();

        request = requestBuilder
                .GET()
                .uri(URI)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpResponse<String> response = client.send(request, handler);
        String str = response.body();
        this.API_TOKEN = Long.parseLong(str);
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI=create(URL+"/save/"+key+"?API_TOKEN="+API_TOKEN);
        request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
       client.send(request, handler);
    }



    String load(String key) throws IOException, InterruptedException {
        URI=create(URL+"/load/"+key+"?API_TOKEN="+API_TOKEN);
        request = requestBuilder
                .GET()
                .uri(URI)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse<String> response = client.send(request, handler);

        return response.body();
    }



}
