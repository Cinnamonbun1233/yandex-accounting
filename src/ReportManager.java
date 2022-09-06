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

                    months.dataInMonthLine = new DataInMonthLine(title, isExpense, quantity, price);
                    months.dataInMonth.add(months.dataInMonthLine);
                }
            }
            System.out.println("Месяц: " + monthNumber + " - отчёт обработан!");
            months.mapOfMonths.put(monthNumber, months.dataInMonth);
        }
    }

    public void readYearReport() {
        String content = readFileContentsOrNull("resources/y.2021.csv");
        if (!(content == null)) {
            String[] lines = content.split("\r?\n");
            for (int linesNumber = 1; linesNumber < lines.length; linesNumber++) {
                String line = lines[linesNumber];
                String[] partOfLine = line.split(",");

                int month = Integer.parseInt(partOfLine[0]);
                int amount = Integer.parseInt(partOfLine[1]);
                boolean isExpense = Boolean.parseBoolean(partOfLine[2]);

                year.dataInYearLine = new DataInYearLine(month, amount, isExpense);
                year.dataInYear.add(year.dataInYearLine);
            }
            setExpensesAndIncomePerYear();
            System.out.println("Год: 2021 - отчёт обработан!");
        } else {
            System.out.println("Возникла ошибка, файл не прочитан");
        }
    }

    public void setExpensesAndIncomePerYear() {
        for (DataInYearLine months : year.dataInYear) {
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
            for (DataInMonthLine expenses : months.mapOfMonths.get(monthNumber)) {
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

    public void printMonthlyReport() {
        try {
            double maxIncome = 0;
            double maxExpense = 0;
            double maxExpenseProduct;
            String nameExpenseTitle = null;
            String nameIncomeTitle = null;
            for (Integer monthNumber : months.mapOfMonths.keySet()) {
                System.out.println("Отчёт за месяц №" + monthNumber + ":");
                for (DataInMonthLine value : months.mapOfMonths.get(monthNumber)) {
                    maxExpenseProduct = value.price * value.quantity;
                    if (maxExpenseProduct > maxExpense && value.isExpense) {
                        maxExpense = maxExpenseProduct;
                        nameExpenseTitle = value.title;
                    } else if (maxExpenseProduct > maxIncome && !value.isExpense) {
                        maxIncome = maxExpenseProduct;
                        nameIncomeTitle = value.title;
                    }
                }
                System.out.println("Наибольший доход: " + maxIncome + " руб. Название строки: " + nameIncomeTitle);
                System.out.println("Наибольшая трата: " + maxExpense + " руб. Название строки: " + nameExpenseTitle);
                maxIncome = 0;
                maxExpense = 0;
                nameExpenseTitle = null;
                nameIncomeTitle = null;
            }
        } catch (NullPointerException exception) {
            System.out.println("Не получается вывести статистику по месячным отчётам. Попробуйте прочитать файлы ещё " +
                    "раз");
        }
    }

    public void printStaticYearReport() {
        try {
            yearProfit();
            averageExpensesAndIncomeForYear();
        } catch (NullPointerException exception) {
            System.out.println("Не получается вывести статистику по годовому отчёту. Попробуйте прочитать файл ещё " +
                    "раз");
        }
    }

    public void yearProfit() {
        System.out.println("Данные за 2021 год:");
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
        System.out.println("В среднем за все месяцы в 2021 году получен расход: " + sumExpenses / monthCount + " руб!");
        System.out.println("В среднем за все месяцы в 2021 году получен доход: " + sumIncome / monthCount + " руб!");
    }

    public void compareReports() {
        getExpensesAndIncomePerMonth();
        try {
            for (int monthNumber = 1; monthNumber < 4; monthNumber++) {
                if (months.expensesPerMonth.get(monthNumber) == year.expensesPerMonth.get(monthNumber).amount) {
                    System.out.println("Сверка расходов в " + monthNumber + " месяце успешно завершена");
                } else {
                    System.out.println("В месяце " + monthNumber + " обнаружено несоответствие в расходах!");
                }
                if (months.incomesPerMonth.get(monthNumber) == year.incomesPerMonth.get(monthNumber).amount) {
                    System.out.println("Сверка доходов в " + monthNumber + " месяце успешно завершена");
                } else {
                    System.out.println("В месяце " + monthNumber + " обнаружено несоответствие в доходах!");
                }
            }
        } catch (NullPointerException exception) {
            System.out.println("Невозможно сравнить файлы с отчётами. Возможно, файлы еще не считаны! \n" +
                    "Используя меню считайте файлы еще раз!");
        }
    }

    protected String readFileContentsOrNull(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. Возможно, файл не находится в нужной " +
                    "директории.");
            return null;
        }
    }
}