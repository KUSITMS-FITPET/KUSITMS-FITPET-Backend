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
@Table(name = "urls")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Url extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id", nullable = false)
    private Long id;

    @Column(name = "url_value", nullable = false)
    private String value;

    @Column(name = "url_name", nullable = false)
    private String name;

    @Builder
    public Url(String value, String name) {

        this.value = value;
        this.name = name;

    }
}
