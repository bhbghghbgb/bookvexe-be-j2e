package org.example.bookvexebej2e.dto.customer;

import lombok.Data;
import java.util.UUID;

@Data
public class CustomerTypeSelectResponse {
    private UUID id;
    private String code;
    private String name;
}
