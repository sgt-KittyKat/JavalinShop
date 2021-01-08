package database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable
public class Order {
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Customer customer;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Product product;
    @DatabaseField
    private String date;
    @DatabaseField
    private String address;
    @DatabaseField
    private Integer quantity;
    @DatabaseField
    private Integer price;
    @DatabaseField
    private Status status;
    public Order() {

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void saveChanges(Order updated) {
        if (updated.customer != null && !updated.customer.equals(this.customer)) {
            this.customer = updated.customer;
        }
        if (updated.product != null && !updated.product.equals(this.product)) {
            this.product = updated.product;
        }
        if (updated.date != null && !updated.date.equals(this.date)) {
            this.date = updated.date;
        }
        if (updated.address != null && !updated.address.equals(this.address)) {
            this.address = updated.address;
        }
        if (updated.quantity != null && !updated.quantity.equals(this.quantity)) {
            this.quantity = updated.quantity;
        }
        if (updated.price != null && !updated.price.equals(this.price)) {
            this.price = updated.price;
        }
        if (updated.status != null && !updated.status.equals(this.status)) {
            this.status = updated.status;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(customer, order.customer) &&
                Objects.equals(product, order.product) &&
                Objects.equals(date, order.date) &&
                Objects.equals(address, order.address) &&
                Objects.equals(quantity, order.quantity) &&
                Objects.equals(price, order.price) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, product, date, address, quantity, price, status);
    }
}
