package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllGoldBookTitlesWithCopiesLessThen5000();

    List<String> findAllTitlesAndPricesOfBooksWherePriceIsLowerThan5AndHigherThan40();

    List<String> findAllBookTitlesWhereBooksAreNotReleasedInTheGivenYear(int year);

    List<String> findBookInfoWhereBookIsReleasedBeforeGivenDate(LocalDate localDate);

    List<String> findAllBooksTitlesWhichContainsAGivenChars(String chars);


    List<String> findAllBookTitlesWrittenByAuthorsWithLastNameChars(String charsInput);

    int findAllCountOfAllBooksWhichTitleLengthIsMoreThanInputNumber(int number);


    List<String> findTitleTypeAgeAndPriceOfGivenBookTitle(String bookTitle);

}
