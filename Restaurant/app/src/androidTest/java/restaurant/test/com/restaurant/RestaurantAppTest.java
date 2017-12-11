package restaurant.test.com.restaurant;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.ListView;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import restaurant.test.com.restaurant.database.DataBaseManager;
import restaurant.test.com.restaurant.pojo.Customer;
import restaurant.test.com.restaurant.utility.Util;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings("deprecation")
@RunWith(AndroidJUnit4.class)
public class RestaurantAppTest extends ActivityInstrumentationTestCase2<CustomerActivity> {
    private ListView list = null;
    private CustomerActivity restaurant;


    public RestaurantAppTest() {
        super(CustomerActivity.class);
    }

    /**
     * inital set-up of the application
     * @throws Exception
     */
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        setActivityInitialTouchMode(false);
        restaurant = getActivity();
        list = (ListView) restaurant.findViewById(R.id.listView);
    }

    /**
     * tear down the test case
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * check the list view present
     * @throws Exception
     */
    @Test
    public void ensureListViewIsPresent() throws Exception {
        Assert.assertNotNull(list);
        Assert.assertNotNull(restaurant);
    }

    /**
     * count the total item of the test case
     */
    @Test
    public void listCount() {
        Assert.assertEquals(21, list.getAdapter().getCount());
    }

    /**
     * key board event test
     */
    @Test
    public void keyEvents() {
        sendKeys("4*DPAD_DOWN");
        Assert.assertEquals(3, list.getSelectedItemPosition());
    }

    /**
     * touch event test
     */
    @Test
    public void touchEvents() {
        TouchUtils.scrollToBottom(this, getActivity(), list);
        getInstrumentation().waitForIdleSync();
        Assert.assertEquals(20, list.getLastVisiblePosition());
    }

    /**
     * test customer data
     */
    @Test
    public void testCustomerData() {
        DataBaseManager dataBaseManager = new DataBaseManager(getActivity());
        dataBaseManager.open();
        dataBaseManager.deleteCustomerData();
        String customerData = readFile("customer-list.json", getActivity());
        Assert.assertNotNull(customerData);
        Customer[] customer = new Gson().fromJson(customerData, Customer[].class);
        Assert.assertNotNull(customer);
        Assert.assertEquals(customer.length, 21);
        Util.parseJsonCustomerData(dataBaseManager, customerData);
        Cursor cursor = dataBaseManager.fetchAllCustomer();
        Assert.assertNotNull(cursor);
        Assert.assertEquals(customer.length, cursor.getCount());
        dataBaseManager.deleteCustomerData();
    }

    /**
     * test booking data
     */
    @Test
    public void testTableBookingData() {
        DataBaseManager dataBaseManager = new DataBaseManager(getActivity());
        dataBaseManager.open();
        dataBaseManager.deleteBookingTable();
        String tableData = readFile("table-map.json", getActivity());
        Assert.assertNotNull(tableData);
        String[] tableDataArray = tableData.substring(1, tableData.length() - 1).split("\\s*,\\s*");
        Assert.assertNotNull(tableDataArray);
        Assert.assertEquals(tableDataArray.length, 70);
        Util.parseJsonTableData(dataBaseManager, tableData);
        Cursor cursor = dataBaseManager.fetchAllBookingTableData();
        Assert.assertNotNull(cursor);
        Assert.assertEquals(70, cursor.getCount());
        dataBaseManager.deleteBookingTable();
    }

    /**
     * check the connectivity of internet
     */
    @Test
    public void testIsNetworkConnected() {
        Assert.assertEquals(Util.isInterNetAvailable(getActivity()), true);
    }

    /**
     *
     * @param fileName - file name of customer and booking data
     * @param context - current state of the application
     * @return - data of customer or table
     */
    public String readFile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }
}


