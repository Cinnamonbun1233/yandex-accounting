import java.util.ArrayList;
import java.util.HashMap;

public class Months {

    MonthLine monthLine;
    ArrayList<MonthLine> dataInMonth = new ArrayList<>();
    HashMap<Integer, ArrayList<MonthLine>> mapOfMonths = new HashMap<>();
    HashMap<Integer, Integer> expensesPerMonth = new HashMap<>();
    HashMap<Integer, Integer> incomesPerMonth = new HashMap<>();
}