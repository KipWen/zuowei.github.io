package dao;

import dto.StudentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentDao {
    List<StudentDTO> findAllStudents();
    StudentDTO findStudentById(Integer id);
    StudentDTO findStudentByStudentId(String studentId);
    int insertStudent(StudentDTO student);
    int updateStudent(StudentDTO student);
    int deleteStudent(Integer id);
}
