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
@Getter
@Table(name = "CardNews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cardnews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cardnews_id", nullable = false)
    private Long id;

    @Column(name = "cardnews_title", nullable = false)
    private String title;

    @Column(name = "cardnews_content", nullable = false)
    private String content;

    @Column(name = "cardnews_image_url", nullable = false)
    private String imageUrl;

    @Builder
    public Cardnews(String title, String content,
                    String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

}
