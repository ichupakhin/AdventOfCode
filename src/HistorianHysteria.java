import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistorianHysteria {


    public static int computeDistance(String filePath) {
      int distance = 0;
      ArrayList<Integer> firstList = new ArrayList<>();
      ArrayList<Integer> secondList = new ArrayList<>();
      try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
          // Process the line
          String[] locationIds = line.split("\\s+");
          firstList.add(Integer.parseInt(locationIds[0]));
          secondList.add(Integer.parseInt(locationIds[1]));
        }
      } catch (IOException e) {
        e.printStackTrace();
        return -1;
      }
      firstList.sort(Integer::compareTo);
      secondList.sort(Integer::compareTo);

      for(int i = 0; i < firstList.size(); i++) {
        distance += Math.abs(firstList.get(i) - secondList.get(i));
      }

      return distance;
    }

  public static int computeDistanceStream(String filePath) {
    ArrayList<Integer> firstList = new ArrayList<>();
    ArrayList<Integer> secondList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      // Read the file and save location ids in two lists
      br.lines()
          .map(line -> line.split("\\s+"))
          .forEach(locationIds -> {
            firstList.add(Integer.parseInt(locationIds[0]));
            secondList.add(Integer.parseInt(locationIds[1]));
          });
      // Sort the lists
      firstList.sort(Integer::compareTo);
      secondList.sort(Integer::compareTo);
      // Compute the distance
      return firstList.stream()
          .mapToInt(firstListValue -> Math.abs(firstListValue - secondList.get(firstList.indexOf(firstListValue))))
          .sum();
    } catch (IOException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public static long computeNumberOfSafeLevels(String filePath) {
    try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      return br.lines()
          .map(line -> line.split("\\s+"))
          .filter(HistorianHysteria::isSafeLevel)
          .count();
    } catch (IOException e) {
      e.printStackTrace();
      return -1;
    }
  }

  private static boolean isSafeLevel(String[] level) {
    int lastDiff = 0;
    for (int i = 1; i < level.length; i++) {
      int currentDiff = Integer.parseInt(level[i]) -  Integer.parseInt(level[i-1]);
      //1 till 3
      if(Math.abs(currentDiff) > 3 || Math.abs(currentDiff) < 1) {
        return false;
      }

      if(lastDiff != 0) { //0 means no previous diff
        //The complete level must be either increasing or decreasing.
        if(lastDiff > 0 && currentDiff < 0
            || lastDiff < 0 && currentDiff > 0) {
          return false;
        }
      }

      lastDiff = currentDiff;
    }
    return true;
  }

  public static long mullItOver(String filePath) {
      long sum = 0;
    try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      return br.lines()
          .map(HistorianHysteria::findSumInLine)
          .reduce(sum, Long::sum);
    } catch (IOException e) {
      e.printStackTrace();
      return -1;
    }
  }

  private static long findSumInLine(String line) {
    long sum = 0;
    Matcher matcher = Pattern.compile("mul\\([1-9]{1,3},[1-9]{1,3}\\)").matcher(line);
    while (matcher.find()) {
      System.out.println(matcher.group());
      sum += Arrays.stream(matcher.group()
          .replace("mul(", "")
          .replace(")", "")
          .split(","))
          .mapToLong(Long::parseLong)
          .reduce(1, (a, b) -> a * b);
    }
    System.out.println("findSumInLine: " + sum);
    return sum;
  }
}
