package service;

import dao.AttendanceRecordsMapper;
import dto.AttendanceRecordsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceRecordsService {

    @Autowired
    private AttendanceRecordsMapper attendanceRecordsMapper;

    public List<AttendanceRecordsDTO> getAllRecords() {
        return attendanceRecordsMapper.findAll();
    }

    public boolean updateRecordStatus(Integer id, String status) {
        return attendanceRecordsMapper.updateStatus(id, status) > 0;
    }

    public AttendanceRecordsDTO getRecordById(Integer id) {
        return attendanceRecordsMapper.findById(id);
    }

    public int createRecord(AttendanceRecordsDTO record) {
        return attendanceRecordsMapper.insert(record);
    }

    public int updateRecord(AttendanceRecordsDTO record) {
        return attendanceRecordsMapper.update(record);
    }

    public int deleteRecord(Integer id) {
        return attendanceRecordsMapper.delete(id);
    }




}