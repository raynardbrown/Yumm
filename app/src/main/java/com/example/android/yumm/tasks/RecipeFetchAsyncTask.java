package com.example.android.yumm.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.utils.YummUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class RecipeFetchAsyncTask extends AsyncTask<Void, Void, RecipeFetchAsyncTask.Results>
{
  private WeakReference<Context> context;

  private IAsyncTaskCompleteListener<Results> listener;

  public class Results
  {
    public List<Recipe> recipeList;

    public Results(List<Recipe> recipeList)
    {
      this.recipeList = recipeList;
    }
  }

  public RecipeFetchAsyncTask(Context context, IAsyncTaskCompleteListener<Results> listener)
  {
    this.context = new WeakReference<Context>(context);
    this.listener = listener;
  }

  @Override
  protected Results doInBackground(Void... voids)
  {
    Uri uri;

    Context weakContext = context.get();

    if(weakContext != null)
    {
      uri = YummUtils.getRecipeDataUri(weakContext);
    }
    else
    {
      // context is invalid
      return null;
    }

    List<Recipe> recipeList = null;

    try
    {
      Response response = YummUtils.queryRecipeData(uri);

      if(response.code() == 200)
      {
        ResponseBody responseBody = response.body();

        if(responseBody != null)
        {
          String responseBodyToString = responseBody.string();

          weakContext = context.get();

          if(weakContext != null)
          {
            recipeList = YummUtils.recipeJsonStringToRecipeList(weakContext, responseBodyToString);
          }
          else
          {
            // the context is invalid
            return null;
          }
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return new RecipeFetchAsyncTask.Results(recipeList);
  }

  @Override
  protected void onPostExecute(Results results)
  {
    super.onPostExecute(results);

    listener.onTaskComplete(results);
  }
}
