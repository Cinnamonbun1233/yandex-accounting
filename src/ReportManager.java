import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ReportManager {

    Months months;
    Year year;

    public ReportManager() {
        months = new Months();
        year = new Year();
    }

    public void readMonthlyReports() {
        System.out.println("Обработка месячных отчётов запущена.");
        for (int monthNumber = 1; monthNumber < 4; monthNumber++) {
            String path = "resources/m.20210" + monthNumber + ".csv";
            String content = readFileContentsOrNull(path);
            months.dataInMonth = new ArrayList<>();
            if (!(content == null)) {
                String[] lines = content.split("\r?\n");
                for (int linesNumber = 1; linesNumber < lines.length; linesNumber++) {
                    String line = lines[linesNumber];
                    String[] partOfLine = line.split(",");

                    String title = partOfLine[0];
                    boolean isExpense = Boolean.parseBoolean(partOfLine[1]);
                    int quantity = Integer.parseInt(partOfLine[2]);
                    int price = Integer.parseInt(partOfLine[3]);

                    months.monthLine = new MonthLine(title, isExpense, quantity, price);
                    months.dataInMonth.add(months.monthLine);
                }
            }
            System.out.println("Месяц: " + monthNumber + ". Отчёт обработан.");
            months.mapOfMonths.put(monthNumber, months.dataInMonth);
        }
    }

    public void readYearReport() {
        System.out.println("Обработка годового отчёта запущена.");
        String content = readFileContentsOrNull("resources/y.2021.csv");
        if (!(content == null)) {
            String[] lines = content.split("\r?\n");
            for (int linesNumber = 1; linesNumber < lines.length; linesNumber++) {
                String line = lines[linesNumber];
                String[] partOfLine = line.split(",");

                int month = Integer.parseInt(partOfLine[0]);
                int amount = Integer.parseInt(partOfLine[1]);
                boolean isExpense = Boolean.parseBoolean(partOfLine[2]);

                year.yearLine = new YearLine(month, amount, isExpense);
                year.dataInYear.add(year.yearLine);
            }
            setExpensesAndIncomePerYear();
            System.out.println("Год: 2021. Отчёт обработан.");
        } else {
            System.out.println("Ошибка, файл не прочитан.");
        }
    }

    public void compareReports() {
        getExpensesAndIncomePerMonth();
        System.out.println("Запущена сверка месячных и годового отчёта.");
        try {
            for (int monthNumber = 1; monthNumber < 4; monthNumber++) {
                if (months.expensesPerMonth.get(monthNumber) == year.expensesPerMonth.get(monthNumber).amount) {
                    System.out.println("Месяц: " + monthNumber + ". Сверка расходов успешно завершена.");
                } else {
                    System.out.println("Месяц: " + monthNumber + ". Сверка расходов завершена с ошибкой.");
                }
                if (months.incomesPerMonth.get(monthNumber) == year.incomesPerMonth.get(monthNumber).amount) {
                    System.out.println("Месяц: " + monthNumber + ". Сверка доходов успешно завершена.");
                } else {
                    System.out.println("Меясц: " + monthNumber + ". Сверка доходов завершена с ошибкой.");
                }
            }
        } catch (NullPointerException exception) {
            System.out.println("Ошибка, ещё не считаны все мясячные или годовой отчёт. Считайте их и попробуйте " +
                    "сделать сверку ещё раз.");
        }
    }

    public void printMonthlyReport() {
        System.out.println("Печать месячных отчётов запущена.");
        try {
            double maxIncome = 0;
            double maxExpense = 0;
            double maxExpenseProduct;
            String nameExpenseTitle = null;
            String nameIncomeTitle = null;
            for (Integer monthNumber : months.mapOfMonths.keySet()) {
                System.out.println("Месяц: " + monthNumber + ":");
                for (MonthLine value : months.mapOfMonths.get(monthNumber)) {
                    maxExpenseProduct = value.price * value.quantity;
                    if (maxExpenseProduct > maxExpense && value.isExpense) {
                        maxExpense = maxExpenseProduct;
                        nameExpenseTitle = value.title;
                    } else if (maxExpenseProduct > maxIncome && !value.isExpense) {
                        maxIncome = maxExpenseProduct;
                        nameIncomeTitle = value.title;
                    }
                }
                System.out.println("Наибольший доход: " + maxIncome + " руб. Название строки: " + nameIncomeTitle +
                        ".");
                System.out.println("Наибольший расход: " + maxExpense + " руб. Название строки: " + nameExpenseTitle +
                        ".");
                maxIncome = 0;
                maxExpense = 0;
                nameExpenseTitle = null;
                nameIncomeTitle = null;
            }
        } catch (NullPointerException exception) {
            System.out.println("Ошибка, не получается вывести месячные отчёты.");
        }
    }

    public void printYearReport() {
        System.out.println("Печать отчёта за 2021 год запущена.");
        try {
            yearProfit();
            averageExpensesAndIncomeForYear();
        } catch (NullPointerException exception) {
            System.out.println("Ошибка, не получается вывести статистику по годовому отчёту.");
        }
    }

    public void setExpensesAndIncomePerYear() {
        for (YearLine months : year.dataInYear) {
            int index = months.month;
            if (months.isExpense) {
                year.expensesPerMonth.put(index, months);
            } else {
                year.incomesPerMonth.put(index, months);
            }
        }
    }

    public void getExpensesAndIncomePerMonth() {
        for (Integer monthNumber : months.mapOfMonths.keySet()) {
            int expense = 0;
            int income = 0;
            for (MonthLine expenses : months.mapOfMonths.get(monthNumber)) {
                if (expenses.isExpense) {
                    expense += expenses.quantity * expenses.price;
                } else {
                    income += expenses.quantity * expenses.price;
                }
            }
            months.expensesPerMonth.put(monthNumber, expense);
            months.incomesPerMonth.put(monthNumber, income);
        }
    }

    public void yearProfit() {
        for (int monthNumber = 1; monthNumber < 4; monthNumber++) {
            double profit = year.incomesPerMonth.get(monthNumber).amount - year.expensesPerMonth.get(monthNumber).amount;
            year.profitPerMonth.put(monthNumber, profit);
        }
        for (int monthNumber = 1; monthNumber < year.profitPerMonth.size() + 1; ++monthNumber) {
            System.out.println("Месяц: " + monthNumber + ". Прибыль: " + year.profitPerMonth.get(monthNumber) + " руб.");
        }
    }

    public void averageExpensesAndIncomeForYear() {
        double sumExpenses = 0;
        double sumIncome = 0;
        int monthCount = 0;
        for (int monthNumber = 1; monthNumber < year.expensesPerMonth.size() + 1; monthNumber++) {
            monthCount++;
            sumExpenses += year.expensesPerMonth.get(monthNumber).amount;
            sumIncome += year.incomesPerMonth.get(monthNumber).amount;
        }
        System.out.println("Средний расход за все месяцы 2021 года: " + sumExpenses / monthCount + " руб.");
        System.out.println("Средний доход за все месяцы 2021 года: " + sumIncome / monthCount + " руб.");
    }

    protected String readFileContentsOrNull(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Ошибка, не полуается прочить файл. Возможно указана неверная директория.");
            return null;
        }
    }
}