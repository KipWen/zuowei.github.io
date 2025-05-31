package controller;

import dto.AttendanceRecordsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AttendanceRecordsService;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceRecordsController {

    @Autowired
    private AttendanceRecordsService attendanceRecordService;

    @GetMapping
    public ResponseEntity<List<AttendanceRecordsDTO>> getAllRecords() {
        List<AttendanceRecordsDTO> records = attendanceRecordService.getAllRecords();
        return ResponseEntity.ok(records);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRecordStatus(
            @PathVariable Integer id,
            @RequestBody AttendanceRecordsDTO dto) {
        boolean success = attendanceRecordService.updateRecordStatus(id, dto.getStatus());
        if (success) {
            return ResponseEntity.ok("考勤记录更新成功");
        } else {
            return ResponseEntity.badRequest().body("考勤记录更新失败");
        }
    }

    @PostMapping
    public ResponseEntity<String> addRecord(@RequestBody AttendanceRecordsDTO record) {
        try {
            // 数据验证
            if (record.getStudentName() == null || record.getStudentName().isEmpty()) {
                return new ResponseEntity<>("学生姓名不能为空", HttpStatus.BAD_REQUEST);
            }
            if (record.getDate() == null) {
                return new ResponseEntity<>("日期不能为空", HttpStatus.BAD_REQUEST);
            }
            if (record.getStatus() == null || record.getStatus().isEmpty()) {
                return new ResponseEntity<>("状态不能为空", HttpStatus.BAD_REQUEST);
            }

            int result = attendanceRecordService.createRecord(record);
            if (result > 0) {
                return ResponseEntity.ok("考勤记录添加成功");
            } else {
                return new ResponseEntity<>("考勤记录添加失败", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("服务器错误: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
