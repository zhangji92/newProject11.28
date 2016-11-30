package com.yoka.mrskin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.yoka.mrskin.model.data.YKCity;

public class SearchAdapter<T> extends BaseAdapter implements Filterable
{
    private List<T> mObjects;

    private List<Set<String>> pinyinList;// 支持多音字,类似:{{z,c},{j},{z},{q,x}}的集合

    private final Object mLock = new Object();

    private int mResource;

    private int mFieldId = 0;

    @SuppressWarnings("unused")
    private Context mContext;

    private ArrayList<T> mOriginalValues;
    private ArrayFilter mFilter;

    private LayoutInflater mInflater;

    public static final int ALL = -1;// 全部
    private int maxMatch = 100;// 最多显示多少个可能选项

    /**
     * 支持多音字
     */
    public SearchAdapter(Context context, int textViewResourceId, T[] objects,
            int maxMatch)
        {
            init(context, textViewResourceId, 0, Arrays.asList(objects));
            this.pinyinList = getHanziSpellList(objects);
            this.maxMatch = maxMatch;
        }

    public SearchAdapter(Context context, int textViewResourceId,
            List<T> objects, int maxMatch)
        {
            init(context, textViewResourceId, 0, objects);
            this.pinyinList = getHanziSpellList(objects);
            this.maxMatch = maxMatch;
        }

    private void init(Context context, int resource, int textViewResourceId,
            List<T> objects)
    {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = resource;
        mObjects = objects;
        mFieldId = textViewResourceId;
    }

    /**
     * 获得汉字拼音首字母列表
     */
    private List<Set<String>> getHanziSpellList(T[] hanzi)
    {
        List<Set<String>> listSet = new ArrayList<Set<String>>();
        PinYin pinyin = new PinYin();
        for (int i = 0; i < hanzi.length; i++) {
            YKCity c = (YKCity) mObjects.get(i);
            listSet.add(pinyin.getPinyin(c.getmCity()));
        }
        return listSet;
    }

    /**
     * 获得汉字拼音首字母列表
     */
    private List<Set<String>> getHanziSpellList(List<T> hanzi)
    {
        List<Set<String>> listSet = new ArrayList<Set<String>>();
        PinYin pinyin = new PinYin();
        for (int i = 0; i < hanzi.size(); i++) {
            YKCity c = (YKCity) mObjects.get(i);
            listSet.add(pinyin.getPinyin(c.getmCity()));
        }
        return listSet;
    }

    public int getCount()
    {
        return mObjects.size();
    }

    public T getItem(int position)
    {
        return mObjects.get(position);
    }

    public int getPosition(T item)
    {
        return mObjects.indexOf(item);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView,
            ViewGroup parent, int resource)
    {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                text = (TextView) view;
            } else {
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter",
                    "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        YKCity c = (YKCity) mObjects.get(position);
        String content = c.getmCity();
        if (!TextUtils.isEmpty(c.getmCityDesc())) {
            content += "(" + c.getmCityDesc() + ")";
        }
        text.setText(content);

        return view;
    }

    public Filter getFilter()
    {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    @SuppressLint("DefaultLocale")
    private class ArrayFilter extends Filter
    {
        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence prefix)
        {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<T>(mObjects);//
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    // ArrayList<T> list = new ArrayList<T>();//无
                    ArrayList<T> list = new ArrayList<T>(mOriginalValues);// List<T>
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final ArrayList<T> hanzi = mOriginalValues;// 汉字String
                final int count = hanzi.size();

                final Set<T> newValues = new HashSet<T>(count);// 支持多音字,不重复
                final ArrayList<T> resultsValues = new ArrayList<T>(count);// 支持多音字,不重复

                for (int i = 0; i < count; i++) {
                    final T value = hanzi.get(i);// 汉字String
                    final String valueText = value.toString().toLowerCase();// 汉字String
                    final Set<String> pinyinSet = pinyinList.get(i);// 支持多音字,类似:{z,c}
                    @SuppressWarnings("rawtypes")
                    Iterator iterator = pinyinSet.iterator();// 支持多音字
                    while (iterator.hasNext()) {// 支持多音字
                        final String pinyin = iterator.next().toString()
                                .toLowerCase();// 取出多音字里的一个字母

                        if (pinyin.indexOf(prefixString) != -1) {// 任意匹配
                            if(!newValues.contains(value)){
                                resultsValues.add(value);
                                newValues.add(value);
                            }
                        } else if (valueText.indexOf(prefixString) != -1) {// 如果是汉字则直接添加
                            if(!newValues.contains(value)){
                                resultsValues.add(value);
                                newValues.add(value);
                            }
                        }
                    }
                    if (maxMatch > 0) {// 有数量限制
                        if (resultsValues.size() > maxMatch - 1) {// 不要太多
                            break;
                        }
                    }

                }
//                List<T> list = Set2List(newValues);// 转成List
                results.values = resultsValues;
                results.count = resultsValues.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint,
                FilterResults results)
        {

            mObjects = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    // List Set 相互转换
    @SuppressWarnings("hiding")
    public <T extends Object> Set<T> List2Set(List<T> tList)
    {
        Set<T> tSet = new HashSet<T>(tList);
        return tSet;
    }

    @SuppressWarnings("hiding")
    public <T extends Object> List<T> Set2List(Set<T> oSet)
    {
        List<T> tList = new ArrayList<T>(oSet);
        return tList;
    }
}