package com.controlcash.app.controllers.goal.impl;

import com.controlcash.app.controllers.goal.IGoalController;
import com.controlcash.app.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${path-api}/goals")
public class GoalController implements IGoalController {

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }
}
