import java.util.*;
class User{
    private int id;
    private String name;
    private String mail;

    public User(int id,String name , String mail){
        this.id=id;
        this.name=name;
        this.mail=mail;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }
}
class Admin extends User{
    private String password;
    Admin(int id,String name , String mail,String password){
        super(id,name,mail);
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public void addProduct(ProductCatalog catelog, Product product){
        catelog.addProduct(product);
    }
}
class Customer extends User {
    private Cart cart;

    Customer(int id, String name, String mail) {
        super(id, name, mail);
        this.cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }

    public Order checkout(int orderId) {
        Order order = new Order(orderId, this);

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            double price = product.getPrice();

            OrderItem orderItem =
                    new OrderItem(product, quantity, price);

            order.addItem(orderItem);
        }

        return order;
    }
}
class OrderItem {
    private Product product;
    private int quantity;
    private double price;

    public OrderItem(Product product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
    public double getTotal() {
        return price * quantity;
    }
}
class Order {
    private int orderId;
    private Customer customer;
    private List<OrderItem> items = new ArrayList<>();

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double calculateTotal() {
        double total = 0;

        for (OrderItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }

        return total;
    }

    public void displayOrder() {
        System.out.println(" ORDER ");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Email: " + customer.getMail());

        System.out.println("\nItems:");

        for (OrderItem item : items) {
            System.out.println(item.getProduct().getName()
                            + " | Quantity: " + item.getQuantity()
                            + " | Price: " + item.getPrice()
                            + " | Total: " + (item.getPrice() * item.getQuantity()));
        }

        System.out.println("\nOrder Total: " + calculateTotal());
    }
}
class ProductCatalog{
    List<Product> products = new ArrayList<>();
    public void addProduct(Product product){
        products.add(product);
    }
    public Product findProduct(int id) {
            for (Product product : products) {
                if (product.getId() == id) {
                    return product;
                }
            }
            return null;
    }

    public void displayProduct(){
            for (Product product : products) {
                System.out.println("ID: " + product.getId()
                                + " Name: " + product.getName()
                                + " Price: " + product.getPrice()
                                + " Stock: " + product.getStock());
            }
        }
}
class Product {
    private int id;
    private String name;
    private int stock;
    private double price;

    Product(int id, String name, int stock, double price) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getName() {
        return name;
    }

    public int reduceStock(int quant) {

        if (quant <= 0) {
            System.out.println("Invalid quantity");
            return stock;
        }

        if (stock == 0) {
            System.out.println("Item out of stock");
            return stock;
        }

        if (quant > stock) {
            System.out.println("Insufficient stock");
            return stock;
        }

        stock -= quant;
        return stock;
    }
    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            return;
        }

        stock += quantity;
    }
}
class CartItem{
    private int quantity;
    private Product product;

    CartItem(int quantity,Product product){
        this.product=product;
        this.quantity=quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
    public void displayInfo(){
        System.out.println("name is "+getProduct().getName()+" in stock "+getProduct().getStock()+" id is "+getProduct().getId()+"of quantity " + getQuantity());
    }
    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

}
class Cart{
    List<CartItem> items= new ArrayList<>();
    public void addProduct(Product product, int quantity) {

        if (quantity <= 0) {
            System.out.println("Invalid quantity");
            return;
        }
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                if (quantity > product.getStock()) {
                    System.out.println(
                            "Unable to add. Current stock: " + product.getStock()
                    );
                    return;
                }
                item.increaseQuantity(quantity);
                product.reduceStock(quantity);

                return;
            }
        }
        if (quantity > product.getStock()) {
            System.out.println(
                    "Unable to add. Current stock: " + product.getStock()
            );
            return;
        }

        CartItem newItem = new CartItem(quantity, product);
        items.add(newItem);

        product.reduceStock(quantity);
    }
    public void removeProduct(int prodId){
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getId() == prodId) {
                items.get(i).getProduct().increaseStock(items.get(i).getQuantity());
                items.remove(i);

                return;
            }
        }
        System.out.println("item do no exist");
    }
    public void updateQuantity(int id, int newQuantity) {

        if (newQuantity <= 0) {
            System.out.println("Invalid quantity");
            return;
        }
        for (CartItem item : items) {
            if (item.getProduct().getId() == id) {
                int oldQuantity = item.getQuantity();
                int difference = newQuantity - oldQuantity;
                if (difference > 0) {

                    if (difference > item.getProduct().getStock()) {
                        System.out.println(
                                "Unable to update. Current stock: "
                                        + item.getProduct().getStock()
                        );
                        return;
                    }
                    item.getProduct().reduceStock(difference);
                }
                else if (difference < 0) {

                    int returnedStock = -difference;

                    item.getProduct().increaseStock(returnedStock);
                }
                item.updateQuantity(newQuantity);
                return;
            }
        }

        System.out.println("Invalid id");
    }
    public double calculateTotal(){
        double totalAmount=0;
        if(items.size()==0)return 0;
        for(CartItem item:items){
            totalAmount+=item.getProduct().getPrice()*item.getQuantity();
        }
        return totalAmount;
    }

    public void displayCart(){
        for(var item:items){
            item.displayInfo();
        }
    }
    public List<CartItem> getItems() {
        return items;
    }
}
public class Main{
    static void adminVerify(Admin admin,String password){

        if(password.equals(admin.getPassword())){
            System.out.println("Welcome Boss");
            return;
        }
        else{
            System.out.println("Access denied !! Wrong password");
        }
    }
    static void adminWork(Admin admin, Scanner sc, ProductCatalog catalog) {

            while (true) { System.out.println("\nADMIN MENU");
            System.out.println("1. Add Product");
            System.out.println("2. View Catalog");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter product id: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter product name: ");
            String name = sc.nextLine();
            System.out.print("Enter stock: ");
            int stock = sc.nextInt();
            System.out.print("Enter price: ");
            double price = sc.nextDouble();
            Product product = new Product(id, name, stock, price);
            admin.addProduct(catalog, product);
            System.out.println("Product added successfully!");
            break;
            case 2:
                catalog.displayProduct();
                break;
                case 0:
                    return;
                    default:
                        System.out.println("Invalid choice!");
        }
        }
    }
        static void customerWork(Customer customer, ProductCatalog catalog, Scanner sc) {
            Order lastOrder = null;
            int orderId = 1001;
            while (true) {
                System.out.println("\nCUSTOMER MENU ");
                System.out.println("1. View Catalog");
                System.out.println("2. Add Product to Cart");
                System.out.println("3. View Cart");
                System.out.println("4. Update Cart Quantity");
                System.out.println("5. Remove Product from Cart");
                System.out.println("6. Checkout");
                System.out.println("7. Display Last Order");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.println("\n PRODUCT CATALOG ");
                        catalog.displayProduct();
                        break;
                    case 2:
                        System.out.print("Enter product ID: ");
                        int productId = sc.nextInt();
                        Product product = catalog.findProduct(productId);
                        if (product == null) {
                            System.out.println("Product not found!");
                            break;
                        }
                        System.out.println("Product: " + product.getName() + " | Price: " + product.getPrice() + " | Stock: " + product.getStock());
                        System.out.print("Enter quantity: ");
                        int quantity = sc.nextInt();
                        customer.getCart().addProduct(product, quantity);
                        break;
                    case 3:
                        System.out.println("\nYOUR CART ");
                        customer.getCart().displayCart();
                        System.out.println("Cart Total: " + customer.getCart().calculateTotal());
                        break;
                    case 4:
                        System.out.print("Enter product ID: ");
                        int updateId = sc.nextInt();
                        System.out.print("Enter new quantity: ");
                        int newQuantity = sc.nextInt();
                        customer.getCart().updateQuantity(updateId, newQuantity);
                        break;
                    case 5:
                        System.out.print("Enter product ID to remove: ");
                        int removeId = sc.nextInt();
                        customer.getCart().removeProduct(removeId);
                        break;
                    case 6:
                        if (customer.getCart().getItems().isEmpty()) {
                            System.out.println("Your cart is empty!");
                        } else {
                            lastOrder = customer.checkout(orderId++);
                            System.out.println("\nCheckout successful!");
                            lastOrder.displayOrder();
                        }
                        break;
                    case 7:
                        if (lastOrder == null) {
                            System.out.println("No order placed yet.");
                        } else {
                            lastOrder.displayOrder();
                        }
                        break;
                    case 0:
                        System.out.println("Thank you for shopping!");
                        return;
                        default:
                        System.out.println("Invalid choice!");
                }
            }
        }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Admin admin = new Admin(1, "Admin", "admin@gmail.com","hello1");
        Customer customer = new Customer(2, "Sunny", "sunny@gmail.com");
        ProductCatalog catalog = new ProductCatalog();
        System.out.println("Welcome to Amma Zone");
        boolean flag = true;
        while(flag){
            System.out.println("Press 1 if your a Admin ");
            System.out.println("Press 2 if you are a Customer ");
            System.out.println("Press 0 to exit this app ");
            int choice =sc.nextInt();
            switch (choice){
                case 1:
                    System.out.print("Enter password: ");
                    String password = sc.next();
                    if (password.equals(admin.getPassword())) {
                        System.out.println("Welcome Boss");
                        adminWork(admin, sc, catalog);
                    }
                    else {
                        System.out.println("Access denied !! Wrong password");
                    }
                    break;
                case 2:
                    customerWork(customer, catalog, sc);
                    break;
                case 0:
                    System.out.println("Thank You for chosing US ");
                    flag=false;
                    break;
            }
        }
}
}
