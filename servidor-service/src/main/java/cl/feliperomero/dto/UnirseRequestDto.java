package cl.feliperomero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record UnirseRequestDto(
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    String nombre,

    @Min(value = 10, message = "Si decides jugar con tiempo, el mínimo permitido son 10 segundos por turno")
    @Max(value = 300, message = "Si decides jugar con tiempo, el máximo permitido son 300 segundos por turno")
    Integer segundosPorTurno
){}