package org.example.bookvexebej2e.models.dto.car;

import lombok.Data;

import java.util.UUID;

@Data
public class CarTypeSelectResponse {
    private UUID id;
    private String code;
    private String name;
}
