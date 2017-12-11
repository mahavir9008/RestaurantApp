package restaurant.test.com.restaurant;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import restaurant.test.com.restaurant.adapter.CustomCursorAdapter;
import restaurant.test.com.restaurant.asyncThread.AsyncThread;
import restaurant.test.com.restaurant.database.DataBaseManager;
import restaurant.test.com.restaurant.intreface.AsyncTaskCompleteListener;
import restaurant.test.com.restaurant.utility.Constant;
import restaurant.test.com.restaurant.utility.Util;

public class BookingActivity extends AppCompatActivity implements AsyncTaskCompleteListener {

    private DataBaseManager dataBaseManager;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        inflateView();
        dataBaseManager = new DataBaseManager(this);
        dataBaseManager.open();
        checkApplicationStatus();
        reservationRemove();
    }

    /**
     *  to get the updated values from database
     on changing the check of checkbox
     * @param view - use to access the current state of the application/object
     */
    @SuppressWarnings("deprecation")
    public void clickHandler(View view) {
        if (view.getId() == R.id.checkbox) {
            cursor.requery();
        }
    }

    /**
     * show the reserved and un-reserved status on the gridView
     */
    @SuppressWarnings("deprecation")
    private void displayCustomerView() {
        cursor = dataBaseManager.fetchAllBookingTableData();
        startManagingCursor(cursor);
        CustomCursorAdapter myCursorAdapter = new CustomCursorAdapter(this, cursor, dataBaseManager);
        GridView gridView = (GridView) findViewById(R.id.tableList);
        gridView.setAdapter(myCursorAdapter);
    }

    @Override
    public void onTaskComplete(Object result) {
        Util.parseJsonTableData(dataBaseManager, result.toString());
        displayCustomerView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Checked the application status by following way -:
     * No Database and NO Internet  -  Application closed with the message "Internet Not available".
     * NO Database but Internet - Application fetch the data from the server .
     * Database but NO internet - Application showed the data from the database.
     */
    private void checkApplicationStatus() {
        Cursor cursorCheck = dataBaseManager.fetchAllBookingTableData();
        if (!Util.isInterNetAvailable(this) && cursorCheck.getCount() == 0) {
            Toast.makeText(this, Constant.INTERNET_STATUS, Toast.LENGTH_LONG).show();
        } else if (Util.isInterNetAvailable(this) && cursorCheck.getCount() == 0) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(Constant.LOADING);
            AsyncThread asyncThread = new AsyncThread(this, progressDialog);
            asyncThread.execute(Constant.TABLE_LIST);
        } else {
            displayCustomerView();
        }
    }

    /**
     * inflate the view
     */
    private void inflateView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    /**
     * reservation remove in every 15 minute .
     */
    private void reservationRemove() {
        ScheduledExecutorService scheduler = Executors
                .newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBaseManager.KEY_BT_TABLE_NUMBER, Constant.TABLE_EMPTY);
                dataBaseManager.getDb().update(DataBaseManager.BOOKING_TABLE, contentValues, null, null);
                runOnUiThread(new Runnable() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void run() {
                        cursor.requery();
                    }
                });
            }
        }, Constant.TIME_TO_EMPTY_TABLE, Constant.TIME_TO_EMPTY_TABLE, TimeUnit.MINUTES);
    }
}
