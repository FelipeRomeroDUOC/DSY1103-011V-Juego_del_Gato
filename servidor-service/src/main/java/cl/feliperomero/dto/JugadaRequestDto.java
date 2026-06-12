package cl.feliperomero.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

public record JugadaRequestDto(

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    String nombre,

    @NotNull(message = "El número de casilla es requerido.")
    @Min(value = 1, message = "Debes elegir una casilla entre el 1 y el 9")
    @Max(value = 9, message = "Debes elegir una casilla entre el 1 y el 9")
    Integer casilla

){}