package com.controlcash.app.controllers.goal;

import com.controlcash.app.dtos.goal.request.GoalCreateRequestDTO;
import com.controlcash.app.dtos.goal.request.GoalUpdateRequestDTO;
import com.controlcash.app.dtos.goal.response.GoalCompleteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Goals", description = "Endpoints for managing financial goals")
public interface IGoalController {

    @Operation(
            summary = "Create a new goal",
            description = "Creates a new financial goal by providing a JSON, XML, or YML representation of the goal.",
            tags = {"Goals"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = GoalCompleteResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<GoalCompleteResponseDTO> create(GoalCreateRequestDTO goalCreateRequestDTO);

    @Operation(
            summary = "Fetch all goals",
            description = "Retrieves a paginated list of all financial goals, with sorting and pagination options.",
            tags = {"Goals"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = GoalCompleteResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> findAll(int page, int size, String sort);

    @Operation(
            summary = "Fetch a goal by ID",
            description = "Retrieves the details of a financial goal by its unique identifier (UUID).",
            tags = {"Goals"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = GoalCompleteResponseDTO.class))
                    ),
                    @ApiResponse(description = "Goal Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> findById(UUID id);

    @Operation(
            summary = "Update a goal",
            description = "Updates a financial goal's details by providing its unique identifier (UUID) and the updated data.",
            tags = {"Goals"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Goal Not Found", responseCode = "404"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> update(GoalUpdateRequestDTO goalUpdateRequestDTO, UUID id);

    @Operation(
            summary = "Delete a goal",
            description = "Deletes a financial goal by its unique identifier (UUID).",
            tags = {"Goals"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "Goal Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> delete(UUID id);
}
