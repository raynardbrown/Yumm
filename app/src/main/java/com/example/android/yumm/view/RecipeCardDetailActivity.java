package com.example.android.yumm.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.yumm.R;
import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.model.RecipeStep;
import com.example.android.yumm.utils.YummConstants;
import com.example.android.yumm.utils.YummUtils;

public class RecipeCardDetailActivity extends AppCompatActivity implements IRecipeCardDetailListItemClickListener
{
  private Recipe recipe;

  private int layoutSizeHint;

  /**
   * The current position of the master list.
   */
  private int masterListIndex;

  private RecipeCardDetailStepFragment recipeCardDetailStepFragment;
  private RecipeCardIngredientFragment recipeCardIngredientFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    if(savedInstanceState != null)
    {
      recipe = savedInstanceState.getParcelable(getString(R.string.recipe_type_key));

      masterListIndex = savedInstanceState.getInt(getString(R.string.master_list_index_key));

      if(getSupportActionBar() != null)
      {
        getSupportActionBar().setTitle(recipe.getName());
      }

      setContentView(R.layout.activity_recipe_card_detail);

      if(YummUtils.isTabletSizedDevice(this))
      {
        // tablet layout

        layoutSizeHint = YummConstants.TABLET_SIZED_DEVICE;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        if(masterListIndex == 0)
        {
          // Init the ingredients fragment
          registerIngredientFragment();
        }
        else
        {
          // Init the step fragment
          registerRecipeStepFragment();

          updateRecipeStepFragment();
        }
      }
      else
      {
        // phone layout
        layoutSizeHint = YummConstants.PHONE_SIZED_DEVICE;
      }
    }
    else
    {
      // Grab the intent before inflating the layout because the fragment needs information from
      // the intent before the fragment is displayed.
      Intent intent = getIntent();

      recipe = intent.getParcelableExtra(getString(R.string.recipe_type_key));

      // The master list starts off at index 0.
      masterListIndex = 0;

      layoutSizeHint = intent.getIntExtra(getString(R.string.layout_size_hint_key),
              YummConstants.PHONE_SIZED_DEVICE);  // Default value should never get happen

      if(getSupportActionBar() != null)
      {
        getSupportActionBar().setTitle(recipe.getName());
      }

      setContentView(R.layout.activity_recipe_card_detail);

      if(layoutSizeHint == YummConstants.TABLET_SIZED_DEVICE)
      {
        // tablet layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        registerIngredientFragment();
      }
    }
  }

  private void registerIngredientFragment()
  {
    ingredientFragmentHelper(true);
  }

  private void replaceCurrentFragmentWithIngredientFragment()
  {
    ingredientFragmentHelper(false);
  }

  private void ingredientFragmentHelper(boolean flag)
  {
    // create the fragment (detail flow)
    FragmentManager fragmentManager = getSupportFragmentManager();

    recipeCardIngredientFragment = new RecipeCardIngredientFragment();
    recipeCardIngredientFragment.setRecipeIngredientList(recipe.getRecipeIngredients());

    if(flag)
    {
      fragmentManager.beginTransaction()
              .add(R.id.detail_step_flow_fragment, recipeCardIngredientFragment)
              .commit();
    }
    else
    {
      fragmentManager.beginTransaction()
              .replace(R.id.detail_step_flow_fragment, recipeCardIngredientFragment)
              .commit();
    }

    recipeCardDetailStepFragment = null;
  }

  private void registerRecipeStepFragment()
  {
    recipeStepFragmentHelper(true);
  }

  private void replaceCurrentFragmentWithRecipeStepFragment()
  {
    recipeStepFragmentHelper(false);
  }

  private void recipeStepFragmentHelper(boolean flag)
  {
    FragmentManager fragmentManager = getSupportFragmentManager();

    recipeCardDetailStepFragment = new RecipeCardDetailStepFragment();

    recipeCardDetailStepFragment.setOrientation(Configuration.ORIENTATION_LANDSCAPE);

    recipeCardDetailStepFragment.setLayoutSizeHint(layoutSizeHint);

    if(flag)
    {
      fragmentManager.beginTransaction()
              .add(R.id.detail_step_flow_fragment, recipeCardDetailStepFragment)
              .commit();
    }
    else
    {
      fragmentManager.beginTransaction()
              .replace(R.id.detail_step_flow_fragment, recipeCardDetailStepFragment)
              .commit();
    }

    recipeCardIngredientFragment = null;
  }

  private void updateRecipeStepFragment()
  {
    RecipeStep recipeStep = recipe.getRecipeSteps().get(masterListIndex - 1);
    recipeCardDetailStepFragment.setRecipeStep(recipeStep);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);

    outState.putParcelable(getString(R.string.recipe_type_key), recipe);

    // Don't worry about saving the layout size hint because the orientation could have changed

    outState.putInt(getString(R.string.master_list_index_key), masterListIndex);
  }

  @Override
  public void onAttachFragment(android.support.v4.app.Fragment fragment)
  {
    super.onAttachFragment(fragment);

    try
    {
      // master list
      RecipeCardDetailFragment recipeCardDetailFragment = (RecipeCardDetailFragment) fragment;

      recipeCardDetailFragment.setRecipe(recipe);

    }
    catch (ClassCastException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void onRecipeCardDetailListItemClick(Recipe recipe, int clickIndex)
  {
    masterListIndex = clickIndex;

    if(layoutSizeHint == YummConstants.TABLET_SIZED_DEVICE)
    {
      // We are in the tablet layout
      if(masterListIndex == 0)
      {
        // The ingredients was clicked

        // Initialize the ingredients fragment (if it is not already initialized) and populate it
        if(recipeCardIngredientFragment == null)
        {
          replaceCurrentFragmentWithIngredientFragment();
        }
      }
      else
      {
        // A step was clicked

        // Initialize the step fragment (if it is not already initialized) and populate it
        if(recipeCardDetailStepFragment == null)
        {
          replaceCurrentFragmentWithRecipeStepFragment();
        }

        updateRecipeStepFragment();
      }
    }
    else
    {
      // We are in the phone layout
      if(masterListIndex == 0)
      {
        // Start the ingredients activity
        Intent intent = new Intent(RecipeCardDetailActivity.this, RecipeCardIngredientActivity.class);

        intent.putExtra(getString(R.string.recipe_type_key), recipe);

        startActivity(intent);
      }
      else
      {
        // Start the step activity
        Intent intent = new Intent(RecipeCardDetailActivity.this, RecipeCardDetailStepActivity.class);

        intent.putExtra(getString(R.string.recipe_type_key), recipe);

        intent.putExtra(getString(R.string.recipe_step_index_key), masterListIndex - 1);

        startActivity(intent);
      }
    }
  }
}
