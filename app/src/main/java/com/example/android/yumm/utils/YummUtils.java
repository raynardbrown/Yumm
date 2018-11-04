package com.example.android.yumm.utils;

import android.content.Context;
import android.net.Uri;

import com.example.android.yumm.R;
import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.model.RecipeIngredient;
import com.example.android.yumm.model.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YummUtils
{

  public static Uri getRecipeDataUri(Context context)
  {
    return Uri.parse(context.getString(R.string.recipe_data_url)).buildUpon()
            .build();
  }

  public static Response queryRecipeData(Uri queryUri) throws IOException
  {
    OkHttpClient okHttpClient = new OkHttpClient();

    Request request = new Request.Builder()
            .url(queryUri.toString())
            .build();

    return okHttpClient.newCall(request).execute();
  }

  public static List<Recipe> recipeJsonStringToRecipeList(Context context, String json)
  {
    try
    {
      // root item is an array
      JSONArray rootArray = new JSONArray(json);

      List<Recipe> recipeList = new ArrayList<>();

      for(int i = 0; i < rootArray.length(); ++i)
      {
        JSONObject recipeJsonObject = rootArray.getJSONObject(i);

        int id = recipeJsonObject.getInt(context.getString(R.string.json_recipe_id_string));

        String recipeName = recipeJsonObject.getString(context.getString(R.string.json_recipe_name_string));

        int servings = recipeJsonObject.getInt(context.getString(R.string.json_recipe_servings_string));

        String recipeImagePath = recipeJsonObject.getString(context.getString(R.string.json_recipe_image_string));

        // grab the list of ingredients.

        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

        JSONArray ingredientsJsonArray = recipeJsonObject.getJSONArray(context.getString(R.string.json_recipe_ingredients_string));

        for(int j = 0; j < ingredientsJsonArray.length(); ++j)
        {
          JSONObject ingredientJsonObject = ingredientsJsonArray.getJSONObject(j);

          double quantity = ingredientJsonObject.getDouble(context.getString(R.string.json_recipe_ingredients_quantity_string));

          String ingredientMeasure = ingredientJsonObject.getString(context.getString(R.string.json_recipe_ingredients_measure_string));

          String ingredient = ingredientJsonObject.getString(context.getString(R.string.json_recipe_ingredients_ingredient_name_string));

          RecipeIngredient recipeIngredient = new RecipeIngredient(quantity, ingredientMeasure, ingredient);

          recipeIngredientList.add(recipeIngredient);
        }

        // grab the list of steps.

        List<RecipeStep> recipeStepList = new ArrayList<>();

        JSONArray recipeStepsJsonArray = recipeJsonObject.getJSONArray(context.getString(R.string.json_recipe_steps_string));

        for(int k = 0; k < recipeStepsJsonArray.length(); ++k)
        {
          JSONObject stepJsonObject = recipeStepsJsonArray.getJSONObject(k);

          int stepId = stepJsonObject.getInt(context.getString(R.string.json_recipe_steps_id_string));

          String shortDescription = stepJsonObject.getString(context.getString(R.string.json_recipe_steps_short_description_string));

          String description = stepJsonObject.getString(context.getString(R.string.json_recipe_steps_description_string));

          String videoURL = stepJsonObject.getString(context.getString(R.string.json_recipe_steps_video_url_string));

          String thumbnailURL = stepJsonObject.getString(context.getString(R.string.json_recipe_steps_thumbnail_url_string));

          RecipeStep recipeStep = new RecipeStep(stepId, shortDescription, description, videoURL, thumbnailURL);

          recipeStepList.add(recipeStep);
        }

        Recipe recipe = new Recipe(id, recipeName, recipeIngredientList, recipeStepList, servings, recipeImagePath);

        recipeList.add(recipe);
      }

      return recipeList;
    }
    catch(JSONException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Check whether the specified context is associated with a device that is the size of a tablet
   * or larger.
   *
   * @param context the Context to check.
   *
   * @return true if the specified Context is associated with a tablet sized device.
   */
  public static boolean isTabletSizedDevice(Context context)
  {
    return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
  }
}
