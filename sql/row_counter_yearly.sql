SELECT exam_year, COUNT(*) AS total_rows
FROM student_results
GROUP BY exam_year
ORDER BY exam_year;
