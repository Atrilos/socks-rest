package com.atrilos.socksrest.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto-class for addition/removal of socks
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSocksDTO {
    @NotBlank(message = "Color must be present!")
    private String color;
    @Min(value = 0L, message = "Cotton part must be between 0 and 100!")
    @Max(value = 100L, message = "Cotton part must be between 0 and 100!")
    private int cottonPart;
    @Min(value = 1L, message = "Quantity must be greater than 0!")
    private long quantity;
}
