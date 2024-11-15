package nl.casperdaris.boekenkast.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/// Helper klasse om gestructureerd en paginagewijs responses te verwerken.
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {

    private List<T> content;
    private Integer pageNumber;
    private Integer size;
    private long totalElement;
    private Integer totalPages;
    private boolean first;
    private boolean last;
}
