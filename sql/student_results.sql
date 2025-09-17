DROP TABLE IF EXISTS student_results CASCADE;

CREATE TABLE student_results (
    roll_number BIGINT NOT NULL,
    marks INT NOT NULL,
    exam_year INT NOT NULL,
    PRIMARY KEY (roll_number, exam_year)
) PARTITION BY RANGE (exam_year);

CREATE TABLE student_results_2023 PARTITION OF student_results
    FOR VALUES FROM (2023) TO (2024);

CREATE TABLE student_results_2024 PARTITION OF student_results
    FOR VALUES FROM (2024) TO (2025);

CREATE TABLE student_results_2025 PARTITION OF student_results
    FOR VALUES FROM (2025) TO (2026);

INSERT INTO student_results (roll_number, marks, exam_year)
SELECT
    generate_series(1, 333334) AS roll_number,
    (random()*100)::INT AS marks,
    2023 AS exam_year;

INSERT INTO student_results (roll_number, marks, exam_year)
SELECT
    generate_series(333335, 666667) AS roll_number,
    (random()*100)::INT AS marks,
    2024 AS exam_year;

INSERT INTO student_results (roll_number, marks, exam_year)
SELECT
    generate_series(666668, 1000000) AS roll_number,
    (random()*100)::INT AS marks,
    2025 AS exam_year;

SELECT COUNT(*) AS total_rows FROM student_results;

