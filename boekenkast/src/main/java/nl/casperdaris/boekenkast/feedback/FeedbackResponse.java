package nl.casperdaris.boekenkast.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    private Double rating;
    private String comment;
    private boolean isFeedbackFromConnectedUser;
}
