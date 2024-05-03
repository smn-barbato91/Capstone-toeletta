package matteofurgani.Capstone.project.reservations;

import io.micrometer.common.util.StringUtils;
import matteofurgani.Capstone.project.exceptions.BadRequestException;
import matteofurgani.Capstone.project.exceptions.InvalidDateException;
import matteofurgani.Capstone.project.exceptions.UserNotActiveException;
import matteofurgani.Capstone.project.pets.PetInfo;
import matteofurgani.Capstone.project.pets.PetInfoService;
import matteofurgani.Capstone.project.servicesType.ServiceType;
import matteofurgani.Capstone.project.servicesType.ServiceTypeService;
import matteofurgani.Capstone.project.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService rs;

    @Autowired
    private ServiceTypeService typeService;

    @Autowired
    private PetInfoService petService;


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> save(@RequestBody @Validated NewReservationDTO body, BindingResult validation) throws IOException {
    	if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        try {
            Reservation reservation = rs.save(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(new NewReservationRespDTO(reservation.getId()));
        } catch (InvalidDateException e) {
            // Gestisci l'eccezione InvalidDateException qui
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PostMapping("/check")
    public ResponseEntity<?> checkReservation(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                              @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        try {
            boolean exists = rs.isReservationExists(date, time);
            if (exists) {
                return ResponseEntity.ok("Una prenotazione esiste già per la data e l'ora specificate o per l'ora precedente.");
            } else {
                return ResponseEntity.ok("Nessuna prenotazione esiste per la data e l'ora specificate o per l'ora precedente.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la verifica della prenotazione.");
        }
    }


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Reservation> getReservations(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sort){
        return rs.getReservation(page, size, sort);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Reservation findById(@PathVariable int id){
        return rs.findById(id);
    }

    @PutMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Reservation findByIdAndUpdate(@PathVariable int reservationId, @RequestBody NewReservationDTO body) {
        return rs.findByIdAndUpdate(reservationId, body);
    }

    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable int reservationId) {
        rs.findByIdAndDelete(reservationId);
    }

    @GetMapping("/me/{date}/{time}")
    public Reservation getReservation(@AuthenticationPrincipal Reservation currentReservation) {
       return rs.findByDateAndTime(currentReservation.getDate(), currentReservation.getTime());
    }

    @PutMapping("/me/update/{date}/{time}")
    public Reservation updateReservation (@AuthenticationPrincipal Reservation currentReservation, @RequestBody Reservation reservation) {
        return rs.findByDateAndTimeAndUpdate(currentReservation.getDate(), currentReservation.getTime(), reservation);
    }

    @DeleteMapping("/me/delete/{date}/{time}")
    public void deleteReservation(@AuthenticationPrincipal Reservation currentReservation) {
        rs.findByDateAndTimeAndDelete(currentReservation.getDate(), currentReservation.getTime());
    }



}