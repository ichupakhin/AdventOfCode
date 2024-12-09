import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HistorianHysteriaTest {

  @Test
  void computeDistance() {
    HistorianHysteria.computeDistance("resources/puzzle_input_day_1_task_1");
  }

  @Test
  void computeDistanceStream() {
    HistorianHysteria.computeDistanceStream("resources/puzzle_input_day_1_task_1");
  }

  @Test
  void computeNumberOfSafeLevels() {
    HistorianHysteria.computeNumberOfSafeLevels("resources/puzzle_input_day_1_task_2");
  }

  @Test
  void mullItOver() {
    long computed = HistorianHysteria.mullItOver("resources/puzzle_input_day_2_task_1_test");
    assertEquals(161, computed);
  }

  @Test
  void bridgeRepairCorrectResult() {
    assertEquals(3749,
        HistorianHysteria.bridgeRepair("resources/puzzle_input_day_7_task_1_test", 1));
  }

  @ParameterizedTest
  @CsvSource({"1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024"})
  void bridgeRepairTakesUnderFiveSeconds(int threadNumber) {
    long startTime = System.currentTimeMillis();
    long computedSum = HistorianHysteria.bridgeRepair("resources/puzzle_input_day_7_task_1", threadNumber);
    long endTime = System.currentTimeMillis();
    System.out.println("bridgeRepair using " + threadNumber + " thread(s) has taken " + (endTime - startTime) + "ms. " +
        "Computed sum: " + computedSum);
    assertTrue(endTime - startTime < 5000);
  }
}