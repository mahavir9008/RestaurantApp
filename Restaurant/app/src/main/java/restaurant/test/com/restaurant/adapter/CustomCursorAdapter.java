package restaurant.test.com.restaurant.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;

import restaurant.test.com.restaurant.R;
import restaurant.test.com.restaurant.database.DataBaseManager;
import restaurant.test.com.restaurant.utility.Constant;
import restaurant.test.com.restaurant.utility.ViewHolder;

public class CustomCursorAdapter extends CursorAdapter {

    private final DataBaseManager dataBaseManager;
    private final LayoutInflater mInflater;

    /**
     *
     * @param context - use to access the current state of the application/object
     * @param c - use to inflate the data and update the DB
     * @param dataBaseManager - use to st
     */
    @SuppressWarnings("deprecation")
    public CustomCursorAdapter(Context context, Cursor c, DataBaseManager dataBaseManager) {
        super(context, c);
        mInflater = LayoutInflater.from(context);
        this.dataBaseManager = dataBaseManager;
    }

    /**
     *
     * @param view - to inflate in the screen
     * @param context - use to access the current state of the application/object
     * @param cursor - use to inflate the data and update the DB
     */
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.setCheckBox((CheckBox) view.findViewById(R.id.checkbox));
        CheckBox cb = holder.getCheckBox();
        cb.setTag(cursor.getPosition());

        CompoundButton.OnCheckedChangeListener checkedChange = new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues contentValues = new ContentValues();
                Integer currentPosition = (Integer) buttonView.getTag();
                if (cursor.moveToPosition(currentPosition)) {
                    String rowID = cursor.getString(cursor.getColumnIndex(DataBaseManager.KEY_BT_ROW_ID));
                    if (isChecked) {
                        contentValues.put(DataBaseManager.KEY_BT_TABLE_NUMBER, Constant.TABLE_RESERVE);
                        dataBaseManager.getDb().update(DataBaseManager.BOOKING_TABLE, contentValues, "_id = ?",
                                new String[]{rowID});
                    } else {
                        contentValues.put(DataBaseManager.KEY_BT_TABLE_NUMBER, Constant.TABLE_EMPTY);
                        dataBaseManager.getDb().update(DataBaseManager.BOOKING_TABLE, contentValues, "_id = ?",
                                new String[]{rowID});
                    }
                }
            }
        };
        cb.setOnCheckedChangeListener(checkedChange);

        if (cursor.getString(cursor.getColumnIndex(DataBaseManager.KEY_BT_TABLE_NUMBER)).equals(Constant.TABLE_RESERVE)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
    }

    /**
     *
      * @param context - use to access the current state of the application/object
     * @param cursor - use to inflate the data and update the DB
     * @param parent - attached the view
     * @return - return the view
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder;
        View convertView = mInflater.inflate(R.layout.custom_table, parent, false);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);
        return convertView;
    }
}