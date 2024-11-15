package nl.casperdaris.boekenkast.handler;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/// Dit is een model die wordt gebruikt om de exception responses te verwerken.
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    private Integer errorCode;
    private String errorDescription;
    private String error;
    private Set<String> validationErrors;
    private Map<String, String> errors;
}
