package pn.nutrimeter.service.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.associations.DailyStoryFood;
import pn.nutrimeter.data.repositories.DailyStoryFoodRepository;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.FoodRepository;
import pn.nutrimeter.error.DailyStoryNotFoundException;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.services.api.DailyStoryFoodService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DailyStoryFoodServiceImplTest {

    private static final String ID = "id";
    private static final double QUANTITY = 100.0;

    @MockBean
    DailyStoryFoodRepository dailyStoryFoodRepository;

    @MockBean
    FoodRepository foodRepository;

    @MockBean
    DailyStoryRepository dailyStoryRepository;

    @Autowired
    DailyStoryFoodService dailyStoryFoodService;

    @Test
    public void create_withValidInput_shouldReturnCorrect() {
        LocalDate date = LocalDate.now();
        DailyStory dailyStory = mock(DailyStory.class);
        Food food = mock(Food.class);

        when(this.dailyStoryRepository.findByDateAndUserId(date, ID)).thenReturn(Optional.of(dailyStory));
        when(this.foodRepository.findById(ID)).thenReturn(Optional.of(food));

        this.dailyStoryFoodService.create(QUANTITY, date, ID, ID);

        ArgumentCaptor<DailyStoryFood> dsf = ArgumentCaptor.forClass(DailyStoryFood.class);
        verify(this.dailyStoryFoodRepository).save(dsf.capture());

        DailyStoryFood dailyStoryFood = dsf.getValue();
        assertNotNull(dailyStoryFood);
    }

    @Test
    public void create_withInvalidDate_shouldThrow() {
        assertThrows(DailyStoryNotFoundException.class, () -> this.dailyStoryFoodService.create(QUANTITY, null, ID, ID));
    }

    @Test
    public void create_withInvalidUserId_shouldThrow() {
        assertThrows(DailyStoryNotFoundException.class, () -> this.dailyStoryFoodService.create(QUANTITY, LocalDate.now(), ID, ID));
    }

    @Test
    public void create_withInvalidFoodId_shouldThrow() {
        LocalDate date = LocalDate.now();
        when(this.dailyStoryRepository.findByDateAndUserId(date, ID)).thenReturn(Optional.of(mock(DailyStory.class)));
        assertThrows(IdNotFoundException.class, () -> this.dailyStoryFoodService.create(QUANTITY, date, ID, ID));
    }
}