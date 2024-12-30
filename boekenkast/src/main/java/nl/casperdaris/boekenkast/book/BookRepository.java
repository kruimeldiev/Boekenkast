package nl.casperdaris.boekenkast.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/// Deze interface bevat alle methoden die de database aanroepen om boeken op te halen, aan te maken of te wijzigen.
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query("""
            SELECT b
            FROM Book b
            WHERE b.isArchived = false
            AND b.isSharable = true
            AND b.owner.id != :userId
            """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);
}
