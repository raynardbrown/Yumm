package com.example.android.yumm.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.yumm.db.RecipeContract;
import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.model.RecipeIngredient;
import com.example.android.yumm.model.RecipeStep;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecipeDbInsertAsyncTask extends AsyncTask<Recipe, Void, RecipeDbInsertAsyncTask.Result>
{
  private WeakReference<Context> context;
  private IAsyncTaskCompleteListener<RecipeDbInsertAsyncTask.Result> listener;

  public class Result
  {
    /**
     * True if all of the recipes from the list were added successfully.
     */
    public boolean success;

    public Result(boolean success)
    {
      this.success = success;
    }
  }

  public RecipeDbInsertAsyncTask(Context context, IAsyncTaskCompleteListener<RecipeDbInsertAsyncTask.Result> listener)
  {
    this.context = new WeakReference<Context>(context);
    this.listener = listener;
  }

  private boolean insertRecipesIntoDatabase(Context weakContext, Recipe... recipes)
  {
    // Add all the recipes

    for(Recipe recipe : recipes)
    {
      ContentValues contentValues = new ContentValues();

      contentValues.put(RecipeContract.RecipeColumns._ID, recipe.getId());
      contentValues.put(RecipeContract.RecipeColumns.RECIPE_NAME, recipe.getName());
      contentValues.put(RecipeContract.RecipeColumns.RECIPE_SERVINGS, recipe.getServings());
      contentValues.put(RecipeContract.RecipeColumns.RECIPE_IMAGE, recipe.getImagePath());


      if(weakContext.getContentResolver().insert(RecipeContract.RecipeColumns.CONTENT_URI, contentValues) == null)
      {
        // error adding this recipe to the database insert task is a failure
        return false;
      }

      // Insert ingredients

      List<RecipeIngredient> recipeIngredientList = recipe.getRecipeIngredients();

      for(int k = 0; k < recipeIngredientList.size(); ++k)
      {
        ContentValues ingredientContentValues = new ContentValues();

        RecipeIngredient recipeIngredient = recipeIngredientList.get(k);

        ingredientContentValues.put(RecipeContract.IngredientColumns.INGREDIENT_NAME, recipeIngredient.getName());
        ingredientContentValues.put(RecipeContract.IngredientColumns.INGREDIENT_QUANTITY, recipeIngredient.getQuantity());
        ingredientContentValues.put(RecipeContract.IngredientColumns.INGREDIENT_MEASURE, recipeIngredient.getMeasure());
        ingredientContentValues.put(RecipeContract.IngredientColumns.RECIPE_FOREIGN_KEY, recipe.getId());

        if(weakContext.getContentResolver().insert(RecipeContract.IngredientColumns.CONTENT_URI, ingredientContentValues) == null)
        {
          // error adding the ingredient list for this recipe to the database
          return false;
        }
      }

      // Insert steps

      List<RecipeStep> recipeStepList = recipe.getRecipeSteps();

      for(int k = 0; k < recipeStepList.size(); ++k)
      {
        ContentValues stepContentValues = new ContentValues();

        RecipeStep recipeStep = recipeStepList.get(k);

        stepContentValues.put(RecipeContract.StepColumns._ID, recipeStep.getId());
        stepContentValues.put(RecipeContract.StepColumns.STEP_INDEX, k);
        stepContentValues.put(RecipeContract.StepColumns.STEP_SHORT_DESCRIPTION, recipeStep.getShortDescription());
        stepContentValues.put(RecipeContract.StepColumns.STEP_DESCRIPTION, recipeStep.getDescription());
        stepContentValues.put(RecipeContract.StepColumns.STEP_VIDEO_URL, recipeStep.getVideoUrl());
        stepContentValues.put(RecipeContract.StepColumns.STEP_THUMBNAIL_URL, recipeStep.getThumbnailUrl());
        stepContentValues.put(RecipeContract.StepColumns.RECIPE_FOREIGN_KEY, recipe.getId());

        if(weakContext.getContentResolver().insert(RecipeContract.StepColumns.CONTENT_URI, stepContentValues) == null)
        {
          // error adding the step list for this recipe to the database
          return false;
        }
      }
    }

    return true;
  }

  @Override
  protected Result doInBackground(Recipe... recipes)
  {
    try
    {
      Context weakContext = context.get();

      if(weakContext != null)
      {
        return new Result(insertRecipesIntoDatabase(weakContext, recipes));
      }
      else
      {
        // context is invalid
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
