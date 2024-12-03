import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
}