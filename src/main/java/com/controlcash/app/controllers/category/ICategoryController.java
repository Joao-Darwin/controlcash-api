package com.controlcash.app.controllers.category;

import com.controlcash.app.dtos.category.request.CategoryRequestDTO;
import com.controlcash.app.dtos.category.response.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Categories", description = "Endpoints for managing Categories")
public interface ICategoryController {

    @Operation(
            summary = "Create a new category",
            description = "Creates a new category by providing a JSON, XML, or YML representation of the category.",
            tags = {"Categories"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> create(CategoryRequestDTO categoryRequestDTO);

    @Operation(
            summary = "Fetch all categories",
            description = "Retrieves a paginated list of all categories, with sorting and pagination options.",
            tags = {"Categories"},
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
    ResponseEntity<?> findAll(int page, int size, String sort);

    @Operation(
            summary = "Fetch a category by ID",
            description = "Retrieves a category's details by its unique identifier (UUID).",
            tags = {"Categories"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
                    ),
                    @ApiResponse(description = "Category Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> findById(UUID id);

    @Operation(
            summary = "Update a category",
            description = "Updates a category's details by passing its unique identifier (UUID) and the updated data.",
            tags = {"Categories"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Category Not Found", responseCode = "404"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> update(CategoryRequestDTO categoryRequestDTO, UUID id);

    @Operation(
            summary = "Delete a category",
            description = "Deletes a category by its unique identifier (UUID).",
            tags = {"Categories"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "Category Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> delete(UUID id);
}
