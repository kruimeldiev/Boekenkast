package nl.casperdaris.boekenkast.feedback;

import java.util.Objects;

import org.springframework.stereotype.Service;

import nl.casperdaris.boekenkast.book.Book;

// Maps a FeedbackRequest to a Feedback entity
@Service
public class FeedbackMapper {

    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .rating(request.rating())
                .comment(request.comment())
                .book(Book.builder().id(request.bookId()).build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer userId) {
        return FeedbackResponse
                .builder()
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .isFeedbackFromConnectedUser(Objects.equals(feedback.getCreatedByUser(), userId))
                .build();
    }
}
