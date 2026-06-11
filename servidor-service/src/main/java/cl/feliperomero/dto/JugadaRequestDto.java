package cl.feliperomero.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record JugadaRequestDto(

    @NotNull(message = "El número de casilla es requerido.")
    @Min(value = 1, message = "Debes elegir una casilla entre el 1 y el 9")
    @Max(value = 9, message = "Debes elegir una casilla entre el 1 y el 9")
    Integer casilla,

    @NotBlank(message = "La ficha es requerida")
    @Pattern(regexp = "^[XOxo]$", message = "La ficha debe ser estrictamente 'X' o 'O' (minúsculas permitidas).")
    String ficha

){}