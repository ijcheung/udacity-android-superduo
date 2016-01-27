package barqsoft.footballscores.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresDBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 2;
    public ScoresDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CreateScoresTable = "CREATE TABLE " + ScoresContract.SCORES_TABLE + " ("
                + ScoresContract.ScoresEntry._ID + " INTEGER PRIMARY KEY,"
                + ScoresContract.ScoresEntry.DATE_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresEntry.TIME_COL + " INTEGER NOT NULL,"
                + ScoresContract.ScoresEntry.HOME_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresEntry.AWAY_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresEntry.LEAGUE_COL + " INTEGER NOT NULL,"
                + ScoresContract.ScoresEntry.HOME_GOALS_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresEntry.AWAY_GOALS_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresEntry.MATCH_ID + " INTEGER NOT NULL,"
                + ScoresContract.ScoresEntry.MATCH_DAY + " INTEGER NOT NULL,"
                + " UNIQUE ("+ ScoresContract.ScoresEntry.MATCH_ID+") ON CONFLICT REPLACE"
                + " );";
        db.execSQL(CreateScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + ScoresContract.SCORES_TABLE);
    }
}
