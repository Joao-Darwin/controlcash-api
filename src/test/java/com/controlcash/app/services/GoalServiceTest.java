package com.controlcash.app.services;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import com.controlcash.app.dtos.goal.response.GoalSimpleResponseDTO;
import com.controlcash.app.models.Category;
import com.controlcash.app.models.Goal;
import com.controlcash.app.models.User;
import com.controlcash.app.repositories.GoalRepository;
import com.controlcash.app.utils.dates.DateFormatUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    private DateFormatUtils dateFormatUtils;
    private Goal goal;
    private GoalCreateRequestDTO goalCreateRequestDTO;
    private UUID id;

    @BeforeEach
    void setUp() throws ParseException {
        dateFormatUtils = DateFormatUtils.getInstance();
        Date createdDate = dateFormatUtils.convertStringToDate("01/08/2024");

        id = UUID.randomUUID();
        goal = new Goal(id, createdDate, 2500.0, new User(), new Category());
        goalCreateRequestDTO = new GoalCreateRequestDTO(createdDate, 2500.0, new User(), new Category());
    }

    @Test
    void testCreate_GivenAGoalCreateRequestDTO_ShouldSaveAndReturnAGoalCompleteResponseDTO() {
        Mockito.when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(goal);

        GoalCompleteResponseDTO actualGoalCompleteResponseDTO = goalService.create(goalCreateRequestDTO);

        Assertions.assertNotNull(actualGoalCompleteResponseDTO);
        Assertions.assertEquals(id, actualGoalCompleteResponseDTO.id());
        Assertions.assertEquals(goalCreateRequestDTO.dueDate(), actualGoalCompleteResponseDTO.dueDate());
        Assertions.assertEquals(goalCreateRequestDTO.value(), actualGoalCompleteResponseDTO.value());
    }

    @Test
    void testFindAll_GivenAPageable_ShouldReturnAGoalSimpleResponseDTOPage() throws ParseException {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        Goal goal2 = new Goal(UUID.randomUUID(), dateFormatUtils.convertStringToDate("01/09/2024"), 2400.0, new User(), new Category());
        List<Goal> goalList = List.of(goal, goal2);
        Page<Goal> goalPage = new PageImpl<>(goalList);
        Mockito.when(goalRepository.findAll(Mockito.any(Pageable.class))).thenReturn(goalPage);

        Page<GoalSimpleResponseDTO> actualGoalSimpleResponseDTOPage = goalService.findAll(pageable);

        Assertions.assertNotNull(actualGoalSimpleResponseDTOPage);
        Assertions.assertEquals(2, actualGoalSimpleResponseDTOPage.getSize());
    }

    @Test
    void testFindAll_GivenAPageableWithNotExistentPageNumber_ShouldReturnAnEmptyPage() {
        Pageable pageable = PageRequest.of(10, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        Page<Goal> goalPage = Page.empty();
        Mockito.when(goalRepository.findAll(Mockito.any(Pageable.class))).thenReturn(goalPage);

        Page<GoalSimpleResponseDTO> actualGoalSimpleResponseDTOPage = goalService.findAll(pageable);

        Assertions.assertNotNull(actualGoalSimpleResponseDTOPage);
        Assertions.assertTrue(actualGoalSimpleResponseDTOPage.isEmpty());
    }
}
