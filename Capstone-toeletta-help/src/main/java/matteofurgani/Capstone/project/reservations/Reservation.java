package matteofurgani.Capstone.project.reservations;


import jakarta.persistence.*;
import lombok.*;
import matteofurgani.Capstone.project.pets.PetInfo;
import matteofurgani.Capstone.project.servicesType.ServiceType;


import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate date;
    private LocalTime time;
    private String cost;
    @ManyToOne
    @JoinColumn(name = "service_type", referencedColumnName = "name")
    private ServiceType serviceType;
    @ManyToOne
    @JoinColumn(name = "pet_info_id", referencedColumnName = "id")
    private PetInfo petInfo;

    public Reservation(LocalDate date, LocalTime time, ServiceType serviceType, PetInfo petInfo) {
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.serviceType = serviceType;
        this.petInfo = petInfo;
    }

	public Reservation() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public PetInfo getPetInfo() {
		return petInfo;
	}

	public void setPetInfo(PetInfo petInfo) {
		this.petInfo = petInfo;
	}

    
}