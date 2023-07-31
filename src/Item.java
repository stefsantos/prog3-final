public class Item {
    private String name;
    private int maxStock;
    private int stock;
    private int calories;
    private double price;

    public Item(String name, int maxStock, int calories, double price) {
        this.name = name;
        this.maxStock = maxStock;
        this.stock = 0;
        this.calories = calories;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCalories() {
        return calories;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
