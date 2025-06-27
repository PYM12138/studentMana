package student_mana.Model;
import lombok.Data;

@Data
public class ActivityRecord {
    private int recordId;
    private int studentId;
    private int activityId;
    private java.sql.Date participationDate;
    private String activityName;

}
