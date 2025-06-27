package student_mana.Model;

import lombok.Data;

@Data
public class Student {
    private int studentId;
    private String name;
    private String gender;
    private int age;
    private int classId;
    private String className;
    private double totalCredit;

    // Getters and setters
}
