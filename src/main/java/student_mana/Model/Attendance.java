package student_mana.Model;

import java.sql.Date;
import lombok.Data;

@Data
public class Attendance {
    private int attendanceId;
    private int studentId;
    private Date date;
    private String status;

}