package dao;

import dto.AttendanceRecordsDTO;
import org.apache.ibatis.annotations.*;
import java.util.Date;
import java.util.List;

@Mapper
public interface AttendanceRecordsMapper {

    @Select("SELECT * FROM attendance_records")
    List<AttendanceRecordsDTO> findAll();

    @Update("UPDATE attendance_records SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);

    @Select("SELECT * FROM attendance_records WHERE id = #{id}")
    AttendanceRecordsDTO findById(Integer id);

    @Select("SELECT * FROM attendance_records WHERE date BETWEEN #{startDate} AND #{endDate}")
    List<AttendanceRecordsDTO> findByDateRange(@Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate);

    @Insert("INSERT INTO attendance_records(studentname, date, status) " +
            "VALUES(#{studentName}, #{date}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AttendanceRecordsDTO record);

    @Update("UPDATE attendance_records SET " +
            "studentname = #{studentName}, " +
            "date = #{date}, " +
            "status = #{status} " +
            "WHERE id = #{id}")
    int update(AttendanceRecordsDTO record);

    @Delete("DELETE FROM attendance_records WHERE id = #{id}")
    int delete(Integer id);
}
