package com.cozyspace.librarymanagement.datasource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GoogleBooksAPI {
    private static final String API_KEY = "AIzaSyAXrUwsUdGAHA4RtnItBmRmJ7sWrZHMPmY";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    public static ObservableList<Document> query(String query) throws Exception {
        String filter = "ebooks";

        String url = String.format("%s?q=%s&filter=%s&key=%s", BASE_URL, query, filter, API_KEY);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url.replaceAll(" ", "%20")))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.close();
        if (response.statusCode() == 200) {
            return parseBooksJson(response.body());
        } else {
            return null;
        }
    }

    private static ObservableList<Document> parseBooksJson(String jsonResponse) throws Exception {
        ObservableList<Document> documents = FXCollections.observableArrayList();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode items = rootNode.path("items");

        for (JsonNode item : items) {
            String title = item.path("volumeInfo").path("title").asText();
            String authors = item.path("volumeInfo").path("authors").toString();
            String description = item.path("volumeInfo").path("description").asText();
            String categories = item.path("volumeInfo").path("categories").toString();
            Document document = new Document(null, title, phrase(authors), description, "Sách", 0,
                    phrase(categories), null);

            documents.add(document);
        }
        return documents;
    }

    private static String phrase(String input) {
        if (input.isEmpty()) return null;
        input = input.substring(1, input.length() - 1);
        String[] authorList = input.split(",");
        StringBuilder result = new StringBuilder();
        for (String author : authorList) {
            result.append(author, 1, author.length() - 1).append(", ");
        }
        return result.substring(0, result.length() - 2);
    }
}
