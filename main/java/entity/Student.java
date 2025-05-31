package entity;



import lombok.Data;

import java.util.Date;

@Data
public class Student {
    private Integer id;
    private String name;
    private String studentId;
    private String grade;
    private String className;
    private Integer gender;
    private Date createTime;
    private Date updateTime;
}
