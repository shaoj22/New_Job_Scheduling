package Application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OblHeftHeuristic {
    // main function
    public static void main(String[] args) {
        //from csv file read tasks dependency and job scheduling data
        String Solution = new String();
        List<String> Solutions = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            String JobSchedulingTableFilePath = "data/RandomData/Job_Scheduling_Table_" + i + ".csv";
            String TasksPretasksListTableFilePath = "data/RandomData/Tasks_Pretasks_List_Table_" + i + ".csv";
            List<List<Integer>> pretasksArray = ReadData.readCSVAndConvertToList(TasksPretasksListTableFilePath);
            List<List<Object>> tasks = ReadData.readCSVToList(JobSchedulingTableFilePath);
            // create instance
            Instance instance = raedDataToInstance(pretasksArray, tasks);
            // transmit the instance to the OLBHeuristicMain and get the solutionT
            long startTime = System.currentTimeMillis(); // get the start time
            int[] SolutionT = getIterSolution(instance); // get the solutionT
            long endTime = System.currentTimeMillis(); // get the end time
            long totalTime = endTime - startTime; // calculate the total time
            // print the result and time
            System.out.println("Instances  " + i + "  total time = " + totalTime + "ms" + "   " + "resource_cap = " + (double)SolutionT[SolutionT.length - 1]/100000);
            Solution = i + ","
                    + tasks.size() + ","
                    + totalTime + ","
                    + (double) SolutionT[SolutionT.length - 1] / 100000;
            Solutions.add(Solution);
        }
        writeSolutionsToCSV("data/java_solution.csv", Solutions);
    }
    public static void writeSolutionsToCSV(String csvFilePath, List<String> solutions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            // 写入CSV文件头
            writer.write("Iteration,Solution\n");

            // 写入每个迭代的Solution
            for (int i = 0; i < solutions.size(); i++) {
                String solution = solutions.get(i);
                writer.write(i + "," + solution + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // create instance
    public static Instance raedDataToInstance(List<List<Integer>> pretasksArray, List<List<Object>> tasks) {
        // create instance
        int timeCap = 1440;
        double resourceCap = 1.0;
        double resourceGap = 1.2;
        double acceptResourceGap = 0.01;
        double resourceLowerBound = getResourceLowerBound(tasks);
        Instance instance = new Instance(
                tasks.size(),
                tasks,
                timeCap,
                resourceCap,
                resourceGap,
                acceptResourceGap,
                pretasksArray,
                resourceLowerBound);

        return instance;
    }

    // task assignment
    public static ArrayList<Integer> getInsertTasksList(Instance instance, int time_now, ArrayList<Integer> unscheduledTasks, ArrayList<Integer> scheduledTasks, double resource_now, double resource_cap) {
        ArrayList<Integer> readyTasks = new ArrayList<>(); // 存储已经准备好的任务
        ArrayList<Integer> insertTasksList = new ArrayList<>(); // 存储可以插入的任务
        ArrayList<Integer> readyTasksLatestStartTime = new ArrayList<>(); // 可插入任务的最晚开始时间
        // 获取已经准备好的任务
        for (int i = 0; i < unscheduledTasks.size(); i++) {
            boolean isCalculatePretasks = getCalculatePretasks(instance, scheduledTasks, unscheduledTasks.get(i));
            if (time_now >= (int) instance.getTasks().get(unscheduledTasks.get(i)).get(4) && isCalculatePretasks == true) {
                readyTasks.add(unscheduledTasks.get(i));
                readyTasksLatestStartTime.add((int) instance.getTasks().get(unscheduledTasks.get(i)).get(5) - (int) instance.getTasks().get(unscheduledTasks.get(i)).get(2));
            }
        }
        // 从准备好的任务中获取可以插入的任务
        while (readyTasks.size() != 0) {
            int minStartTime = Collections.min(readyTasksLatestStartTime);
            int minStartTimeIndex = readyTasksLatestStartTime.indexOf(minStartTime);
            int insertTask = readyTasks.get(minStartTimeIndex);
            resource_now = resource_now + (double) instance.getTasks().get(insertTask).get(3);
            if (resource_now >= resource_cap) {
                break;
            } else {
                insertTasksList.add(insertTask);
                unscheduledTasks.remove(Integer.valueOf(insertTask));
                readyTasks.remove(Integer.valueOf(insertTask));
                readyTasksLatestStartTime.remove(Integer.valueOf(minStartTime));
            }
        }

        return insertTasksList;
    }

    // get pretasks
    public static boolean getCalculatePretasks(Instance instance, ArrayList<Integer> scheduledTasks, int task_id) {
        boolean isCalculatePretasks = true; // if all the pretasks of the task_id are scheduled, return true
        for (int i = 0; i < instance.getPretasksArray().get(task_id).size(); i++) {
            if (!scheduledTasks.contains(instance.getPretasksArray().get(task_id).get(i))) {
                isCalculatePretasks = false; // if there is a pretask of the task_id is not scheduled, return false
                break;
            }
        }

        return isCalculatePretasks;
    }


    // get resourceLowerBound
    public static double getResourceLowerBound(List<List<Object>> tasks) {
        double resourceLowerBound = 0.0;
        for (int i = 0; i < tasks.size(); i++) {
            Object num1 = tasks.get(i).get(3); // get the resource of the task
            Object num2 = tasks.get(i).get(2); // get the duration time of the task
            int a1 = Integer.parseInt(num2.toString());
            double a2 = Double.parseDouble(num1.toString());
            resourceLowerBound += a1 * a2;
        }
        return resourceLowerBound / 1440;
    }

    // Iterative
    public static int[] getIterSolution(Instance instance) {
        double resourceGap = instance.getResourceGap();
        double upperBound = instance.getResourceCap();
        int[] outPutSolutionT = new int[instance.getTasks().size() + 1]; // 记录要输出的solutionT,最后一位表示是否是可行解，若为1则表示为可行解，否则为不可行解
        // 通过二分法来确定最终的upperBound
        while (resourceGap > instance.getAcceptResourceGap()) {
            if (upperBound > resourceGap && upperBound - resourceGap > instance.getResourceLowerBound()) {
                int[] solutionT = getMainStructureSolution(instance, upperBound - resourceGap); // 通过OLBHeuristicMain来获取solutionT
                if (solutionT[solutionT.length - 1] == 1) {
                    outPutSolutionT = solutionT;
                    upperBound = upperBound - resourceGap;
                    if (resourceGap != instance.getAcceptResourceGap()) {
                        resourceGap = resourceGap / 2;
                    }
                } else {
                    resourceGap = resourceGap / 2;
                }
            } else {
                resourceGap = resourceGap / 2;
            }
        }
        // 将upperBound转换为int类型，并且将其放入outPutSolutionT中
        double roundedValue = Math.round(upperBound * 100000.0) / 100000.0;
        double multipliedValue = roundedValue * 100000;
        int intUpperBound = (int) multipliedValue;
        outPutSolutionT[outPutSolutionT.length - 1] = intUpperBound;

        return outPutSolutionT;
    }

    // MainStructure
    public static int[] getMainStructureSolution(Instance instance, double resourceCap) {
        int[] solutionT = new int[instance.getTasks().size() + 1]; // 初始化每个任务开始的执行时间为0
        ArrayList<Integer> schedulingTasks = new ArrayList<>(); //存储正在调度的任务
        ArrayList<Integer> unscheduledTasks = new ArrayList<>(); //存储未调度的任务
        for (int i = 0; i < instance.getTasks().size(); i++) {
            unscheduledTasks.add(i);
        } // 初始化未调度的任务列表
        ArrayList<Integer> scheduledTasks = new ArrayList<>(); //存储已经调度的任务
        double resourceNow = 0.0; //记录当前的资源占用
        for (int i = 0; i < instance.getTimeCap(); i++) {
            ArrayList<Integer> checkTasks = new ArrayList<>(schedulingTasks); //存储检查的任务
            for (int i1 = 0; i1 < checkTasks.size(); i1++) {
                if (i >= (solutionT[checkTasks.get(i1)] + (int) instance.getTasks().get(checkTasks.get(i1)).get(2))) {
                    schedulingTasks.remove(Integer.valueOf(checkTasks.get(i1))); // 将已经调度的任务从正在调度的任务列表中移除
                    scheduledTasks.add(checkTasks.get(i1)); // 将已经调度的任务加入到已调度的任务列表中
                    resourceNow = resourceNow - (double) instance.getTasks().get(checkTasks.get(i1)).get(3);
                }
            }
            ArrayList<Integer> insertTasksList = getInsertTasksList(instance, i, unscheduledTasks, scheduledTasks, resourceNow, resourceCap); // 获取插入的任务列表
            for (int i2 = 0; i2 < insertTasksList.size(); i2++) {
                unscheduledTasks.remove(Integer.valueOf(insertTasksList.get(i2))); // 将插入的任务从未调度的任务列表中移除
                solutionT[insertTasksList.get(i2)] = i; // 将插入的任务的开始执行时间设置为当前时间
                schedulingTasks.add(insertTasksList.get(i2)); // 将插入的任务加入到正在调度的任务列表中
                resourceNow = resourceNow + (double) instance.getTasks().get(insertTasksList.get(i2)).get(3);
            }
            // System.out.println("time now = " + i + " resource_now = " + resourceNow + " scheduling_task_num = " + schedulingTasks.size() + " scheduled_task_num = " + scheduledTasks.size() + " unscheduled_task_num = " + unscheduledTasks.size() + " resource_upper_bound = " + resourceCap);
        }
        if (scheduledTasks.size() != instance.getTasks().size()) {
            solutionT[solutionT.length - 1] = 0;
        } else {
            solutionT[solutionT.length - 1] = 1;
        }

        return solutionT;
    }
}
