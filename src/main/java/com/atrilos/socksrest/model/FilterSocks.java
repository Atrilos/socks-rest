package com.atrilos.socksrest.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto-class for filtering
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterSocks {
    private String color;
    private String operation;
    @Min(value = 0L, message = "Cotton part must be between 0 and 100!")
    @Max(value = 100L, message = "Cotton part must be between 0 and 100!")
    private Integer cottonPart;
}
