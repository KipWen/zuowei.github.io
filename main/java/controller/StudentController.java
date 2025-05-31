package controller;


import dto.ResponseDTO;
import dto.StudentDTO;
import entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseDTO<List<Student>> getAllStudents() {
        ResponseDTO<List<Student>> responseDTO = new ResponseDTO<>();
        try {
            List<Student> students = studentService.findAllStudents();
            responseDTO.setCode("200");
            responseDTO.setMsg("success");
            responseDTO.setData(students);
        } catch (Exception e) {
            responseDTO.setCode("500");
            responseDTO.setMsg("获取学生列表失败: " + e.getMessage());
        }
        return responseDTO;
    }

    @GetMapping("/{id}")
    public ResponseDTO<Student> getStudentById(@PathVariable Integer id) {
        ResponseDTO<Student> responseDTO = new ResponseDTO<>();
        try {
            Student student = studentService.findStudentById(id);
            if (student == null) {
                responseDTO.setCode("404");
                responseDTO.setMsg("学生不存在");
            } else {
                responseDTO.setCode("200");
                responseDTO.setMsg("success");
                responseDTO.setData(student);
            }
        } catch (Exception e) {
            responseDTO.setCode("500");
            responseDTO.setMsg("获取学生信息失败: " + e.getMessage());
        }
        return responseDTO;
    }

    @PostMapping
    public ResponseDTO<Student> createStudent(@RequestBody StudentDTO studentDTO) {
        ResponseDTO<Student> responseDTO = new ResponseDTO<>();
        try {
            if (studentService.isStudentIdExists(studentDTO.getStudentId())) {
                responseDTO.setCode("400");
                responseDTO.setMsg("学号已存在");
                return responseDTO;
            }

            Student createdStudent = studentService.createStudent(studentDTO);
            responseDTO.setCode("200");
            responseDTO.setMsg("创建成功");
            responseDTO.setData(createdStudent);
        } catch (Exception e) {
            responseDTO.setCode("500");
            responseDTO.setMsg("创建学生失败: " + e.getMessage());
        }
        return responseDTO;
    }


    // 其他方法类似实现...

    // 4. 更新学生信息
    @PutMapping("/{id}")
    public ResponseDTO<Student> updateStudent(
            @PathVariable Integer id,
            @RequestBody StudentDTO studentDTO) {
        ResponseDTO<Student> responseDTO = new ResponseDTO<>();
        try {
            // 检查学生是否存在
            if (!studentService.existsById(id)) {
                responseDTO.setCode("404");
                responseDTO.setMsg("学生不存在");
                return responseDTO;
            }

            // 检查学号是否被其他学生使用
            Student studentWithSameId = studentService.findByStudentId(studentDTO.getStudentId());
            if (studentWithSameId != null && !studentWithSameId.getId().equals(id)) {
                responseDTO.setCode("400");
                responseDTO.setMsg("学号已被其他学生使用");
                return responseDTO;
            }

            Student updatedStudent = studentService.updateStudent(id, studentDTO);
            responseDTO.setCode("200");
            responseDTO.setMsg("更新成功");
            responseDTO.setData(updatedStudent);
        } catch (Exception e) {
            responseDTO.setCode("500");
            responseDTO.setMsg("更新学生信息失败: " + e.getMessage());
        }
        return responseDTO;
    }

    // 5. 删除学生
    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteStudent(@PathVariable Integer id) {
        ResponseDTO<Void> responseDTO = new ResponseDTO<>();
        try {
            // 检查学生是否存在
            if (!studentService.existsById(id)) {
                responseDTO.setCode("404");
                responseDTO.setMsg("学生不存在");
                return responseDTO;
            }

            studentService.deleteStudent(id);
            responseDTO.setCode("200");
            responseDTO.setMsg("删除成功");
        } catch (Exception e) {
            responseDTO.setCode("500");
            responseDTO.setMsg("删除学生失败: " + e.getMessage());
        }
        return responseDTO;
    }
}

