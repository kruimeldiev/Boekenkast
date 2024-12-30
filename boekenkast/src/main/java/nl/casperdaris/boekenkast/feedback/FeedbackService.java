package nl.casperdaris.boekenkast.feedback;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nl.casperdaris.boekenkast.book.Book;
import nl.casperdaris.boekenkast.book.BookRepository;
import nl.casperdaris.boekenkast.common.PaginationResponse;
import nl.casperdaris.boekenkast.exceptions.ForbiddenException;
import nl.casperdaris.boekenkast.user.User;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer createFeedback(FeedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID: " + request.bookId()));
        if (book.isArchived() || !book.isSharable()) {
            throw new ForbiddenException("The requested book is not available for feedback");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new ForbiddenException("You cannot leave feedback on your own book");
        }
        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    public PaginationResponse<FeedbackResponse> findAllFeedbackByBook(Integer bookId, Integer page, Integer size,
            Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
                .toList();
        return new PaginationResponse<>(feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast());
    }
}
