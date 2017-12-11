package restaurant.test.com.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import restaurant.test.com.restaurant.asyncThread.AsyncThread;
import restaurant.test.com.restaurant.database.DataBaseManager;
import restaurant.test.com.restaurant.intreface.AsyncTaskCompleteListener;
import restaurant.test.com.restaurant.utility.Constant;
import restaurant.test.com.restaurant.utility.Util;

public class CustomerActivity extends AppCompatActivity implements AsyncTaskCompleteListener {

    private DataBaseManager dataBaseManager;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        dataBaseManager = new DataBaseManager(this);
        dataBaseManager.open();
        checkApplicationStatus();
        displayCustomerView();
        filterName();
    }

    /**
     * fetch the customer data from the database and display on the listView
     */
    private void displayCustomerView() {
        Cursor cursor = dataBaseManager.fetchAllCustomer();
        // The desired columns to be bound
        String[] columns = new String[]{
                DataBaseManager.KEY_CUSTOMER_NAME,
        };
        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.textCustomerName,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.customer_info,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {

                Intent tableActivity = new Intent(CustomerActivity.this, BookingActivity.class);
                startActivity(tableActivity);

            }
        });
    }

    @Override
    public void onTaskComplete(Object result) {
        Util.parseJsonCustomerData(dataBaseManager, result.toString());
        displayCustomerView();

    }

    /**
     * Checked the application status by following way -:
     * No Database and NO Internet  -  Application closed with the message "Internet Not available".
     * NO Database but Internet - Application fetch the data from the server .
     * Database but NO internet - Application showed the data from the database.
     */
    private void checkApplicationStatus() {
        Cursor cursor = dataBaseManager.fetchAllCustomer();
        if (!Util.isInterNetAvailable(this) && cursor.getCount() == 0) {
            Toast.makeText(this, Constant.INTERNET_STATUS, Toast.LENGTH_LONG).show();
            finish();
        } else if (Util.isInterNetAvailable(this) && cursor.getCount() == 0) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(Constant.LOADING);
            AsyncThread asyncThread = new AsyncThread(this, progressDialog);
            asyncThread.execute(Constant.CUSTOMER_LIST);
        } else {
            displayCustomerView();
        }
    }

    /**
     * Search from the Customer Table according to filter
     */
    private void filterName() {
        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dataBaseManager.fetchByName(constraint.toString());
            }
        });

    }

}
