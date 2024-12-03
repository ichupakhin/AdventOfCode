//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

  public static void main(String[] args) {
    System.out.println("HistorianHysteria: computed distance is " + HistorianHysteria.computeDistance(
        "resources/puzzle_input_day_1_task_1"));
    System.out.println("HistorianHysteria: computed distance is " + HistorianHysteria.computeDistanceStream("resources/puzzle_input_day_1_task_1"));
    System.out.println("HistorianHysteria: computed number of safe levels is " + HistorianHysteria.computeNumberOfSafeLevels("resources/puzzle_input_day_1_task_2"));
    System.out.println("HistorianHysteria: mull it over " + HistorianHysteria.mullItOver("resources/puzzle_input_day_2_task_1"));
  }
}