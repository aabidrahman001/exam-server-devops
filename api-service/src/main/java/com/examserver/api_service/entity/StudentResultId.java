package com.examserver.api_service.entity;

import java.io.Serializable;
import java.util.Objects;

public class StudentResultId implements Serializable {
    private Long rollNumber;
    private Integer examYear;

    public StudentResultId() {}

    public StudentResultId(Long rollNumber, Integer examYear) {
        this.rollNumber = rollNumber;
        this.examYear = examYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentResultId)) return false;
        StudentResultId that = (StudentResultId) o;
        return Objects.equals(rollNumber, that.rollNumber) &&
               Objects.equals(examYear, that.examYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rollNumber, examYear);
    }
}
