package org.example.bookvexebej2e.dto.car;

import lombok.Data;

@Data
public class CarTypeUpdate {
    private String code;
    private String name;
    private String description;
    private Integer seatCount;
}
