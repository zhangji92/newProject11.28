package com.yoka.mrskin.model.managers.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import android.text.TextUtils;

import com.yoka.mrskin.model.data.YKDate;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;

public class YKTaskManager extends Observable
{
    private final static boolean IS_DEBUG = false;
    private static YKTaskManager singleton = null;
    private static Object lock = new Object();

    public static YKTaskManager getInstnace() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKTaskManager();
            }
        }
        return singleton;
    }
    

    public void notifyTaskDataChanged() {
        setChanged();
        notifyObservers();
    }

    public ArrayList<YKTask> getTaskList() {
        return YKTaskStore.getInstnace().getTaskList();
    }

    public boolean addTask(YKTask task) {
        YKTaskStore.getInstnace().saveData(task);
        return true;
    }

    public boolean removeTask(String taskId) {
        YKTaskStore.getInstnace().removeData(taskId);
        return true;
    }

    public boolean clearAllTask() {
        YKTaskStore.getInstnace().clearAllData();
        return true;
    }

    public void resetTaskStatusIfNeed() {
        ArrayList<YKTask> tempList = getTaskList();
        int nativeSize = tempList.size();
        if (tempList == null || nativeSize < 1) {
            return;
        }
        for (int i = 0; i < nativeSize; i++) {
            YKTask ykTaskParent = tempList.get(i);
            if (ykTaskParent == null) {
                continue;
            }
            ArrayList<YKTask> subTaskList = ykTaskParent.getmSubtask();
            if (subTaskList == null) {
                continue;
            }
            boolean needModifyTime = ykTaskParent.getmCycleTime() == -1;
            if (needModifyTime) {
                YKTaskStore.getInstnace().updateAllSubtaskStatusIfNeed(
                        ykTaskParent);
                YKTaskStore.getInstnace().saveData(ykTaskParent);
            }
        }
    }

    // public ArrayList<YKTask> getTodayTaskList() {
    // ArrayList<YKTask> tempList = getTaskList();
    // if (tempList == null) {
    // return null;
    // }
    // ArrayList<YKTask> taskList = new ArrayList<YKTask>();
    // for (int i = 0; i < tempList.size(); i++) {
    // YKTask ykTask = tempList.get(i);
    // ArrayList<YKTask> subTaskList = ykTask.getmSubtask();
    // if (subTaskList == null) {
    // continue;
    // }
    // for (int j = 0; j < subTaskList.size(); i++) {
    // YKTask subYkTask = subTaskList.get(i);
    // YKDate ykDate = subYkTask.getmRemindTime();
    // if (TimeUtil.isToday(ykDate.getmMills())) {
    // taskList.add(subYkTask);
    // }
    // }
    // }
    // return taskList;
    // }

    public YKTask getTaskById(String parentId) {
        if (TextUtils.isEmpty(parentId)) {
            return null;
        }
        YKTask task = YKTaskStore.getInstnace().loadData(parentId);
        return task;
    }

    /**
     * 首页任务,有序
     * 
     * @return
     */
    public ArrayList<HomeCardData> getHomeCardData() {
        ArrayList<YKTask> tempList = getTaskList();
        int nativeSize = tempList.size();
        if (tempList == null || nativeSize < 1) {
            return null;
        }
        if (IS_DEBUG) {
            System.out.println("xxx  getHomeCardData  " + nativeSize);
        }
        ArrayList<HomeCardData> taskList = new ArrayList<HomeCardData>();
        for (int i = 0; i < nativeSize; i++) {
            YKTask ykTaskParent = tempList.get(i);
            if (IS_DEBUG) {
                System.out.println("xxx  getHomeCardData  i  " + i
                        + "  ykTaskParent " + ykTaskParent);
            }
            if (ykTaskParent == null) {
                continue;
            }
            ArrayList<YKTask> subTaskList = ykTaskParent.getmSubtask();
            if (subTaskList == null) {
                continue;
            }
            boolean needModifyTime = ykTaskParent.getmCycleTime() == -1;
            int size = subTaskList.size();
            if (needModifyTime) {
                for (int j = 0; j < size; j++) {
                    YKTask subYkTask = subTaskList.get(j);
                    if (!isTaskComplete(subYkTask)) {
                        YKDate ykDate = subYkTask.getmRemindTime();
                        ykDate.setmMills(ykDate.getmMills()
                                + TimeUtil.getTodayZero());
                        if (!TimeUtil.isBefor(ykDate.getmMills())) {
                            if (IS_DEBUG) {
                                taskList.add(new HomeCardData(ykTaskParent
                                        .getmTitle()
                                        + "("
                                        + subYkTask.getmDesc() + ")", subYkTask
                                        .getmTitle(), subYkTask.getID(),
                                        subYkTask.getmParentId(), ykDate,
                                        subYkTask.getmScore(), ykTaskParent
                                                .ismCanreMind(), subYkTask
                                                .getmRemindContent()));
                            } else {
                                taskList.add(new HomeCardData(ykTaskParent
                                        .getmTitle(), subYkTask.getmTitle(),
                                        subYkTask.getID(), subYkTask
                                                .getmParentId(), ykDate,
                                        subYkTask.getmScore(), ykTaskParent
                                                .ismCanreMind(), subYkTask
                                                .getmRemindContent()));
                            }

                        }
                    }
                }
            } else {
                for (int j = 0; j < size; j++) {
                    YKTask subYkTask = subTaskList.get(j);
                    if (isHomeCardData(subYkTask)) {
                        if (IS_DEBUG) {
                            taskList.add(new HomeCardData(ykTaskParent
                                    .getmTitle()
                                    + "("
                                    + subYkTask.getmDesc()
                                    + ")", subYkTask.getmTitle(), subYkTask
                                    .getID(), subYkTask.getmParentId(),
                                    subYkTask.getmRemindTime(), subYkTask
                                            .getmScore(), ykTaskParent
                                            .ismCanreMind(), subYkTask
                                            .getmRemindContent()));
                        } else {
                            taskList.add(new HomeCardData(ykTaskParent
                                    .getmTitle(), subYkTask.getmTitle(),
                                    subYkTask.getID(),
                                    subYkTask.getmParentId(), subYkTask
                                            .getmRemindTime(), subYkTask
                                            .getmScore(), ykTaskParent
                                            .ismCanreMind(), subYkTask
                                            .getmRemindContent()));
                        }
                    }
                }
            }
        }
        return sortHomeCardData(taskList);
    }

    // public ArrayList<ScoreCardData> getScoreCardData() {
    // ArrayList<YKTask> tempList = getTaskList();
    // int nativeSize = tempList.size();
    // if (tempList == null || nativeSize < 1) {
    // return null;
    // }
    // if (IS_DEBUG) {
    // System.out.println("xxx  getScoreCardData  " + nativeSize);
    // }
    // ArrayList<ScoreCardData> taskList = new ArrayList<ScoreCardData>();
    // for (int i = 0; i < nativeSize; i++) {
    // YKTask ykTaskParent = tempList.get(i);
    // if (IS_DEBUG) {
    // System.out.println("xxx  getHomeCardData  i  " + i
    // + "  ykTaskParent " + ykTaskParent);
    // }
    // if (ykTaskParent == null) {
    // continue;
    // }
    // ArrayList<YKTask> subTaskList = ykTaskParent.getmSubtask();
    // if (subTaskList == null) {
    // continue;
    // }
    // boolean needModifyTime = ykTaskParent.getmCycleTime() == -1;
    // int size = subTaskList.size();
    // if (needModifyTime) {
    // for (int j = 0; j < size; j++) {
    // YKTask subYkTask = subTaskList.get(j);
    // YKDate ykDate = subYkTask.getmRemindTime();
    // ykDate.setmMills(ykDate.getmMills()
    // + TimeUtil.getTodayZero());
    // if (!TimeUtil.isBefor(ykDate.getmMills())) {
    // if (IS_DEBUG) {
    // taskList.add(new ScoreCardData(ykTaskParent
    // .getmTitle()
    // + "("
    // + subYkTask.getmDesc()
    // + ")", subYkTask.getmTitle(), subYkTask
    // .getID(), subYkTask.getmParentId(), ykDate,
    // subYkTask.getmScore(), subYkTask
    // .getmStatus()));
    // } else {
    // taskList.add(new ScoreCardData(ykTaskParent
    // .getmTitle(), subYkTask.getmTitle(),
    // subYkTask.getID(),
    // subYkTask.getmParentId(), ykDate, subYkTask
    // .getmScore(), subYkTask
    // .getmStatus()));
    // }
    //
    // }
    // }
    // } else {
    // for (int j = 0; j < size; j++) {
    // YKTask subYkTask = subTaskList.get(j);
    // YKDate ykDate = subYkTask.getmRemindTime();
    // if (TimeUtil.isToday(ykDate.getmMills())) {
    // if (IS_DEBUG) {
    // taskList.add(new ScoreCardData(ykTaskParent
    // .getmTitle()
    // + "("
    // + subYkTask.getmDesc()
    // + ")", subYkTask.getmTitle(), subYkTask
    // .getID(), subYkTask.getmParentId(),
    // subYkTask.getmRemindTime(), subYkTask
    // .getmScore(), subYkTask
    // .getmStatus()));
    // } else {
    // taskList.add(new ScoreCardData(ykTaskParent
    // .getmTitle(), subYkTask.getmTitle(),
    // subYkTask.getID(),
    // subYkTask.getmParentId(), subYkTask
    // .getmRemindTime(), subYkTask
    // .getmScore(), subYkTask
    // .getmStatus()));
    // }
    // }
    // }
    // }
    // }
    // return taskList;
    // }

    /**
     * 卡片排序
     * 
     * @param sourceListData
     * @return
     */
    private ArrayList<HomeCardData> sortHomeCardData(
            ArrayList<HomeCardData> sourceListData) {
        if (sourceListData == null) {
            return null;
        }
        if (IS_DEBUG) {
            System.out.println(" sortHomeCardData old sort  start");
            for (int j = 0; j < sourceListData.size(); j++) {
                System.out.println(sourceListData.get(j).getmId() + "   "
                        + sourceListData.get(j).getmTitle() + "   "
                        + sourceListData.get(j).getmSubTitle() + "   "
                        + sourceListData.get(j).getmYkDate().getmMills());
            }
            System.out.println(" sortHomeCardData old sort  end");
        }
        ArrayList<HomeCardData> taskList = new ArrayList<HomeCardData>();
        for (int i = 0; i < sourceListData.size(); i++) {
            HomeCardData sourceData = sourceListData.get(i);
            if (sourceData == null) {
                continue;
            }
            boolean isAdd = false;
            for (int j = 0; j < taskList.size(); j++) {
                if (taskList.get(j).getmYkDate().getmMills() > sourceData
                        .getmYkDate().getmMills()) {
                    taskList.add(j, sourceData);
                    isAdd = true;
                    break;
                }
            }
            if (!isAdd) {
                taskList.add(sourceData);
            }

        }
        if (IS_DEBUG) {
            System.out.println(" sortHomeCardData new sort  start");
            for (int j = 0; j < taskList.size(); j++) {
                System.out.println(taskList.get(j).getmId() + "   "
                        + taskList.get(j).getmTitle() + "   "
                        + taskList.get(j).getmSubTitle() + "   "
                        + taskList.get(j).getmYkDate().getmMills());
            }
            System.out.println(" sortHomeCardData new sort  end");
        }
        return taskList;
    }

    private final static int DEFAULT_SCORE = 50;

    /**
     * 获取当天的完成分数。默认50分
     * 
     * @return
     */
    public int getHomeScore() {
        ArrayList<YKTask> tempList = getTaskList();
        int score = DEFAULT_SCORE;
        if (tempList == null || tempList.size() < 1) {
            return score;
        }
        for (int i = 0; i < tempList.size(); i++) {
            YKTask ykTaskParent = tempList.get(i);
            if (ykTaskParent == null) {
                continue;
            }
            boolean needModifyTime = ykTaskParent.getmCycleTime() == -1;
            ArrayList<YKTask> subTaskList = ykTaskParent.getmSubtask();
            if (subTaskList == null) {
                continue;
            }
            int size = subTaskList.size();
            if (needModifyTime) {
                for (int j = 0; j < size; j++) {
                    YKTask subYkTask = subTaskList.get(j);
                    if (subYkTask.isFinished()) {
                        score += subYkTask.getmScore();
                    }
                }
            } else {
                for (int j = 0; j < size; j++) {
                    YKTask subYkTask = subTaskList.get(j);
                    YKDate ykDate = subYkTask.getmRemindTime();
                    if (TimeUtil.isToday(ykDate.getmMills())
                            && subYkTask.isFinished()) {
                        score += subYkTask.getmScore();
                    }
                }
            }
        }
        return score;
    }

    /**
     * 更新子任务状态
     * 
     * @param taskId
     * @param subTaskId
     * @param status
     */
    public void updateSubTaskStatu(String taskId, String subTaskId, int status) {
        YKTaskStore.getInstnace().updateSubtaskStatu(taskId, subTaskId, status);
    }

    public void updateSubtaskRemindTime(String taskId, String subTaskId,
            long time) {
        YKTaskStore.getInstnace().updateSubtaskRemindTime(taskId, subTaskId,
                time);
    }

    public void updateTaskCanRemind(String taskId, boolean canRemind) {
        YKTaskStore.getInstnace().updateTaskCanRemind(taskId, canRemind);
    }

    public YKTask findSubtaskById(String taskId, String subTaskId) {
        return YKTaskStore.getInstnace().findSubtaskById(taskId, subTaskId);
    }

    /**
     * 传入父任务对象，返回今天的子任务是否完成
     * 
     * @param parentTask
     *            父任务对象
     * @return
     */
    public boolean isTodayComplete(YKTask parentTask) {
        if (parentTask == null) {
            return false;
        }
        ArrayList<YKTask> subTaskList = parentTask.getmSubtask();
        if (subTaskList == null) {
            return false;
        }
        boolean isComplete = true;
        for (int i = 0; i < subTaskList.size(); i++) {
            YKTask subYkTask = subTaskList.get(i);
            if (subYkTask == null) {
                continue;
            }
            if (parentTask.getmCycleTime() != -1
                    && !TimeUtil
                            .isToday(subYkTask.getmRemindTime().getmMills())) {
                continue;
            }
            if (!subYkTask.isFinished()) {
                isComplete = false;
                break;
            }
        }
        return isComplete;
    }

    /**
     * 返回此任务进行到周期里的第几天
     * 
     * @param parentTask
     * @return
     */
    public int getIndexInCycle(YKTask parentTask) {
        if (parentTask == null) {
            return 0;
        }
        ArrayList<YKTask> subTaskList = parentTask.getmSubtask();
        if (subTaskList == null) {
            return 0;
        }
        if (parentTask.getmCycleTime() == -1) {
            int count = 0;
            for (int i = 0; i < subTaskList.size(); i++) {
                YKTask subYkTask = subTaskList.get(i);
                if (subYkTask.isFinished()) {
                    count++;
                }
            }
            return count;
        } else {
            boolean isNotStart = true;
            for (int i = 0; i < subTaskList.size(); i++) {
                YKTask subYkTask = subTaskList.get(i);
                if (subYkTask == null) {
                    continue;
                }
                long time = subYkTask.getmRemindTime().getmMills();
                if (TimeUtil.isBefor(time)) {
                    isNotStart = false;
                }
                if (!TimeUtil.isToday(time)) {
                    continue;
                }
                return subYkTask.getmIndexTime();
            }
            if (isNotStart) {
                return 0;
            } else {
                return parentTask.getmCycleTime();
            }
        }
    }

    public int getCompletePercentage(YKTask parentTask) {
        if (parentTask == null) {
            return 0;
        }
        ArrayList<YKTask> subTaskList = parentTask.getmSubtask();
        if (subTaskList == null) {
            return 0;
        }
        int size = subTaskList.size();
        int completeCount = 0;
        for (int i = 0; i < subTaskList.size(); i++) {
            YKTask subYkTask = subTaskList.get(i);
            if (subYkTask == null) {
                continue;
            }
            if (subYkTask.isFinished()) {
                completeCount++;
            }
        }
        int result = Math.round(completeCount * 100 / size);
        return result;
    }

    private boolean isHomeCardData(YKTask subYkTask) {
        YKDate ykDate = subYkTask.getmRemindTime();
        boolean isToday = false;
        if (TimeUtil.isToday(ykDate.getmMills())) {
            isToday = true;
        }
        boolean isBefor = false;
        if (TimeUtil.isBefor(ykDate.getmMills())) {
            isBefor = true;
        }

        boolean isTaskComplete = isTaskComplete(subYkTask);
        return isToday && !isBefor && !isTaskComplete;
    }

    private boolean isTaskComplete(YKTask subYkTask) {
        return (subYkTask.isFailed() || subYkTask.isFinished());
    }

    public int getUnfinishedTaskNumber()
    {
        ArrayList<YKTask> mYkTasks = YKTaskManager.getInstnace().getTaskList();
        int taskNUmber = 0;
        if (mYkTasks != null
                && YKCurrentUserManager.getInstance().getUser() != null
                && mYkTasks.size() != 0) {
            
            long currentTime = System.currentTimeMillis();
            String currentTimeString = TimeUtil.forTimeForYearMonthDay(currentTime);
            long taskTime;
            String taskTimeString;
                    
            for (YKTask task : mYkTasks) {
                if (task == null) continue;
                for (YKTask subTask : task.getmSubtask()) {
                    
                    if (!subTask.isFinished() && !subTask.isOverdued(task.getmCycleTime() != -1)) {
                        if (task.getmCycleTime() == -1) {
                            ++taskNUmber;
                        } else {
                            taskTime = subTask.getmRemindTime().getmMills();
                            taskTimeString = TimeUtil.forTimeForYearMonthDay(taskTime);
                            if (currentTimeString.equals(taskTimeString)) {
                                ++taskNUmber;
                            }
                            
                        }
                    }
                }
            }
        }
        return taskNUmber;
    }

    public static class HomeCardData implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = -3260374053564081622L;
        // 卡片title,父任务title
        private String mTitle;
        // 卡片subtitle 子任务title
        private String mSubTitle;

        private String mId;// 子任务id
        private String mParent_id; // 父任务id
        private String mRemindContent;
        private YKDate mYkDate;
        private boolean mCanRemind;
        private int mScore;

        public HomeCardData(String mTitle, String mSubTitle, String mId,
                String mParent_id, YKDate mYkDate, int mScore,
                boolean mCanRemind, String mRemindContent)
        {
            super();
            this.mTitle = mTitle;
            this.mSubTitle = mSubTitle;
            this.mId = mId;
            this.mParent_id = mParent_id;
            this.mYkDate = mYkDate;
            this.mScore = mScore;
            this.mCanRemind = mCanRemind;
            this.mRemindContent = mRemindContent;
        }

        public String getmTitle() {
            return mTitle;
        }

        public String getmSubTitle() {
            return mSubTitle;
        }

        public String getmId() {
            return mId;
        }

        public String getmParent_id() {
            return mParent_id;
        }

        public YKDate getmYkDate() {
            return mYkDate;
        }

        public int getmScore() {
            return mScore;
        }

        public boolean ismCanRemind() {
            return mCanRemind;
        }

        public String getmRemindContent() {
            return mRemindContent;
        }

    }

}
