package com.controlcash.app.controllers.user;

import com.controlcash.app.dtos.user.request.UserCreateRequestDTO;
import com.controlcash.app.dtos.user.response.UserAllResponseDTO;
import com.controlcash.app.dtos.user.response.UserCreateResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Users", description = "Endpoints for managing Users")
public interface IUserController {

    @Operation(
            summary = "Adds a new User",
            description = "Adds a new User by passing in a JSON, XML or YML representation of the user!",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserCreateResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<UserCreateResponseDTO> create(UserCreateRequestDTO userCreateRequestDTO);

    @Operation(
            summary = "Fetch all users",
            description = "Retrieves a paginated list of all users, with sorting and pagination options.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<Page<UserAllResponseDTO>> findAll(int page, int size, String sort);

    @Operation(
            summary = "Fetch a user by ID",
            description = "Retrieves a user's details by their unique identifier (UUID).",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserAllResponseDTO.class))
                    ),
                    @ApiResponse(description = "User Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> findById(UUID id);

    @Operation(
            summary = "Update a user",
            description = "Updates a user's details by passing their unique identifier (UUID) and the updated data.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "User Not Found", responseCode = "404"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> update(UUID id, UserCreateRequestDTO userUpdated);

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by their unique identifier (UUID).",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "User Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> delete(UUID id);
}
