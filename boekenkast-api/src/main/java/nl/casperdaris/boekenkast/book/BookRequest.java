package nl.casperdaris.boekenkast.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/// DTO voor het request object van een boek.
public record BookRequest(Integer id,
        @NotNull(message = "100") @NotEmpty(message = "100") String title,
        @NotNull(message = "101") @NotEmpty(message = "101") String author,
        @NotNull(message = "102") @NotEmpty(message = "102") String isbn,
        @NotNull(message = "103") @NotEmpty(message = "103") String synopsis,
        boolean isSharable) {

}
