package restaurant.test.com.restaurant.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import restaurant.test.com.restaurant.database.DataBaseManager;
import restaurant.test.com.restaurant.pojo.Customer;

public class Util {

    /**
     *  use to parse customer data and store in the database
     * @param dataBaseManager - database instance to store the data
     * @param customerJsonData - Json data of the customer
     */
    public static void parseJsonCustomerData(DataBaseManager dataBaseManager, String customerJsonData) {
        Customer[] customer = new Gson().fromJson(customerJsonData, Customer[].class);
        for (Customer cus : customer) {
            dataBaseManager.insertCustomerInfo(cus.getCustomerFirstName() + " " + cus.getCustomerLastName(), String.valueOf(cus.getId()));
        }
    }

    /**
     * use to parse Table booking data and store in the database
     * @param dataBaseManager -  database instance to store the data
     * @param tableData -  data of the Booking table status
     */
    public static void parseJsonTableData(DataBaseManager dataBaseManager, String tableData) {
        String[] tableDataArray = tableData.substring(1, tableData.length() - 1).split("\\s*,\\s*");
        for (String s : tableDataArray) {
            dataBaseManager.insertBookingInfo(s);
        }
    }

    /**
     *
     * @param context - use to access the current state of the application/object
     * @return - status of internet on your device
     */
    public static boolean isInterNetAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
