package yuekao.app01.com.app01practice.mode;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 * 课程所以的相关信息
 */
public class CourseList {
    private String classNo;
    private String className;
    private Teacher teacher;
    private List<Student> students;

    /**
     * 添加学生
     * 1、不能重复添加课程
     * 2、只能添加20人
     *
     * @param stu
     * @return
     */
    public boolean addStudent(Student stu) {
        //对象为空时不能添加学生
        if (students == null) {
            return false;
        }
        //当学生等于20人时，不能添加学生
        if (students.size() >= 20) {
            return false;
        }
        //遍历List集合
        for (Student s : students) {
            if (s.getStudentName().equals(stu.getStudentName())) {
                return false;
            }
        }
        students.add(stu);
        return true;
    }

    /**
     * 查询该课程的详情信息
     * @return 返回查询内容
     */
    public String selectCourse() {
        String temp = "";
        String course = this.className;
        int len = students.size();
        temp = course + "," + len + "人听课，还有" + (20 - len) + "人可以选择该课程！";
        return temp;
    }

    public CourseList() {
    }

    public CourseList(String classNo, String className, Teacher teacher, List<Student> students) {
        this.classNo = classNo;
        this.className = className;
        this.teacher = teacher;
        this.students = students;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
