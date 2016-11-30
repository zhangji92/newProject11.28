package yuekao.app01.com.app01practice;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import yuekao.app01.com.app01practice.mode.CourseList;
import yuekao.app01.com.app01practice.mode.Student;
import yuekao.app01.com.app01practice.mode.Teacher;

/**
 * Created by Administrator on 2016/8/10.
 *  课程列表数据源
 */
public class CourseApplication extends Application {

    public List<CourseList> lists = new ArrayList<>();
    public List<AppCompatActivity> activities=new ArrayList<>();



    public List<CourseList> getLists() {
        return lists;
    }

    public void setLists(List<CourseList> lists) {
        this.lists = lists;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lists.add(new CourseList("1","数学",new Teacher("王老师"),new ArrayList<Student>()));
        lists.add(new CourseList("2","语文",new Teacher("刘老师"),new ArrayList<Student>()));
        lists.add(new CourseList("3","英语",new Teacher("唐老师"),new ArrayList<Student>()));
        lists.add(new CourseList("4","化学",new Teacher("李老师"),new ArrayList<Student>()));
        lists.add(new CourseList("5","物理",new Teacher("赵老师"),new ArrayList<Student>()));


    }

}
