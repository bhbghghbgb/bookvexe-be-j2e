package org.example.bookvexebej2e.models.responses;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateUpdateDto {
    @NotBlank
    private String code;

    @NotBlank
    private String name;
}