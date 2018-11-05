package com.example.android.yumm.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.yumm.R;
import com.example.android.yumm.db.RecipeContract;

public class YummWidgetGridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
  private Context context;
  private Cursor cursor;

  public YummWidgetGridRemoteViewsFactory(Context applicationContext)
  {
    this.context = applicationContext;
  }

  @Override
  public void onCreate()
  {

  }

  @Override
  public void onDataSetChanged()
  {
    if(cursor != null)
    {
      cursor.close();
    }

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    int recipeId = sharedPreferences.getInt(context.getString(R.string.selected_recipe_for_widget_key), 1);

    Uri ingredientsUri = RecipeContract.IngredientColumns.CONTENT_URI;
    ingredientsUri = ingredientsUri.buildUpon().appendPath(Integer.toString(recipeId)).build();

    cursor = context.getContentResolver().query(ingredientsUri,
            null,
            null,
            null,
            null);
  }

  @Override
  public void onDestroy()
  {
    cursor.close();
  }

  @Override
  public int getCount()
  {
    if(cursor == null)
    {
      return 0;
    }
    else
    {
      return cursor.getCount();
    }
  }

  @Override
  public RemoteViews getViewAt(int position)
  {
    if(cursor != null)
    {
      if(cursor.moveToPosition(position))
      {
        String ingredientName = cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientColumns.INGREDIENT_NAME));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yumm_widget_recipe_list_item);

        views.setTextViewText(R.id.tv_widget_recipe_card_ingredient_item_name, ingredientName);

        return views;
      }
    }

    return null;
  }

  @Override
  public RemoteViews getLoadingView()
  {
    return null;
  }

  @Override
  public int getViewTypeCount()
  {
    return 1;
  }

  @Override
  public long getItemId(int position)
  {
    return position;
  }

  @Override
  public boolean hasStableIds()
  {
    return true;
  }
}
