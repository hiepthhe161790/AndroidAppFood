package vn.edu.fpt.fooddelivery.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import vn.edu.fpt.fooddelivery.model.Food;
import vn.edu.fpt.fooddelivery.model.Order;
import vn.edu.fpt.fooddelivery.model.OrderItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodDelivery.db";
    private static final int DATABASE_VERSION = 2; // Increase version number to trigger onUpgrade

    // Orders table
    private static final String TABLE_ORDERS = "orders";
    private static final String COL_ORDER_ID = "order_id";
    private static final String COL_USER_NAME = "user_name";
    private static final String COL_USER_ADDRESS = "user_address";
    private static final String COL_USER_PHONE = "user_phone";
    private static final String COL_PAYMENT_METHOD = "payment_method";
    private static final String COL_NOTE = "note";
    private static final String COL_TOTAL_AMOUNT = "total_amount";
    private static final String COL_ORDER_DATE = "order_date"; // Added date column

    // Order items table
    private static final String TABLE_ORDER_ITEMS = "order_items";
    private static final String COL_ITEM_TITLE = "item_title";
    private static final String COL_ITEM_FEE = "item_fee";
    private static final String COL_ITEM_QUANTITY = "item_quantity";
    private static final String COL_ITEM_ORDER_ID = "item_order_id"; // Foreign key to orders table

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create orders table
        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_ADDRESS + " TEXT, " +
                COL_USER_PHONE + " TEXT, " +
                COL_PAYMENT_METHOD + " TEXT, " +
                COL_NOTE + " TEXT, " +
                COL_TOTAL_AMOUNT + " REAL, " +
                COL_ORDER_DATE + " TEXT" +
                ")";
        db.execSQL(createOrdersTable);

        // Create order items table
        String createOrderItemsTable = "CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
                COL_ITEM_ORDER_ID + " INTEGER, " +
                COL_ITEM_TITLE + " TEXT, " +
                COL_ITEM_FEE + " REAL, " +
                COL_ITEM_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + COL_ITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COL_ORDER_ID + ")" +
                ")";
        db.execSQL(createOrderItemsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) { // Check if upgrading from version 1 to 2
            // Alter the orders table to add the order_date column if it doesn't exist
            db.execSQL("ALTER TABLE " + TABLE_ORDERS + " ADD COLUMN " + COL_ORDER_DATE + " TEXT");
        }
    }

    public long insertOrder(String userName, String userAddress, String userPhone, String paymentMethod, String note, double totalAmount, ArrayList<Food> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        long orderId;
        try {
            // Insert order into orders table
            ContentValues orderValues = new ContentValues();
            orderValues.put(COL_USER_NAME, userName);
            orderValues.put(COL_USER_ADDRESS, userAddress);
            orderValues.put(COL_USER_PHONE, userPhone);
            orderValues.put(COL_PAYMENT_METHOD, paymentMethod);
            orderValues.put(COL_NOTE, note);
            orderValues.put(COL_TOTAL_AMOUNT, totalAmount);

            // Get current date
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            orderValues.put(COL_ORDER_DATE, currentDate);

            orderId = db.insert(TABLE_ORDERS, null, orderValues);

            // Insert each cart item into order_items table
            for (Food item : cartItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(COL_ITEM_ORDER_ID, orderId);
                itemValues.put(COL_ITEM_TITLE, item.getTitle());
                itemValues.put(COL_ITEM_FEE, item.getFee());
                itemValues.put(COL_ITEM_QUANTITY, item.getNumberInCart());
                db.insert(TABLE_ORDER_ITEMS, null, itemValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return orderId;
    }

    public List<OrderItem> getOrderItems(String orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COL_ITEM_TITLE + ", " + COL_ITEM_FEE + ", " + COL_ITEM_QUANTITY +
                " FROM " + TABLE_ORDER_ITEMS +
                " WHERE " + COL_ITEM_ORDER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{orderId});

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_TITLE));
                double fee = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ITEM_FEE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ITEM_QUANTITY));

                orderItems.add(new OrderItem(title, fee, quantity));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderItems;
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COL_ORDER_ID + ", " + COL_USER_NAME + ", " + COL_ORDER_DATE + ", " + COL_TOTAL_AMOUNT +
                " FROM " + TABLE_ORDERS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String orderId = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_DATE));
                String userName = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME));
                double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TOTAL_AMOUNT));

                // Fetch items for this order
                List<OrderItem> items = getOrderItems(orderId);

                orders.add(new Order(orderId, userName, date, items, totalAmount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }
    public Order getOrderDetails(String orderId) {
//        if (orderId == null) {
//            throw new IllegalArgumentException("Order ID is null");
//        }

        SQLiteDatabase db = this.getReadableDatabase();
        Order order = null;

        String query = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COL_ORDER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{orderId});

        if (cursor.moveToFirst()) {
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME));
            String userAddress = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ADDRESS));
            String userPhone = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PHONE));
            String paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAYMENT_METHOD));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE));
            double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TOTAL_AMOUNT));
            String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_DATE));

            order = new Order(orderId, userName, userAddress, userPhone, paymentMethod, note, totalAmount, orderDate);
        }
        cursor.close();
        return order;
    }
}
