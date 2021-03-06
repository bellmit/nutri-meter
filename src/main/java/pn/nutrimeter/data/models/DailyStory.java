package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.DailyStoryExercise;
import pn.nutrimeter.data.models.associations.DailyStoryFood;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "daily_stories")
public class DailyStory extends BaseEntity {

    @Column(name = "date", nullable = false, updatable = false)
    private LocalDate date;

    @Column(name = "daily_weight")
    private Double dailyWeight;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "dailyStory", cascade = CascadeType.ALL)
    private List<DailyStoryFood> dailyStoryFoodAssociation;

    @OneToMany(mappedBy = "dailyStory")
    private List<DailyStoryExercise> dailyStoryExerciseAssociation;

    @OneToMany(mappedBy = "dailyStory")
    private List<Note> notes;
}
