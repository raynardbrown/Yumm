package com.example.android.yumm.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.yumm.R;
import com.example.android.yumm.model.Recipe;

public class RecipeCardIngredientActivity extends AppCompatActivity
{
  private Recipe recipe;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();

    recipe = intent.getParcelableExtra(getString(R.string.recipe_type_key));

    setContentView(R.layout.activity_recipe_card_ingredient);

    if(getSupportActionBar() != null)
    {
      getSupportActionBar().setTitle(recipe.getName());
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public void onAttachFragment(Fragment fragment)
  {
    super.onAttachFragment(fragment);

    RecipeCardIngredientFragment recipeCardIngredientFragment = (RecipeCardIngredientFragment)fragment;

    recipeCardIngredientFragment.setRecipeIngredientList(recipe.getRecipeIngredients());
  }
}
