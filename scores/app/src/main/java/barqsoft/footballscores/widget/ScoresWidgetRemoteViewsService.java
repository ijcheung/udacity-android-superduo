package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;

import barqsoft.footballscores.R;
import barqsoft.footballscores.provider.ScoresContract;
import barqsoft.footballscores.util.Utilities;

/**
 * RemoteViewsService controlling the data being shown in the scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoresWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = ScoresWidgetRemoteViewsService.class.getSimpleName();
    private static final String[] SCORE_COLUMNS = {
            ScoresContract.ScoresEntry.MATCH_ID,
            ScoresContract.ScoresEntry.DATE_COL,
            ScoresContract.ScoresEntry.HOME_COL,
            ScoresContract.ScoresEntry.AWAY_COL,
            ScoresContract.ScoresEntry.HOME_GOALS_COL,
            ScoresContract.ScoresEntry.AWAY_GOALS_COL
    };
    // these indices must match the projection
    public static final int INDEX_ID = 0;
    public static final int INDEX_DATE = 1;
    public static final int INDEX_HOME = 2;
    public static final int INDEX_AWAY = 3;
    public static final int INDEX_HOME_GOALS = 4;
    public static final int INDEX_AWAY_GOALS = 5;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                Uri scoresUri = ScoresContract.ScoresEntry
                        .buildScoreWithDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                data = getContentResolver().query(scoresUri,
                        SCORE_COLUMNS,
                        null,
                        new String[]{dateFormat.format(System.currentTimeMillis())},
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_scores_list_item);

                views.setTextViewText(R.id.home_name, data.getString(INDEX_HOME));
                views.setTextViewText(R.id.away_name, data.getString(INDEX_AWAY));
                views.setTextViewText(R.id.data_textview, data.getString(INDEX_DATE));
                views.setTextViewText(R.id.score_textview, Utilities.getScores(data.getInt(INDEX_HOME_GOALS), data.getInt(INDEX_AWAY_GOALS)));
                views.setImageViewResource(R.id.home_crest, Utilities.getTeamCrestByTeamName(data.getString(INDEX_HOME)));
                views.setImageViewResource(R.id.away_crest, Utilities.getTeamCrestByTeamName(data.getString(INDEX_AWAY)));

                Intent fillInIntent = new Intent();
                views.setOnClickFillInIntent(R.id.scores_list_item, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}

