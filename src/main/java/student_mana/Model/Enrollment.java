package student_mana.Model;

import lombok.Data;

@Data
public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int courseId;
    private double grade;
    private Course course;
}
