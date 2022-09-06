public class DataInMonthLine {

    String title;
    boolean isExpense;
    int quantity;
    int price;

    public DataInMonthLine(String title, boolean isExpense, int quantity, int price) {
        this.title = title;
        this.isExpense = isExpense;
        this.quantity = quantity;
        this.price = price;
    }
}