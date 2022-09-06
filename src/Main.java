import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ReportManager reportManager = new ReportManager();
        System.out.println("Добро пожаловать.");

        while (true) {

            printMenu();
            int command = scanner.nextInt();

            switch (command) {
                case 1 -> reportManager.readMonthlyReports();
                case 2 -> reportManager.readYearReport();
                case 3 -> reportManager.compareReports();
                case 4 -> reportManager.printMonthlyReport();
                case 5 -> reportManager.printStaticYearReport();
                case 0 -> {
                    System.out.println("До свидания.");
                    return;
                }
                default -> System.out.println("Ошибка, такой команды не существует.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("Что Вы хотите сделать:");
        System.out.println("1 - Считать все месячные отчёты;");
        System.out.println("2 - Считать годовой отчёт;");
        System.out.println("3 - Сверить отчёты;");
        System.out.println("4 - Вывести информацию о всех месячных отчётах;");
        System.out.println("5 - Вывести информацию о годовом отчёте;");
        System.out.println("0 - Выход из программы.");
    }
}