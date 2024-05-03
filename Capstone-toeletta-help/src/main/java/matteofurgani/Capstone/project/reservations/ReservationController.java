package matteofurgani.Capstone.project.reservations;

import io.micrometer.common.util.StringUtils;
import matteofurgani.Capstone.project.exceptions.BadRequestException;
import matteofurgani.Capstone.project.exceptions.UserNotActiveException;
import matteofurgani.Capstone.project.pets.PetInfo;
import matteofurgani.Capstone.project.pets.PetInfoService;
import matteofurgani.Capstone.project.servicesType.ServiceType;
import matteofurgani.Capstone.project.servicesType.ServiceTypeService;
import matteofurgani.Capstone.project.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public NewReservationRespDTO save(@RequestBody @Validated NewReservationDTO body, BindingResult validation) throws IOException {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }

        Reservation reservation = rs.save(body);
        return new NewReservationRespDTO(reservation.getId());
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