package mbodziony.businesscardsmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by MBodziony on 2016-12-30.
 */

public class DatabaseManager {

    public static final String DEBUG_TAG = "BusinessManager";   // for debugging (to find in logs)

    // SQLite database details
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "BusinessCards.db";


    // CREATE TABLE command (for CARDS and MYCARDS tables)
    private static final String CREATE_TABLE_CARDS = "CREATE TABLE cards (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "logoPath TEXT, name TEXT NOT NULL, mobile TEXT NOT NULL, phone TEXT, fax TEXT, email TEXT, web TEXT, company TEXT, " +
            "address TEXT, job TEXT, facebook TEXT, tweeter TEXT, skype TEXT, other TEXT);";
    private static final String CREATE_TABLE_MYCARDS = "CREATE TABLE mycards (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "logoPath TEXT, name TEXT NOT NULL, mobile TEXT NOT NULL, phone TEXT, fax TEXT, email TEXT, web TEXT, company TEXT, " +
            "address TEXT, job TEXT, facebook TEXT, tweeter TEXT, skype TEXT, other TEXT);";

    // DROP TABLE command (for CARDS and MYCARDS tables)
    public static final String DB_DROP_TABLE_CARDS = "DROP TABLE IF EXISTS cards";
    public static final String DB_DROP_TABLE_MYCARDS = "DROP TABLE IF EXISTS mycards";

    // SQLite
    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    // constructor
    public DatabaseManager (Context context){ this.context = context;}

    // open connection to database
    public DatabaseManager open(){
        dbHelper = new DatabaseHelper(context,DB_NAME,null,DB_VERSION);
        try{
            db = dbHelper.getWritableDatabase();
            Toast.makeText(context,"Connection to DB is open (read/write)",Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e){
            db = dbHelper.getReadableDatabase();
            Toast.makeText(context,"Connection to DB is open (read only)",Toast.LENGTH_SHORT).show();
        }
        return this;
    }

    // close connection to database
    public void close(){
        dbHelper.close();
        Toast.makeText(context,"Connection to DB is closed",Toast.LENGTH_SHORT).show();
    }

    // insert new Card into database
    public boolean insertNewCard(String table, Card card){
        ContentValues newCardValues = new ContentValues();
        newCardValues.put("logoPath",card.getLogoImgPath());
        newCardValues.put("name",card.getName());
        newCardValues.put("mobile",card.getMobile());
        newCardValues.put("phone",card.getPhone());
        newCardValues.put("fax",card.getFax());
        newCardValues.put("email",card.getEmail());
        newCardValues.put("web",card.getWeb());
        newCardValues.put("company",card.getCompany());
        newCardValues.put("address",card.getAddress());
        newCardValues.put("job",card.getJob());
        newCardValues.put("facebook",card.getFacebook());
        newCardValues.put("tweeter",card.getTweeter());
        newCardValues.put("skype",card.getSkype());
        newCardValues.put("other",card.getOther());

        return db.insert(table,null,newCardValues) > 0;
    }

    // update Card element in database
    public boolean updateCard(String table, Card card){
        ContentValues updateCardValues = new ContentValues();
        updateCardValues.put("logoPath",card.getLogoImgPath());
        updateCardValues.put("name",card.getName());
        updateCardValues.put("mobile",card.getMobile());
        updateCardValues.put("phone",card.getPhone());
        updateCardValues.put("fax",card.getFax());
        updateCardValues.put("email",card.getEmail());
        updateCardValues.put("web",card.getWeb());
        updateCardValues.put("company",card.getCompany());
        updateCardValues.put("address",card.getAddress());
        updateCardValues.put("job",card.getJob());
        updateCardValues.put("facebook",card.getFacebook());
        updateCardValues.put("tweeter",card.getTweeter());
        updateCardValues.put("skype",card.getSkype());
        updateCardValues.put("other",card.getOther());

        return db.update(table,updateCardValues,"id = " + card.getId(),null) > 0;
    }

    // delete Card element from database
    public boolean deleteCard(String table, long id){

        return db.delete(table,"id = " + id,null) > 0;
    }

    // get all cards from database (from specific table)
    public Cursor getAllCardsFromDB(String table){

        Cursor cardsCursor;
        String [] columns = {"id","logoPath","name","mobile","phone","fax","email","web","company","address","job","facebook","tweeter","skype","other"};
        cardsCursor = db.query(table,columns,null,null,null,null,null);
        return cardsCursor;
    }


    // private class DatabaseHelper - needed for creating and updating database tables
    private static class DatabaseHelper extends SQLiteOpenHelper{

        // constructor
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_CARDS);
            sqLiteDatabase.execSQL(CREATE_TABLE_MYCARDS);
            Log.d(DEBUG_TAG,"Tables CARDS and MYCARDS were created...");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DB_DROP_TABLE_CARDS);
            sqLiteDatabase.execSQL(DB_DROP_TABLE_MYCARDS);
            Log.d(DEBUG_TAG,"Tables CARDS and MYCARDS were deleted. All data is lost!");

            onCreate(sqLiteDatabase);
        }
    }

}
