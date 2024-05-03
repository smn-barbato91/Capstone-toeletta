package matteofurgani.Capstone.project.reservations;

import matteofurgani.Capstone.project.exceptions.NotFoundException;
import matteofurgani.Capstone.project.pets.PetInfo;
import matteofurgani.Capstone.project.pets.PetInfoService;
import matteofurgani.Capstone.project.servicesType.ServiceType;
import matteofurgani.Capstone.project.servicesType.ServiceTypeService;
import matteofurgani.Capstone.project.utilities.costs.CostGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationDAO rd;

    @Autowired
    private PetInfoService petService;

    @Autowired
    private ServiceTypeService typeService;

    public Reservation save(NewReservationDTO body) throws IOException{

        ServiceType serviceType = typeService.findByName(String.valueOf(body.serviceType()));
        PetInfo petInfo = petService.findById(body.petInfoId());

        Double cost = serviceType.getBaseCost();
        String petSize = String.valueOf(petInfo.getSize());
        String petHair = String.valueOf(petInfo.getHairType());

        CostGenerator cg = new CostGenerator();
        String finalCost = cg.generateProperCost(petHair,petSize, cost);

        Reservation reservation = new Reservation(body.date(), body.time(), serviceType, petInfo);
        reservation.setDate(body.date());
        reservation.setTime(body.time());
        reservation.setServiceType(serviceType);
        reservation.setPetInfo(petInfo);
        reservation.setCost(finalCost);
        return rd.save(reservation);
    }

    public Page<Reservation> getReservation(int page, int size, String sort){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return rd.findAll(pageable);
    }

    public Reservation findById(int id) {

        return rd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Reservation findByDateAndTime(LocalDate date, LocalTime time){
        return rd.findByDateAndTime(date, time).
                orElseThrow(() -> new NotFoundException("Reservation not found for date: " + date + " and time: " + time));
    }


    public Reservation findByIdAndUpdate(int id, NewReservationDTO body){
    	
    	 Double cost = 0D;
         String petSize = null;
         String petHair = null;


        Reservation found = this.findById(id);
      
        found.setDate(body.date());
        found.setTime(body.time());
       
        ServiceType st = typeService.findByName(body.serviceType());
        found.setServiceType(st);
        cost = st.getBaseCost();
      
       
        PetInfo petInfo = petService.findById(body.petInfoId());
 
        found.setPetInfo(petInfo);
            
        petSize = String.valueOf(petInfo.getSize());
        petHair = String.valueOf(petInfo.getHairType());
      
        CostGenerator cg = new CostGenerator();
        String finalCost = cg.generateProperCost(petHair,petSize, cost);
             
        found.setCost(finalCost);
      
        return rd.save(found);
    }


    public Reservation findByDateAndTimeAndUpdate(LocalDate date, LocalTime time, Reservation body) {
        Reservation found = this.findByDateAndTime(date, time);
        found.setDate(body.getDate());
        found.setTime(body.getTime());
        found.setServiceType(body.getServiceType());
        found.setPetInfo(body.getPetInfo());
        return rd.save(found);
    }

    public void findByIdAndDelete(int id) {
        Reservation reservation = rd.findById(id).orElseThrow(() -> new NotFoundException(id));
        rd.delete(reservation);
    }

    public void findByDateAndTimeAndDelete(LocalDate date, LocalTime time) {
        Reservation reservation = rd.findByDateAndTime(date, time).orElseThrow(() -> new NotFoundException("Reservation not found for date: " + date + " and time: " + time));
        rd.delete(reservation);
    }


}