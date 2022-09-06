public class MonthLine {

    String title;
    boolean isExpense;
    int quantity;
    int price;

    public MonthLine(String title, boolean isExpense, int quantity, int price) {
        this.title = title;
        this.isExpense = isExpense;
        this.quantity = quantity;
        this.price = price;
    }
}