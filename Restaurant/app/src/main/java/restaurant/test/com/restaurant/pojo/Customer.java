package restaurant.test.com.restaurant.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {
    @SerializedName("customerFirstName")
    @Expose
    private  String customerFirstName;
    @SerializedName("customerLastName")
    @Expose
    private String customerLastName;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}