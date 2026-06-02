package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.request.PositionRequest;
import hrd.com.hrms.dto.response.PositionResponse;
import hrd.com.hrms.service.PositionService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PositionResponse>> createPosition(@Valid @RequestBody PositionRequest request) {
        PositionResponse response = positionService.createPosition(request);
        return new ResponseEntity<>(new ApiResponse<>("Position created successfully", response, true, java.time.LocalDateTime.now()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PositionResponse>> getPositionById(@PathVariable UUID id) {
        PositionResponse response = positionService.getPositionById(id);
        return ResponseEntity.ok(new ApiResponse<>("Position fetched successfully", response, true, java.time.LocalDateTime.now()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PositionResponse>>> getAllPositions() {
        List<PositionResponse> response = positionService.getAllPositions();
        return ResponseEntity.ok(new ApiResponse<>("All positions fetched successfully", response, true, java.time.LocalDateTime.now()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PositionResponse>> updatePosition(@PathVariable UUID id, @Valid @RequestBody PositionRequest request) {
        PositionResponse response = positionService.updatePosition(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Position updated successfully", response, true, java.time.LocalDateTime.now()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePosition(@PathVariable UUID id) {
        positionService.deletePosition(id);
        return ResponseEntity.ok(new ApiResponse<>("Position deleted successfully", null, true, java.time.LocalDateTime.now()));
    }
}
