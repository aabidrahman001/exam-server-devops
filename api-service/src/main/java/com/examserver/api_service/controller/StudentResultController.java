
package com.examserver.api_service.controller;

import com.examserver.api_service.entity.StudentResult;
import com.examserver.api_service.entity.StudentResultId;
import com.examserver.api_service.repository.StudentResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class StudentResultController {

    @Autowired
    private StudentResultRepository repository;

    //READ
    @GetMapping("/{examYear}")
    public List<StudentResult> getResultsByYear(@PathVariable Integer examYear) {
        return repository.findByExamYear(examYear);
    }

    @GetMapping("/")
    public List<StudentResult> getAllResults() {
        return repository.findAll();
    }

    //CREATE
    @PostMapping("/")
    public StudentResult createResult(@RequestBody StudentResult result) {
        return repository.save(result);
    }

    //UPDATE
    @PutMapping("/{rollNumber}/{examYear}")
    public StudentResult updateResult(@PathVariable Long rollNumber,
                                  @PathVariable Integer examYear,
                                  @RequestBody StudentResult updatedResult) {
    StudentResultId id = new StudentResultId(rollNumber, examYear);
    return repository.findById(id)
            .map(result -> {
                result.setMarks(updatedResult.getMarks());
                return repository.save(result);
            })
            .orElseGet(() -> repository.save(updatedResult));
    }

    @DeleteMapping("/{rollNumber}/{examYear}")
    public void deleteResult(@PathVariable Long rollNumber, @PathVariable Integer examYear) {
    repository.deleteById(new StudentResultId(rollNumber, examYear));
    }

}
