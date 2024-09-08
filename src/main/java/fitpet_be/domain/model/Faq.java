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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "faqs")
public class Faq extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id", nullable = false)
    private Long id;

    @Column(name = "faq_category_id", nullable = false)
    private Long categoryId;

    @Column(name = "faq_category_name", nullable = false)
    private String categoryName;

    @Column(name = "faq_question", nullable = false)
    private String question;

    @Column(name = "faq_answer", nullable = false)
    private String answer;

    @Builder
    public Faq(Long categoryId, String categoryName,
               String question, String answer) {

        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.question = question;
        this.answer = answer;

    }

}
