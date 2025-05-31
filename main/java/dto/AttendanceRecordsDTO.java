package dto;
import lombok.Data;
import java.util.Date;

@Data
public class AttendanceRecordsDTO {

    private Integer id;
    private String studentName;
    private Date date;
    private String status; // 出勤、迟到、缺勤、请假、早退
}
