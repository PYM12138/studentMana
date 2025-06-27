package student_mana.Model;
import lombok.Data;

@Data
public class Activity {
    private int activityId;
    private String activityName;
    private java.sql.Date activityDate;
    private String activityDescription;
}
