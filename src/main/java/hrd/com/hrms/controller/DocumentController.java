package hrd.com.hrms.controller;

import hrd.com.hrms.model.EmployeeDocument;
import hrd.com.hrms.repository.EmployeeDocumentRepository;
import hrd.com.hrms.repository.EmployeeRepository;
import hrd.com.hrms.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final EmployeeDocumentRepository docRepo;
    private final FileStorageService fileService;
    private final EmployeeRepository empRepo;

    public DocumentController(EmployeeDocumentRepository docRepo, FileStorageService fileService, EmployeeRepository empRepo) {
        this.docRepo = docRepo;
        this.fileService = fileService;
        this.empRepo = empRepo;
    }

    @PostMapping("/upload/{employeeId}")
    public ResponseEntity<?> upload(@PathVariable UUID employeeId, @RequestParam("file") MultipartFile file) throws IOException {
        var emp = empRepo.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));
        String path = fileService.storeFile(file);

        EmployeeDocument doc = EmployeeDocument.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .fileUrl(path)
                .employee(emp)
                .build();

        return ResponseEntity.ok(docRepo.save(doc));
    }
}