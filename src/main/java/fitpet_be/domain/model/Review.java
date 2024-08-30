package fitpet_be.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long id;

    @Column(name = "review_pet_info", nullable = false)
    private String petInfo;

    @Column(name = "review_pet_age", nullable = false)
    private Long petAge;

    @Column(name = "review_pet_species", nullable = false)
    private String petSpecies;

    @Column(name = "review_content", nullable = false)
    private String content;

    @Builder
    public Review(
            String petInfo, Long petAge,
            String petSpecies, String content) {

        this.petInfo = petInfo;
        this.petAge = petAge;
        this.petSpecies = petSpecies;
        this.content = content;

    }

}
