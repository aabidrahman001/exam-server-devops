package com.examserver.api_service.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student_results")
@IdClass(StudentResultId.class)
public class StudentResult {

    @Id
    @Column(name = "roll_number")
    private Long rollNumber;

    @Id
    @Column(name = "exam_year")
    private Integer examYear;

    @Column(name = "marks")
    private Integer marks;

    // Constructors
    public StudentResult() {}

    public StudentResult(Long rollNumber, Integer examYear, Integer marks) {
        this.rollNumber = rollNumber;
        this.examYear = examYear;
        this.marks = marks;
    }

    // Getters and Setters
    public Long getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(Long rollNumber) {
        this.rollNumber = rollNumber;
    }

    public Integer getExamYear() {
        return examYear;
    }

    public void setExamYear(Integer examYear) {
        this.examYear = examYear;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }
}
