import java.util.ArrayList;
import java.util.HashMap;

public class Year {

    YearLine yearLine;
    ArrayList<YearLine> dataInYear = new ArrayList<>();
    HashMap<Integer, Double> profitPerMonth = new HashMap<>();
    HashMap<Integer, YearLine> expensesPerMonth = new HashMap<>();
    HashMap<Integer, YearLine> incomesPerMonth = new HashMap<>();
}
