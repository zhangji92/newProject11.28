package yuekao.app01.com.app01practice.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/9.
 */
public class Student implements Serializable{

    private String studentName;
    private int sex;//0:男 1：女
    private int age;

    public Student() {
    }

    public Student(String studentName, int sex, int age) {
        this.studentName = studentName;
        this.sex = sex;
        this.age = age;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
