import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BookSearch {

    public static List<Books> searchBooks(String search, ApiKey apiKey) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String encodedSearch = URLEncoder.encode(search, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/books/v1/volumes?q=" + encodedSearch + "&langRestrict=en&key=" + apiKey.getApiKey()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray items = jsonObject.has("items") ? jsonObject.getAsJsonArray("items") : null;

        List<Books> books = new ArrayList<>();

        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                JsonObject firstItem = items.get(i).getAsJsonObject();
                JsonObject volumeInfo = firstItem.getAsJsonObject("volumeInfo");

                Books book = new Books();

                if (volumeInfo.has("title")) {
                    book.setTitle(volumeInfo.get("title").getAsString());
                }

                if (volumeInfo.has("authors")) {
                    JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
                    String author = authorsArray != null && authorsArray.size() > 0 ? authorsArray.get(0).getAsString() : "Unknown";
                    book.setAuthor(author);
                }

                if (volumeInfo.has("publisher")) {
                    book.setPublisher(volumeInfo.get("publisher").getAsString());
                }

                if (volumeInfo.has("pageCount")) {
                    book.setPages(volumeInfo.get("pageCount").getAsInt());
                }

                if (volumeInfo.has("publishedDate")) {
                    try {
                        book.setYear(Integer.parseInt(volumeInfo.get("publishedDate").getAsString().substring(0, 4)));
                    } catch (NumberFormatException e) {
                        book.setYear(0);
                    }
                }

                if (volumeInfo.has("categories")) {
                    JsonArray categories = volumeInfo.getAsJsonArray("categories");
                    String genre = categories != null && categories.size() > 0 ? categories.get(0).getAsString() : "Unknown";
                    book.setGenre(genre);
                }

                if (volumeInfo.has("description")) {
                    book.setSynopsis(volumeInfo.get("description").getAsString());
                }

                if (volumeInfo.has("language") && "en".equals(volumeInfo.get("language").getAsString())) {
                    book.setLanguage("en");
                    books.add(book);
                }
            }
        }

        return books;
    }
}
