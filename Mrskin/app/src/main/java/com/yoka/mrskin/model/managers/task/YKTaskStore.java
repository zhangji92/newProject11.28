package com.yoka.mrskin.model.managers.task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;

import android.text.TextUtils;

import com.yoka.mrskin.model.data.YKDate;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.util.YKFile;

public class YKTaskStore
{
    public static String CACHE_NAME = "taskstore";
    public static String STOREITEM_CACHE = "storeitem";
    private static YKTaskStore singleton = null;
    private static Object lock = new Object();
    private ArrayList<StoreItem> mStoreItemList;

    public static YKTaskStore getInstnace() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKTaskStore();
            }
        }
        return singleton;
    }

    public ArrayList<StoreItem> getStoreTable() {
        return mStoreItemList;
    }

    private YKTaskStore()
    {
        mStoreItemList = new ArrayList<YKTaskStore.StoreItem>();
        initStoreItemList();
    }

    private boolean saveDataToFile(YKTask ykTask) {
        JSONObject object = ykTask.toJson();
        String string = object.toString();
        byte[] data = null;
        try {
            data = string.getBytes("utf-8");

        } catch (Exception e) {
            return false;
        } 
        if (data != null) {
            YKFile.save(CACHE_NAME + ykTask.getID(), data);
        }
        return true;
    }

    public boolean clearAllData() {
        for (int i = 0; i < mStoreItemList.size(); i++) {
            StoreItem storeItem = mStoreItemList.get(i);
            String tempTaskId = storeItem.getTaskId();
            YKFile.remove(CACHE_NAME + tempTaskId);
        }
        mStoreItemList.clear();
        YKFile.remove(STOREITEM_CACHE);
        return true;
    }

    private YKTask loadDataFromFile(String taskId) {
        byte[] data = YKFile.read(CACHE_NAME + taskId);
        String str;
        YKTask tasks = new YKTask();
        try {
            str = new String(data,"utf-8");
            JSONObject object = new JSONObject(str);
            tasks = YKTask.parse(object);
            updateAllSubtaskStatusIfNeed(tasks);

            return tasks;
        } catch (Exception e) {
        }
        return null;
    }

    public YKTask loadData(String taskId) {
        if (TextUtils.isEmpty(taskId)) {
            return null;
        }
        return loadDataFromFile(taskId);
    }

    public void saveData(YKTask ykTask) {
        if (ykTask == null) {
            return;
        }
        String taskId = ykTask.getID();
        StoreItem storeItem = findStoreItemById(taskId);
        if (storeItem == null) {
            storeItem = new StoreItem(taskId, CACHE_NAME + taskId);
            mStoreItemList.add(storeItem);
            saveStoreItemList();
        }
        saveDataToFile(ykTask);
    }

    public void removeData(String taskId) {
        if (TextUtils.isEmpty(taskId)) {
            return;
        }
        StoreItem storeItem = findStoreItemById(taskId);
        if (storeItem == null) {
            return;
        } else {
            mStoreItemList.remove(storeItem);
            saveStoreItemList();
            boolean result = YKFile.remove(CACHE_NAME + taskId);
            System.out.println("删除计划结果  " + result);
        }
    }

    public void updateSubtaskStatu(String taskId, String subTaskId, int status) {
        YKTask parentTask = loadData(taskId);
        YKTask task = findSubtaskById(parentTask, subTaskId);
        if (task == null) {
            return;
        }
        updateAllSubtaskStatusIfNeed(parentTask);
        task.setmStatus(status);
        saveData(parentTask);
    }

    public void updateAllSubtaskStatusIfNeed(YKTask ykTaskParent) {
        boolean needModifyStatus = ykTaskParent.getmCycleTime() == -1;
        if (!needModifyStatus) {
            return;
        }
        YKDate reminData = ykTaskParent.getmRemindTime();
        if (reminData == null) {
            reminData = new YKDate();
            ykTaskParent.setmRemindTime(reminData);
        }
        long todayZero = TimeUtil.getTodayZero();
        if (reminData.getmMills() == todayZero) {
            return;
        }
        reminData.setmMills(todayZero);
        ArrayList<YKTask> subTaskList = ykTaskParent.getmSubtask();
        if (subTaskList == null) {
            return;
        }
        int size = subTaskList.size();
        for (int j = 0; j < size; j++) {
            YKTask subYkTask = subTaskList.get(j);
            if (subYkTask == null) {
                continue;
            }
            subYkTask.resetTaskStatus();
        }
    }

    public void updateSubtaskRemindTime(String taskId, String subTaskId,
            long time) {
        YKTask parentTask = loadData(taskId);
        YKTask task = findSubtaskById(parentTask, subTaskId);
        if (task == null) {
            return;
        }
        YKDate date = task.getmRemindTime();
        date.setmMills(time);
        saveData(parentTask);
    }

    public void updateTaskCanRemind(String taskId, boolean canRemind) {
        if (TextUtils.isEmpty(taskId)) {
            return;
        }
        YKTask task = loadData(taskId);
        if (task == null) {
            return;
        }
        task.setmCanreMind(canRemind);
        saveData(task);
    }

    public YKTask findSubtaskById(String taskId, String subTaskId) {
        YKTask task = loadData(taskId);
        return findSubtaskById(task, subTaskId);
    }

    public YKTask findSubtaskById(YKTask task, String subTaskId) {
        if (task == null || TextUtils.isEmpty(subTaskId)) {
            return null;
        }
        ArrayList<YKTask> subTasks = task.getmSubtask();
        if (subTasks == null) {
            return null;
        }
        int size = subTasks.size();
        for (int i = 0; i < size; i++) {
            YKTask subtask = subTasks.get(i);
            if (subtask == null) {
                continue;
            }
            if (subTaskId.equals(subtask.getID())) {
                return subtask;
            }
        }
        return null;
    }

    private StoreItem findStoreItemById(String taskId) {
        if (TextUtils.isEmpty(taskId)) {
            return null;
        }
        for (int i = 0; i < mStoreItemList.size(); i++) {
            StoreItem storeItem = mStoreItemList.get(i);
            String tempTaskId = storeItem.getTaskId();
            if (taskId.equals(tempTaskId)) {
                return storeItem;
            }
        }
        return null;
    }

    public boolean saveTaskList(ArrayList<YKTask> taskList) {
        if (taskList == null) {
            return false;
        }

        ArrayList<YKTask> tmpList = new ArrayList<YKTask>();
        for (YKTask task : taskList) {
            tmpList.add(0, task);
        }
        taskList = tmpList;

        for (int i = 0; i < taskList.size(); i++) {
            saveData(taskList.get(i));
        }
        return true;
    }

    public ArrayList<YKTask> getTaskList() {
        if (mStoreItemList == null) {
            return null;
        }
        ArrayList<YKTask> taskList = new ArrayList<YKTask>();
        for (int i = 0; i < mStoreItemList.size(); i++) {
            StoreItem storeItem = mStoreItemList.get(i);
            taskList.add(loadData(storeItem.getTaskId()));
        }
        return taskList;
    }

    public static class StoreItem implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private String taskId;
        private String taskFileName;

        public StoreItem(String taskId, String taskFileName)
        {
            super();
            this.taskId = taskId;
            this.taskFileName = taskFileName;
        }

        public String getTaskId() {
            return taskId;
        }

        public String getTaskFileName() {
            return taskFileName;
        }

    }

    private void saveStoreItemList() {
        if (mStoreItemList == null) {
            return;
        }
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        int count = 0;
        try {
            count = mStoreItemList.size();
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            StoreItem storeItem;
            oos.writeInt(count);
            for (int i = 0; i < count; ++i) {
                storeItem = mStoreItemList.get(i);
                oos.writeObject(storeItem);
            }
            data = baos.toByteArray();
        } catch (Exception e) {
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }
        YKFile.save(STOREITEM_CACHE, data);
    }

    private void initStoreItemList() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(STOREITEM_CACHE);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            int count = ois.readInt();
            StoreItem storeItem;
            for (int i = 0; i < count; ++i) {
                storeItem = (StoreItem) ois.readObject();
                mStoreItemList.add(storeItem);
            }
        } catch (Exception e) {
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                bais.close();
            } catch (Exception e) {
            }
        }
    }
}
