package Application;

import java.util.List;

public class Instance {
    private int taskNum; // the number of tasks
    private List<List<Object>> tasks; // the tasks data
    private int timeCap; // the time cap of the instance
    private double resourceCap; // the resource cap of the instance
    private double resourceGap; // the resource gap of the instance
    private double acceptResourceGap; // the accept resource gap of the instance
    private List<List<Integer>> pretasksArray; // the pretasks array of the instance
    private double resourceLowerBound; // the resource lower bound of the instance

    public Instance() {
    }

    public Instance(int taskNum, List<List<Object>> tasks, int timeCap, double resourceCap, double resourceGap, double acceptResourceGap, List<List<Integer>> pretasksArray, double resourceLowerBound) {
        this.taskNum = taskNum;
        this.tasks = tasks;
        this.timeCap = timeCap;
        this.resourceCap = resourceCap;
        this.resourceGap = resourceGap;
        this.acceptResourceGap = acceptResourceGap;
        this.pretasksArray = pretasksArray;
        this.resourceLowerBound = resourceLowerBound;
    }

    /**
     * 获取
     * @return taskNum
     */
    public int getTaskNum() {
        return taskNum;
    }

    /**
     * 设置
     * @param taskNum
     */
    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    /**
     * 获取
     * @return tasks
     */
    public List<List<Object>> getTasks() {
        return tasks;
    }

    /**
     * 设置
     * @param tasks
     */
    public void setTasks(List<List<Object>> tasks) {
        this.tasks = tasks;
    }

    /**
     * 获取
     * @return timeCap
     */
    public int getTimeCap() {
        return timeCap;
    }

    /**
     * 设置
     * @param timeCap
     */
    public void setTimeCap(int timeCap) {
        this.timeCap = timeCap;
    }

    /**
     * 获取
     * @return resourceCap
     */
    public double getResourceCap() {
        return resourceCap;
    }

    /**
     * 设置
     * @param resourceCap
     */
    public void setResourceCap(double resourceCap) {
        this.resourceCap = resourceCap;
    }

    /**
     * 获取
     * @return resourceGap
     */
    public double getResourceGap() {
        return resourceGap;
    }

    /**
     * 设置
     * @param resourceGap
     */
    public void setResourceGap(double resourceGap) {
        this.resourceGap = resourceGap;
    }

    /**
     * 获取
     * @return acceptResourceGap
     */
    public double getAcceptResourceGap() {
        return acceptResourceGap;
    }

    /**
     * 设置
     * @param acceptResourceGap
     */
    public void setAcceptResourceGap(double acceptResourceGap) {
        this.acceptResourceGap = acceptResourceGap;
    }

    /**
     * 获取
     * @return pretasksArray
     */
    public List<List<Integer>> getPretasksArray() {
        return pretasksArray;
    }

    /**
     * 设置
     * @param pretasksArray
     */
    public void setPretasksArray(List<List<Integer>> pretasksArray) {
        this.pretasksArray = pretasksArray;
    }

    /**
     * 获取
     * @return resourceLowerBound
     */
    public double getResourceLowerBound() {
        return resourceLowerBound;
    }

    /**
     * 设置
     * @param resourceLowerBound
     */
    public void setResourceLowerBound(double resourceLowerBound) {
        this.resourceLowerBound = resourceLowerBound;
    }

    public String toString() {
        return "Instance{taskNum = " + taskNum + ", tasks = " + tasks + ", timeCap = " + timeCap + ", resourceCap = " + resourceCap + ", resourceGap = " + resourceGap + ", acceptResourceGap = " + acceptResourceGap + ", pretasksArray = " + pretasksArray + ", resourceLowerBound = " + resourceLowerBound + "}";
    }
}
