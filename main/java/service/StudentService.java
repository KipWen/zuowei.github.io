package service;

import dao.StudentMapper;
import dto.StudentDTO;

import entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    // 查询所有学生
    public List<Student> findAllStudents() {
        return studentMapper.findAllStudents();
    }

    // 根据ID查询学生
    public Student findStudentById(Integer id) {
        return studentMapper.findStudentById(id);
    }

    // 检查学号是否存在
    public boolean isStudentIdExists(String studentId) {
        return studentMapper.findByStudentId(studentId) != null;
    }

    // 检查学生是否存在
    public boolean existsById(Integer id) {
        return studentMapper.findStudentById(id) != null;
    }

    // 根据学号查询学生
    public Student findByStudentId(String studentId) {
        return studentMapper.findByStudentId(studentId);
    }

    // 添加学生
    public Student createStudent(StudentDTO dto) {
        Student student = convertToEntity(dto);
        studentMapper.insert(student); // 调用insert而非insertStudent
        return student;
    }

    // 更新学生
    public Student updateStudent(Integer id, StudentDTO studentDTO) {
        Student student = convertToEntity(studentDTO);
        student.setId(id);
        studentMapper.update(student);
        return studentMapper.findStudentById(id); // 返回更新后的学生对象
    }

    // 删除学生
    public void deleteStudent(Integer id) {
        studentMapper.delete(id);
    }

    // DTO转Entity的辅助方法
    private Student convertToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setStudentId(dto.getStudentId());
        student.setGrade(dto.getGrade());
        student.setClassName(dto.getClassName());
        student.setGender(dto.getGender());
        return student;
    }
}
