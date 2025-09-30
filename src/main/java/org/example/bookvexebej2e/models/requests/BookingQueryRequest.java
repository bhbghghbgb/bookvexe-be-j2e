package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingQueryRequest extends BasePageableQueryRequest {
    private Integer userId;
    private Integer tripId;
    private String status;
    private List<String> statuses;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}