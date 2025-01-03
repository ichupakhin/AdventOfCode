import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
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
    try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      return findSumInLine(br
          .lines()
          .reduce("", (a, b) -> a + b));
    } catch (IOException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public static long bridgeRepair(String filePath, int threadNumber) {
    AtomicLong sum = new AtomicLong(0);
    try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      List<String> lines = new ArrayList<>();
      br.lines().forEach(lines::add);
      List<Thread> threads = getThreads(threadNumber, lines, sum);
      for (Thread thread : threads) {
          thread.join();
      }
    } catch (IOException e) {
      e.printStackTrace();
      return -1;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return sum.get();
  }

  private static List<Thread> getThreads(int threadNumber, List<String> lines, AtomicLong sum) {
    int linesPerThread = lines.size() / threadNumber;
    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < threadNumber; i++) {
      int start = i * linesPerThread;
      int end = (i + 1) * linesPerThread;
      if (i == threadNumber - 1) {
        end = lines.size();
      }
      int finalEnd = end;
      Thread thread = new Thread(() -> {
        for (int j = start; j < finalEnd; j++) {
          sum.addAndGet(testEquation(lines.get(j)));
        }
      });
      threads.add(thread);
      thread.start();
    }
    return threads;
  }

  private static long testEquation(String equation) {
    if (equation == null || equation.isEmpty()) {
      return 0;
    }
    String[] splitOnColon = equation.split(":");
    if(splitOnColon.length != 2) {
      return 0;
    }
    long expectedResult = Long.parseLong(splitOnColon[0]);
    int[] numbersToTest = Arrays.stream(splitOnColon[1].trim()
        .split(" "))
        .mapToInt(Integer::parseInt).toArray();
    if(numbersToTest.length < 2) {
      return 0;
    }
    int numberOfPossibleOperatorCombinations = (int) Math.pow(2, numbersToTest.length - 1);
    return testEquationRecursive(numbersToTest, expectedResult, numberOfPossibleOperatorCombinations, 1);
  }

  private static long testEquationRecursive(int[] numbersToTest,
                                           long expectedResult,
                                           int numberOfPossibleOperatorCombinations,
                                           int currentOperatorCombination) {
      if(numberOfPossibleOperatorCombinations < currentOperatorCombination) {
        return 0;
      }
      else {
        long computedResult = numbersToTest[0];
        for (int i = 1; i < numbersToTest.length; i++) {
          if((currentOperatorCombination & (1 << (i - 1))) != 0) {
            computedResult += numbersToTest[i];
          }
          else {
            computedResult *= numbersToTest[i];
          }
        }
        if(computedResult == expectedResult) {
          return expectedResult;
        }
        else return testEquationRecursive(numbersToTest, expectedResult, numberOfPossibleOperatorCombinations,
            currentOperatorCombination + 1);
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
