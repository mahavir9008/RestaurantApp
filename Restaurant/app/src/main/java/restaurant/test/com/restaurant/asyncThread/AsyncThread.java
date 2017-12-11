package restaurant.test.com.restaurant.asyncThread;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import restaurant.test.com.restaurant.intreface.AsyncTaskCompleteListener;


public class AsyncThread extends AsyncTask<String, Void, String> {

    private final AsyncTaskCompleteListener<String> callback;
    private final ProgressDialog pDialog;


    /**
     *
     * @param cb - interface listener use to send the data over activity
     * @param progressBar - to show the progress icon in the screen
     */
    public AsyncThread(AsyncTaskCompleteListener<String> cb,ProgressDialog dialog) {
        this.callback = cb;
        this.pDialog = dialog;
    }

    /**
     * use to show the progress icon in the screen
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.show();

    }

    /**
     *
     * @param strings - pass the URL Value
     * @return - data from the server
     */
    @Override
    protected String doInBackground(String... strings) {
        InputStreamReader isr = null;
        BufferedReader input = null;
        StringBuilder returnString = new StringBuilder();
        try {
            URL url = new URL(strings[0]);
            isr = new InputStreamReader(url.openStream());
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
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    /**
     *
     * @param result - data from the server
     */
    protected void onPostExecute(String result) {
        pDialog.dismiss();
        callback.onTaskComplete(result);
    }
}