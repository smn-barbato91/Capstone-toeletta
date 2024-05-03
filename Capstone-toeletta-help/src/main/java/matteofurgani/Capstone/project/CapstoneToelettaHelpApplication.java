package matteofurgani.Capstone.project;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import matteofurgani.Capstone.project.reservations.NewReservationDTO;
import matteofurgani.Capstone.project.reservations.TestReservation;

@SpringBootApplication
public class CapstoneToelettaHelpApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneToelettaHelpApplication.class, args);
		
       
	}

}
