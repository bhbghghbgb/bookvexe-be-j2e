package org.example.bookvexebej2e.dto.car;

import lombok.Data;

@Data
public class CarTypeCreate {
    private String code;
    private String name;
    private String description;
    private Integer seatCount;
}
