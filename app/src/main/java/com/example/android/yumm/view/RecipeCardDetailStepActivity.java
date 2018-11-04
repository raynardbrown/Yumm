package com.example.android.yumm.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.android.yumm.R;
import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.model.RecipeStep;
import com.example.android.yumm.utils.YummConstants;
import com.example.android.yumm.utils.YummUtils;

import java.util.List;

public class RecipeCardDetailStepActivity extends AppCompatActivity implements IRecipeCardDetailStepPreviousClickListener,
                                                                               IRecipeCardDetailStepNextClickListener
{
  private Recipe recipe;

  private int recipeStepIndex;

  private RecipeCardDetailStepFragment recipeCardDetailStepFragment;

  private int orientation;

  private int layoutSizeHint;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_recipe_card_detail_step);

    orientation = getResources().getConfiguration().orientation;

    if(YummUtils.isTabletSizedDevice(this))
    {
      layoutSizeHint = YummConstants.TABLET_SIZED_DEVICE;
    }
    else
    {
      layoutSizeHint = YummConstants.PHONE_SIZED_DEVICE;
    }

    if(savedInstanceState == null)
    {
      // Grab the intent before inflating the layout because the fragment needs information from
      // the intent before the fragment is displayed.
      Intent intent = getIntent();

      recipe = intent.getParcelableExtra(getString(R.string.recipe_type_key));

      recipeStepIndex = intent.getIntExtra(getString(R.string.recipe_step_index_key), 0);
    }
    else
    {
      recipe = savedInstanceState.getParcelable(getString(R.string.recipe_type_key));

      recipeStepIndex = savedInstanceState.getInt(getString(R.string.recipe_step_index_key));
    }

    if(getSupportActionBar() != null)
    {
      getSupportActionBar().setTitle(recipe.getName());
    }

    hideSystemUiIfLandscape();

    FragmentManager fragmentManager = getSupportFragmentManager();

    RecipeCardDetailStepFragment fragment = (RecipeCardDetailStepFragment)fragmentManager.findFragmentById(R.id.recipe_card_detail_step_fragment);

    if(fragment == null)
    {
      recipeCardDetailStepFragment = new RecipeCardDetailStepFragment();

      initFragmentState();

      fragmentManager.beginTransaction()
              .add(R.id.recipe_card_detail_step_fragment, recipeCardDetailStepFragment)
              .commit();
    }
    else
    {
      recipeCardDetailStepFragment = fragment;
    }

    setButtonState();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);

    outState.putParcelable(getString(R.string.recipe_type_key), recipe);

    outState.putInt(getString(R.string.recipe_step_index_key), recipeStepIndex);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus)
  {
    if(hasFocus)
    {
      hideSystemUiIfLandscape();
    }
  }

  @Override
  public void onPreviousButtonClick()
  {
    recipeStepIndex = recipeStepIndex - 1;

    setButtonState();

    RecipeStep recipeStep = recipe.getRecipeSteps().get(recipeStepIndex);

    recipeCardDetailStepFragment.setRecipeStep(recipeStep);
  }

  @Override
  public void onNextButtonClick()
  {
    recipeStepIndex = recipeStepIndex + 1;

    setButtonState();

    RecipeStep recipeStep = recipe.getRecipeSteps().get(recipeStepIndex);

    recipeCardDetailStepFragment.setRecipeStep(recipeStep);
  }

  private void setButtonState()
  {
    if(layoutSizeHint == YummConstants.PHONE_SIZED_DEVICE)
    {
      if(recipeStepIndex == 0)
      {
        recipeCardDetailStepFragment.setPreviousButtonEnabled(false);
      }
      else
      {
        recipeCardDetailStepFragment.setPreviousButtonEnabled(true);
      }

      if(recipeStepIndex == (recipe.getRecipeSteps().size() - 1))
      {
        recipeCardDetailStepFragment.setNextButtonEnabled(false);
      }
      else
      {
        recipeCardDetailStepFragment.setNextButtonEnabled(true);
      }
    }
  }

  private void initFragmentState()
  {
    recipeCardDetailStepFragment.setOrientation(orientation);

    RecipeStep recipeStep = recipe.getRecipeSteps().get(recipeStepIndex);

    recipeCardDetailStepFragment.setRecipeStep(recipeStep);
  }

  private void hideSystemUiIfLandscape()
  {
    List<RecipeStep> recipeStepList = recipe.getRecipeSteps();

    String videoUrl = recipeStepList.size() > 0 ?  recipeStepList.get(recipeStepIndex).getVideoUrl() : null;
    String imageUrl = recipeStepList.size() > 0 ?  recipeStepList.get(recipeStepIndex).getThumbnailUrl() : null;

    final boolean videoAvailable = videoUrl != null && videoUrl.length() > 0;
    final boolean imageAvailable = imageUrl != null && imageUrl.length() > 0;

    if(orientation == Configuration.ORIENTATION_LANDSCAPE &&
       layoutSizeHint == YummConstants.PHONE_SIZED_DEVICE &&
       (videoAvailable || imageAvailable))
    {
      View decorView = getWindow().getDecorView();

      if(Build.VERSION.SDK_INT < 16)
      {
        // Hide the nav bar and status bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
      }
      else
      {
        // hide the navigation bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        if(getSupportActionBar() != null)
        {
          getSupportActionBar().hide();
        }
      }
    }
  }
}
