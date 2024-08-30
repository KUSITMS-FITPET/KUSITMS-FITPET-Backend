package fitpet_be.domain.model;

import fitpet_be.common.BaseTimeEntity;
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
@Table(name = "partnerships")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Partnership extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partnership_id", nullable = false)
    private Long id;

    @Column(name = "partnership_name", nullable = false)
    private String name;

    @Column(name = "partnership_email", nullable = false)
    private String email;

    @Column(name = "partnership_phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "partnership_contetn", nullable = false)
    private String content;

    @Column(name = "partnership_agreement", nullable = false)
    private boolean agreement;

    @Builder
    public Partnership(String name, String email,
                       String phoneNumber, String content,
                       boolean agreement) {

        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.content = content;
        this.agreement = agreement;

    }

}
