package fitpet_be.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "estimates")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimate_id")
    private Long id;

    @Column(name = "estimate_pet_info", nullable = false)
    private String petInfo;

    @Column(name = "estimate_pet_name", nullable = false)
    private String petName;

    @Column(name = "estimate_pet_age", nullable = false)
    private Long petAge;

    @Column(name = "estimate_pet_species", nullable = false)
    private String petSpecies;

    @Column(name = "estimate_phone_number", nullable = false)
    private String phoneNumber;

    @Column(name  ="estimate_more_info")
    private String moreInfo;

    @Column(name = "estimate_agreement", nullable = false)
    private boolean agreement;

    @Builder
    public Estimate(String petInfo, String petName,
                    Long petAge, String petSpecies,
                    String phoneNumber, String moreInfo,
                    boolean agreement) {

        this.petInfo = petInfo;
        this.petName = petName;
        this.petAge = petAge;
        this.petSpecies = petSpecies;
        this.phoneNumber = phoneNumber;
        this.moreInfo = moreInfo;
        this.agreement = agreement;

    }

}
