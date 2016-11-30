package yuekao.app01.com.app01practice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import yuekao.app01.com.app01practice.mode.CourseList;

/**
 * Created by Administrator on 2016/8/9.
 * 课程列表
 */
public class CourseAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<CourseList> list;


    public CourseAdapter(Context context, List<CourseList> list) {
        this.layoutInflater = layoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size() == 0 ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = layoutInflater.inflate(R.layout.courselist, null);
        TextView classNo_textView = (TextView) view1.findViewById(R.id.classNo);
        TextView className_textView = (TextView) view1.findViewById(R.id.className);
        TextView teacher_textView = (TextView) view1.findViewById(R.id.teacher);
        TextView student_textView = (TextView) view1.findViewById(R.id.student);
        classNo_textView.setText(list.get(i).getClassNo());
        className_textView.setText(list.get(i).getClassName());
        teacher_textView.setText(list.get(i).getTeacher().getTeacherName());
        student_textView.setText(String.valueOf(list.get(i).getStudents().size()));
        return view1;
    }
}
