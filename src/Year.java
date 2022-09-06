import java.util.ArrayList;
import java.util.HashMap;

public class Year {

    DataInYearLine dataInYearLine;
    ArrayList<DataInYearLine> dataInYear = new ArrayList<>();
    HashMap<Integer, Double> profitPerMonth = new HashMap<>();
    HashMap<Integer, DataInYearLine> expensesPerMonth = new HashMap<>();
    HashMap<Integer, DataInYearLine> incomesPerMonth = new HashMap<>();
}
