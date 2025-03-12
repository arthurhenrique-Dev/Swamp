import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Search {
    public static void main(String[] args) throws IOException, InterruptedException {

        ArrayList<String> listMyBooks = new ArrayList<>();
        ApiKey apiKey = new ApiKey();
        Scanner choice = new Scanner(System.in);
        Scanner sc = new Scanner(System.in);
        Scanner sc2 = new Scanner(System.in);
        Scanner sc3 = new Scanner(System.in);
        int command;

        while (true) {

            String menu = """
                    What do you want to do?

                    1- Search books
                    2- See your favorites
                    3- Exit
                    """;
            System.out.println(menu);
            command = choice.nextInt();

            switch (command) {
                case 1:
                    System.out.println("Enter the book you want to search for:");
                    String search = sc.nextLine();

                    List<Books> books = BookSearch.searchBooks(search, apiKey);

                    if (!books.isEmpty()) {

                        Books book = books.get(0);
                        System.out.println("Title: " + book.getTitle());
                        System.out.println("Author: " + book.getAuthor());
                        System.out.println("Publisher: " + book.getPublisher());
                        System.out.println("Pages: " + book.getPages());
                        System.out.println("Year: " + book.getYear());
                        System.out.println("Genre: " + book.getGenre());
                        System.out.println("Synopsis: " + book.getSynopsis());
                        System.out.println();
                        System.out.println("Want to add this book to your favorites? (y/n)");
                        String choice2 = sc2.nextLine();

                        if (choice2.equals("y")) {
                            listMyBooks.add(book.getTitle());
                            System.out.println(listMyBooks);
                        }
                    } else {
                        System.out.println("No books found.");
                    }
                    break;

                case 2:
                    System.out.println("Your favorites:");
                    System.out.println(listMyBooks);
                    System.out.println("You wanna search one of them?");
                    int numberList = Integer.parseInt(sc3.nextLine());
                    int numberSearch = numberList - 1;
                    System.out.println("Searching: " + listMyBooks.get(numberSearch));

                    List<Books> booksFav = BookSearch.searchBooks(listMyBooks.get(numberSearch), apiKey);

                    if (!booksFav.isEmpty()) {

                        Books bookFav = booksFav.get(0);
                        System.out.println("Title: " + bookFav.getTitle());
                        System.out.println("Author: " + bookFav.getAuthor());
                        System.out.println("Publisher: " + bookFav.getPublisher());
                        System.out.println("Pages: " + bookFav.getPages());
                        System.out.println("Year: " + bookFav.getYear());
                        System.out.println("Genre: " + bookFav.getGenre());
                        System.out.println("Synopsis: " + bookFav.getSynopsis());
                        System.out.println();
                    }

                    break;

                case 3:
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }
}
