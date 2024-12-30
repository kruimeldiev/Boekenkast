package nl.casperdaris.boekenkast.book;

import org.springframework.data.jpa.domain.Specification;

/// Deze klasse bevat alle specifications die de boeken aan de database aanroepen.
/// Specifications zijn een manier om de database te filteren op basis van de gegevens in de entiteiten.
public class BookSpecification {

    public static Specification<Book> withOwnerId(Integer id) {
        return (root, query, cb) -> cb.equal(root.get("owner").get("id"), id);
    }
}
