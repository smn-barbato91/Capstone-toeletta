package matteofurgani.Capstone.project.pets;


import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import matteofurgani.Capstone.project.reservations.Reservation;

@Entity
@Table(name = "pets_info")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PetInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private Size size;
    @Enumerated(EnumType.STRING)
    private HairType hairType;
    @OneToMany(mappedBy = "petInfo")
    private List<Reservation> reservations;

    public PetInfo(Size size, HairType hairType) {
        this.size = size;
        this.hairType = hairType;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public HairType getHairType() {
		return hairType;
	}

	public void setHairType(HairType hairType) {
		this.hairType = hairType;
	}
    
    
}
