package com.controlcash.app.controllers.permission;

import com.controlcash.app.dtos.permission.request.PermissionCreateRequestDTO;
import com.controlcash.app.dtos.permission.request.PermissionUpdateRequestDTO;
import com.controlcash.app.dtos.permission.response.AllPermissionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Permissions", description = "Endpoints for managing Permissions")
public interface IPermissionController {

    @Operation(
            summary = "Create a new permission",
            description = "Creates a new permission by providing a JSON, XML, or YML representation of the permission.",
            tags = {"Permissions"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = AllPermissionResponseDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> create(PermissionCreateRequestDTO permission);

    @Operation(
            summary = "Fetch all permissions",
            description = "Retrieves a paginated list of all permissions, with sorting and pagination options.",
            tags = {"Permissions"},
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
    ResponseEntity<Page<AllPermissionResponseDTO>> findAll(int page, int size, String sort);

    @Operation(
            summary = "Fetch a permission by ID",
            description = "Retrieves a permission's details by its unique identifier (UUID).",
            tags = {"Permissions"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = AllPermissionResponseDTO.class))
                    ),
                    @ApiResponse(description = "Permission Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> findById(UUID id);

    @Operation(
            summary = "Update a permission",
            description = "Updates a permission's details by passing its unique identifier (UUID) and the updated data.",
            tags = {"Permissions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Permission Not Found", responseCode = "404"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> update(PermissionUpdateRequestDTO permissionUpdateRequestDTO, UUID id);

    @Operation(
            summary = "Delete a permission",
            description = "Deletes a permission by its unique identifier (UUID).",
            tags = {"Permissions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "Permission Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Error", responseCode = "500")
            }
    )
    ResponseEntity<?> delete(UUID id);
}
