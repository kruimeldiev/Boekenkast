package nl.casperdaris.boekenkast.history;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/// Deze interface bevat alle custom methoden die de database aanroepen om boeken op te halen, aan te maken of te wijzigen.
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);

    @Query("""
            SELECT
            (COUNT (*) > 0) AS isBorrowed
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
            AND history.book.id = :bookId
            AND history.returnApprovedByOwner = false
            """)
    boolean isAlreadyBorrowed(Integer bookId, Integer id);

    @Query("""
            SELECT transactionHistory
            FROM BookTransactionHistory transactionHistory
            WHERE transactionHistory.book.id = :bookId
            AND transactionHistory.user.id = :userId
            AND transactionHistory.returned = false
            AND transactionHistory.returnApprovedByOwner = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);

    @Query("""
            SELECT transactionHistory
            FROM BookTransactionHistory transactionHistory
            WHERE transactionHistory.book.owner.id = :userId
            AND transactionHistory.book.id = :bookId
            AND transactionHistory.returned = true
            AND transactionHistory.returnApprovedByOwner = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer id);
}