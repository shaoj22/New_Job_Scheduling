package Application;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadData {
    // read csv file to two-dimensional array
    public static List<List<Integer>> readCSVToArray(String filePath, String delimiter) {
        String[][] dataArray = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int numRows = 0;
            int numColumns = 0;

            // 计算行数和列数
            String line;
            while ((line = br.readLine()) != null) {
                numRows++;
                String[] data = line.split(delimiter);
                numColumns = Math.max(numColumns, data.length);
            }

            // 创建二维数组
            dataArray = new String[numRows][numColumns];

            // 重新读取文件以填充二维数组
            br.close(); // 已经在此处关闭BufferedReader

            BufferedReader newBr = new BufferedReader(new FileReader(filePath));
            int row = 0;
            while ((line = newBr.readLine()) != null) {
                String[] data = line.split(delimiter);
                for (int col = 0; col < data.length; col++) {
                    dataArray[row][col] = data[col];
                }
                row++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int numRows = dataArray.length;
        int numCols = dataArray[0].length;

        int[][] intArray = new int[numRows-1][numCols];

        for (int i = 0; i < numRows - 1; i++) {
            for (int j = 0; j < numCols; j++) {
                double doubleValue = Double.parseDouble(dataArray[i+1][j]);
                intArray[i][j] = (int) doubleValue;
            }
        }
        List<List<Integer>> resultList = new ArrayList<>();
        for (int[] row : intArray) {
            List<Integer> list = new ArrayList<>();
            for (int num : row) {
                list.add(num);
            }
            resultList.add(list);
        }

        return resultList;
    }

    // transform two-dimensional array to HashMap
    public static Map<Integer, String[]> arrayToHashMap(String[][] twoDimArray) {
        Map<Integer, String[]> hashMap = new HashMap<>();

        for (int i = 1; i < twoDimArray.length; i++) {
            hashMap.put(i - 1, twoDimArray[i]);
        }

        return hashMap;
    }

    // read csv file to List
    public static List<List<Object>> readCSVToList(String filePath) {
        List<List<Object>> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // 跳过第一行
                }

                String[] data = line.split(",");

                int intValue = Integer.parseInt(data[0]);
                String stringValue = data[1];
                int int3Value = Integer.parseInt(data[2]);
                double doubleValue = Double.parseDouble(data[3]);
                int int5Value = Integer.parseInt(data[4]);
                int int6Value = Integer.parseInt(data[5]);

                List<Object> rowData = new ArrayList<>();
                rowData.add(intValue);
                rowData.add(stringValue);
                rowData.add(int3Value);
                rowData.add(doubleValue);
                rowData.add(int5Value);
                rowData.add(int6Value);

                dataList.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    public static List<List<Integer>> readCSVAndConvertToList(String csvFilePath) {
        List<List<Integer>> taskDependencies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean skipFirstLine = true; // 跳过第一行（编号行）

            while ((line = reader.readLine()) != null) {
                if (skipFirstLine) {
                    skipFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                List<Integer> dependencies = new ArrayList<>();
                for (String value : values) {
                    if (!value.isEmpty()) {
                        // 转换为整数
                        dependencies.add((int) Double.parseDouble(value));
                    }
                }

                taskDependencies.add(dependencies);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return taskDependencies;
    }
}
