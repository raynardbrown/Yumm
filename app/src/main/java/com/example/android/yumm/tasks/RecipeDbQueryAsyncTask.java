package com.example.android.yumm.tasks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.yumm.db.RecipeContract;
import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.model.RecipeIngredient;
import com.example.android.yumm.model.RecipeStep;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RecipeDbQueryAsyncTask extends AsyncTask<Void, Void, RecipeDbQueryAsyncTask.Result>
{
  private WeakReference<Context> context;
  private IAsyncTaskCompleteListener<Result> listener;

  public class Result
  {
    public List<Recipe> recipeList;

    Result(List<Recipe> recipeList)
    {
      this.recipeList = recipeList;
    }
  }

  public RecipeDbQueryAsyncTask(Context context, IAsyncTaskCompleteListener<Result> listener)
  {
    this.context = new WeakReference<>(context);
    this.listener = listener;
  }

  @Override
  protected Result doInBackground(Void... voids)
  {
    try
    {
      Context weakContext = context.get();

      if(weakContext != null)
      {
        // get all recipes
        Cursor recipeCursor = weakContext.getContentResolver().query(RecipeContract.RecipeColumns.CONTENT_URI,
                null,
                null,
                null,
                null);

        if(recipeCursor != null)
        {
          List<Recipe> recipeList = new ArrayList<>();

          // Grab a recipe
          for(;recipeCursor.moveToNext();)
          {
            int recipeId = recipeCursor.getInt(recipeCursor.getColumnIndex(RecipeContract.RecipeColumns._ID));
            String recipeName = recipeCursor.getString(recipeCursor.getColumnIndex(RecipeContract.RecipeColumns.RECIPE_NAME));
            int recipeServings = recipeCursor.getInt(recipeCursor.getColumnIndex(RecipeContract.RecipeColumns.RECIPE_SERVINGS));
            String recipeImage = recipeCursor.getString(recipeCursor.getColumnIndex(RecipeContract.RecipeColumns.RECIPE_IMAGE));

            // grab the ingredients for this recipe
            Uri ingredientsUri = RecipeContract.IngredientColumns.CONTENT_URI;
            ingredientsUri = ingredientsUri.buildUpon().appendPath(Integer.toString(recipeId)).build();

            List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

            Cursor ingredientsCursor = weakContext.getContentResolver().query(ingredientsUri,
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

            // grab the steps for this recipe
            Uri stepsUri = RecipeContract.StepColumns.CONTENT_URI;
            stepsUri = stepsUri.buildUpon().appendPath(Integer.toString(recipeId)).build();

            List<RecipeStep> recipeStepList = new ArrayList<>();

            // Make sure the steps are in order
            final String stepSortOrder = String.format("%s ASC", RecipeContract.StepColumns.STEP_INDEX);

            Cursor stepsCursor = weakContext.getContentResolver().query(stepsUri,
                    null,
                    null,
                    null,
                    stepSortOrder);

            if(stepsCursor != null)
            {
              for(;stepsCursor.moveToNext();)
              {
                int stepId = stepsCursor.getInt(stepsCursor.getColumnIndex(RecipeContract.StepColumns._ID));
                String stepShortDescription = stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepColumns.STEP_SHORT_DESCRIPTION));
                String stepDescription = stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepColumns.STEP_DESCRIPTION));
                String stepVideoUrl = stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepColumns.STEP_VIDEO_URL));
                String stepThumbnailUrl = stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepColumns.STEP_THUMBNAIL_URL));

                recipeStepList.add(new RecipeStep(stepId, stepShortDescription, stepDescription, stepVideoUrl, stepThumbnailUrl));
              }
              // Done with this cursor
              stepsCursor.close();
            }

            recipeList.add(new Recipe(recipeId, recipeName, recipeIngredientList, recipeStepList, recipeServings, recipeImage));

          } // end recipe loop

          // done with this cursor
          recipeCursor.close();
          return new Result(recipeList);
        }
        else
        {
          // recipe cursor is invalid we are done
          return null;
        }
      }
      else
      {
        // invalid context
        return null;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  protected void onPostExecute(Result result)
  {
    super.onPostExecute(result);
    listener.onTaskComplete(result);
  }
}
