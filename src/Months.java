import java.util.ArrayList;
import java.util.HashMap;

public class Months {

    DataInMonthLine dataInMonthLine;
    ArrayList<DataInMonthLine> dataInMonth = new ArrayList<>();
    HashMap<Integer, ArrayList<DataInMonthLine>> mapOfMonths = new HashMap<>();
    HashMap<Integer, Integer> expensesPerMonth = new HashMap<>();
    HashMap<Integer, Integer> incomesPerMonth = new HashMap<>();
}