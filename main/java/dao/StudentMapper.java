package dao;


import entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Select("SELECT * FROM student")
    List<Student> findAllStudents();

    @Select("SELECT * FROM student WHERE id = #{id}")
    Student findStudentById(Integer id);

    @Select("SELECT * FROM student WHERE studentid = #{studentId}")
    Student findByStudentId(String studentId);

    @Insert("INSERT INTO student (name, studentid, grade, classname, gender) " +
            "VALUES (#{name}, #{studentId}, #{grade}, #{className}, #{gender})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Student student);

    @Update("UPDATE student SET " +
            "name = #{name}, " +
            "studentid = #{studentId}, " +
            "grade = #{grade}, " +
            "classname = #{className}, " +
            "gender = #{gender} " +
            "WHERE id = #{id}")
    void update(Student student);

    @Delete("DELETE FROM student WHERE id = #{id}")
    void delete(Integer id);

    @Select("SELECT COUNT(1) > 0 FROM student WHERE id = #{id}")
    boolean existsById(Integer id);
}
