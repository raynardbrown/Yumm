package com.example.android.yumm.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.yumm.R;
import com.example.android.yumm.db.RecipeContract;
import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.model.RecipeIngredient;
import com.example.android.yumm.model.RecipeStep;

import java.util.ArrayList;
import java.util.List;

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

    cursor = context.getContentResolver().query(RecipeContract.RecipeColumns.CONTENT_URI,
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
    if(cursor != null && cursor.getCount() > 0)
    {
      if(cursor.moveToPosition(position))
      {
        int recipeId = cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeColumns._ID));
        String recipeName = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeColumns.RECIPE_NAME));
        int recipeServings = cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeColumns.RECIPE_SERVINGS));
        String recipeImage = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeColumns.RECIPE_IMAGE));

        // grab the ingredients for this recipe
        Uri ingredientsUri = RecipeContract.IngredientColumns.CONTENT_URI;
        ingredientsUri = ingredientsUri.buildUpon().appendPath(Integer.toString(recipeId)).build();

        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

        Cursor ingredientsCursor = context.getContentResolver().query(ingredientsUri,
                null,
                null,
                null,
                null);

        if(ingredientsCursor != null)
        {
          for(;ingredientsCursor.moveToNext();)
          {
            String ingredientName = ingredientsCursor.getString(ingredientsCursor.getColumnIndex(RecipeContract.IngredientColumns.INGREDIENT_NAME));
            double ingredientQuantity = ingredientsCursor.getDouble(ingredientsCursor.getColumnIndex(RecipeContract.IngredientColumns.INGREDIENT_QUANTITY));
            String ingredientMeasure = ingredientsCursor.getString(ingredientsCursor.getColumnIndex(RecipeContract.IngredientColumns.INGREDIENT_MEASURE));

            recipeIngredientList.add(new RecipeIngredient(ingredientQuantity, ingredientMeasure, ingredientName));
          }

          // Done with this cursor
          ingredientsCursor.close();
        }

        // Don't worry about grabbing the steps, the ingredients activity does not need the steps

        Recipe recipe = new Recipe(recipeId, recipeName, recipeIngredientList, new ArrayList<RecipeStep>(), recipeServings, recipeImage);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yumm_widget_recipe_list_item);

        views.setTextViewText(R.id.tv_widget_recipe_card_item_title, recipeName);

        // Set the pending intent template for the recipe
        Bundle bundle = new Bundle();
        bundle.putParcelable(context.getString(R.string.recipe_type_key), recipe);

        Intent intent = new Intent();
        intent.putExtras(bundle);

        views.setOnClickFillInIntent(R.id.tv_widget_recipe_card_item_title, intent);

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
