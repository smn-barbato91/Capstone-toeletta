package matteofurgani.Capstone.project.reservations;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record TestReservation(@NotNull(message = "Service is required")
String serviceId,
@NotNull(message = "Pet is required")
Integer petInfoId) {

	
}
