package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        System.out.println("Please select Exercise number:");
        int exerciseNumber = Integer.parseInt(bufferedReader.readLine());
        //  printAllBooksAfterYear(2000);
        //  printAllAuthorsAndNumberOfTheirBooks();
        //  printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
        //  printALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");
        switch (exerciseNumber) {
            case 1 -> Exercise_1_Books_TitlesByAgeRestriction();
            case 2 -> Exercise_2_GoldenBooks();
            case 3 -> Exercise_3_BooksByPrice();
            case 4 -> Exercise_4_NotReleasedBooks();
            case 5 -> Exercise_5_BooksReleasedBeforeDate();
            case 6 -> Exercise_6_AuthorsSearch();
            case 7 -> Exercise_7_BooksSearch();
            case 8 -> Exercise_8_BookTitlesSearch();
            case 9 -> Exercise_9_CountBooks();
            case 10 -> Exercise_10_TotalBookCopies();
            case 11 -> Exercise_11_ReducedBook();
        }
    }

    private void Exercise_11_ReducedBook() throws IOException {
        System.out.println("Please enter a book title:");
        String bookTitle = bufferedReader.readLine();
        bookService
                .findTitleTypeAgeAndPriceOfGivenBookTitle(bookTitle)
                .forEach(System.out::println);
    }

    private void Exercise_10_TotalBookCopies() {
        Map<String, Integer> resultMap = new HashMap<>();

        for (String allAuthorsAndTheirTotalCopy : authorService.findAllAuthorsAndTheirTotalCopies()) {
            String[] split = allAuthorsAndTheirTotalCopy.split(" - ");
            String fullName = split[0];
            Integer copiesCount = Integer.valueOf(split[1]);
            resultMap.put(fullName, copiesCount);
        }

        List<Map.Entry<String, Integer>> sortedMap = new ArrayList<>(resultMap.entrySet());
        sortedMap
                .sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        sortedMap
                .forEach(e ->
                        System.out.printf("%s - %d%n",
                                e.getKey(),
                                e.getValue()));
    }

    private void Exercise_9_CountBooks() throws IOException {
        System.out.println("Please insert a title length:");
        int number = Integer.parseInt(bufferedReader.readLine());
        System.out.println(bookService.findAllCountOfAllBooksWhichTitleLengthIsMoreThanInputNumber(number));
    }

    private void Exercise_8_BookTitlesSearch() throws IOException {
        System.out.println("Please enter first characters of author's last name");
        String charsInput = bufferedReader.readLine();
        bookService.findAllBookTitlesWrittenByAuthorsWithLastNameChars(charsInput)
                .forEach(System.out::println);
    }

    private void Exercise_7_BooksSearch() throws IOException {
        System.out.println("Please enter chars that might contain in book titles:");
        String chars = bufferedReader.readLine();
        bookService.findAllBooksTitlesWhichContainsAGivenChars(chars)
                .forEach(System.out::println);
    }

    private void Exercise_6_AuthorsSearch() throws IOException {
        System.out.println("Please enter last characters of author's first name");
        String charsInput = bufferedReader.readLine();
        authorService
                .findNameOfAuthorsWhichFirstNameEndsWithInputChars(charsInput)
                .forEach(System.out::println);
    }

    private void Exercise_5_BooksReleasedBeforeDate() throws IOException {
        System.out.println("Please enter date in format dd-MM-yyyy:");
        LocalDate localDate = LocalDate.parse(bufferedReader.readLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        bookService
                .findBookInfoWhereBookIsReleasedBeforeGivenDate(localDate)
                .forEach(System.out::println);
    }

    private void Exercise_4_NotReleasedBooks() throws IOException {
        System.out.println("Please enter year:");
        int year = Integer.parseInt(bufferedReader.readLine());
        bookService
                .findAllBookTitlesWhereBooksAreNotReleasedInTheGivenYear(year)
                .forEach(System.out::println);
    }

    private void Exercise_3_BooksByPrice() {
        bookService
                .findAllTitlesAndPricesOfBooksWherePriceIsLowerThan5AndHigherThan40()
                .forEach(System.out::println);
    }

    private void Exercise_2_GoldenBooks() {
        bookService
                .findAllGoldBookTitlesWithCopiesLessThen5000()
                .forEach(System.out::println);
    }

    private void Exercise_1_Books_TitlesByAgeRestriction() throws IOException {
        System.out.println("Please enter Age Restriction:");
        AgeRestriction ageRestriction = AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase());
        bookService
                .findAllBookTitlesWithAgeRestriction(ageRestriction)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }

    private void printALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }


}
