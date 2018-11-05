package com.example.android.yumm.view;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.yumm.R;
import com.example.android.yumm.idling.SimpleIdlingResource;
import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.receivers.INetworkBroadcastReceiverListener;
import com.example.android.yumm.receivers.NetworkBroadcastReceiver;
import com.example.android.yumm.tasks.IAsyncTaskCompleteListener;
import com.example.android.yumm.tasks.RecipeDbInsertAsyncTask;
import com.example.android.yumm.tasks.RecipeDbQueryAsyncTask;
import com.example.android.yumm.tasks.RecipeFetchAsyncTask;
import com.example.android.yumm.utils.YummConstants;
import com.example.android.yumm.utils.YummUtils;
import com.example.android.yumm.widgets.YummWidgetProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IRecipeCardListItemClickListener,
                                                               INetworkBroadcastReceiverListener
{
  /**
   * A hint that allows layouts to react according to this value.
   *
   * 1. phone sized device
   *
   * 2. tablet sized device.
   */
  private int layoutSizeHint;

  private RecipeCardFragment recipeCardFragment;

  private NetworkBroadcastReceiver networkBroadcastReceiver;

  /**
   * Flag that specifies whether the device has a valid connection to a network.
   */
  private boolean hasNetworkConnectivity;

  private List<Recipe> recipeList;

  @Nullable
  private SimpleIdlingResource idlingResource;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    // Check to see if we are working with a tablet sized device
    if(YummUtils.isTabletSizedDevice(this))
    {
      // tablet sized device
      layoutSizeHint = YummConstants.TABLET_SIZED_DEVICE;
    }
    else
    {
      // phone sized device
      layoutSizeHint = YummConstants.PHONE_SIZED_DEVICE;
    }

    setContentView(R.layout.activity_main);

    launchBroadCastReceiverIfNotRegistered();

    dispatchRecipeTaskIfNetworkAvailable();
  }

  @Override
  public void onAttachFragment(Fragment fragment)
  {
    super.onAttachFragment(fragment);

    // I don't have to surround this in a try catch block because the static fragment,
    // RecipeCardFragment will be the only fragment ever associated with this activity.
    recipeCardFragment = (RecipeCardFragment)fragment;

    recipeCardFragment.setLayoutSizeHint(layoutSizeHint);
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();

    if(networkBroadcastReceiver != null)
    {
      unregisterReceiver(networkBroadcastReceiver);
    }
  }

  private void launchBroadCastReceiverIfNotRegistered()
  {
    if(networkBroadcastReceiver == null)
    {
      IntentFilter networkIntentFilter = new IntentFilter();
      networkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

      networkBroadcastReceiver = new NetworkBroadcastReceiver(this);

      registerReceiver(networkBroadcastReceiver, networkIntentFilter);
    }
  }

  @Override
  public void onRecipeCardListItemClick(Recipe recipe)
  {
    Intent intent = new Intent(MainActivity.this, RecipeCardDetailActivity.class);

    intent.putExtra(getString(R.string.recipe_type_key), recipe);

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(getString(R.string.selected_recipe_for_widget_key), recipe.getId());
    editor.apply();

    // Let the widget know to get the new ingredients
    updateAppWidget(this);

    intent.putExtra(getString(R.string.layout_size_hint_key), layoutSizeHint);

    startActivity(intent);
  }

  @Override
  public void onNetworkConnected()
  {
    hasNetworkConnectivity = true;

    if(recipeCardFragment.getRecipeList() == null ||
            recipeCardFragment.getRecipeList().isEmpty())
    {
      showRecipeListViewAndClearError();

      dispatchRecipeTaskIfNetworkAvailable();
    }
  }

  @Override
  public void onNetworkDisconnected()
  {
    hasNetworkConnectivity = false;

    // Hide the list and show a network error if the list is empty
    if(recipeCardFragment.getRecipeList() == null ||
            recipeCardFragment.getRecipeList().isEmpty())
    {
      hideRecipeListViewAndDisplayError();
    }
  }

  private void dispatchRecipeTaskIfNetworkAvailable()
  {
    if(hasNetworkConnectivity)
    {
      if(idlingResource != null)
      {
        // we are not idle
        idlingResource.setIdleState(false);
      }

      new RecipeFetchAsyncTask(this, new RecipeListAsyncTaskCompleteListener()).execute();
    }
  }

  private void hideRecipeListViewAndDisplayError()
  {
    recipeCardFragment.hideListAndShowError();
  }

  private void showRecipeListViewAndClearError()
  {
    recipeCardFragment.showListAndClearError();
  }

  @VisibleForTesting
  @NonNull
  public IdlingResource getIdlingResource()
  {
    if(idlingResource == null)
    {
      idlingResource = new SimpleIdlingResource();
    }
    return idlingResource;
  }

  public class RecipeListAsyncTaskCompleteListener implements IAsyncTaskCompleteListener<RecipeFetchAsyncTask.Results>
  {
    @Override
    public void onTaskComplete(RecipeFetchAsyncTask.Results result)
    {
      if(result != null)
      {
        if(result.recipeList != null)
        {
          // Don't display the list until the database has been updated/checked
          // TODO: Probably should have a loading message
          MainActivity.this.recipeList = result.recipeList;

           // kick off our database query task
          new RecipeDbQueryAsyncTask(MainActivity.this, new RecipeDbQueryAsyncTaskCompleteListener()).execute();
        }
      }
    }
  }

  private void updateAppWidget(Context context)
  {
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, YummWidgetProvider.class));

    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gv_recipe_widget);

    YummWidgetProvider.updateYummWidgets(context, appWidgetManager, appWidgetIds);
  }

  public class RecipeDbQueryAsyncTaskCompleteListener implements IAsyncTaskCompleteListener<RecipeDbQueryAsyncTask.Result>
  {
    @Override
    public void onTaskComplete(RecipeDbQueryAsyncTask.Result result)
    {
      if(result != null && result.recipeList.size() > 0)
      {
        // Compare the list from the database with our list from the json, if they differ, update the database.
        if(isRecipeListEqual(MainActivity.this.recipeList, result.recipeList))
        {
          // We have valid results, and the database is sync'd, populate the fragment
          recipeCardFragment.setRecipeList(MainActivity.this.recipeList);

          // Update the widget too
          updateAppWidget(MainActivity.this);
        }
        else
        {
          // TODO: Update the database since it doesn't match the json
          recipeCardFragment.setRecipeList(MainActivity.this.recipeList);
        }

        // we are idle
        if(idlingResource != null)
        {
          idlingResource.setIdleState(true);
        }
      }
      else
      {
        // Database error or the database is empty, run the insert db task using our list
        new RecipeDbInsertAsyncTask(MainActivity.this, new RecipeDbInsertAsyncTaskCompleteListener()).execute(recipeList.toArray(new Recipe[recipeList.size()]));
      }
    }
  }

  private boolean isRecipeListEqual(List<Recipe> jsonRecipeList, List<Recipe> dbRecipeList)
  {
    if(jsonRecipeList.size() != dbRecipeList.size())
    {
      return false;
    }

    // check recipes
    for(int i = 0; i < jsonRecipeList.size(); ++i)
    {
      Recipe jsonRecipe = jsonRecipeList.get(i);
      Recipe dbRecipe = dbRecipeList.get(i);

      if(!jsonRecipe.equals(dbRecipe))
      {
        return false;
      }
    }

    return true;
  }

  public class RecipeDbInsertAsyncTaskCompleteListener implements IAsyncTaskCompleteListener<RecipeDbInsertAsyncTask.Result>
  {
    @Override
    public void onTaskComplete(RecipeDbInsertAsyncTask.Result result)
    {
      if(result != null)
      {
        if(result.success)
        {
          // Update the widget too
          updateAppWidget(MainActivity.this);
        }
      }

      // We have valid results, populate the fragment
      recipeCardFragment.setRecipeList(MainActivity.this.recipeList);

      // we are idle
      if(idlingResource != null)
      {
        idlingResource.setIdleState(true);
      }
    }
  }
}
