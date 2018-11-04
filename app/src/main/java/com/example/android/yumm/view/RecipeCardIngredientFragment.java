package com.example.android.yumm.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.yumm.R;
import com.example.android.yumm.databinding.FragmentRecipeCardIngredientsBinding;
import com.example.android.yumm.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeCardIngredientFragment extends Fragment
{
  private List<RecipeIngredient> recipeIngredientList;

  private RecipeCardIngredientAdapter adapter;

  private FragmentRecipeCardIngredientsBinding fragmentRecipeCardIngredientsBinding;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    fragmentRecipeCardIngredientsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_card_ingredients, container, false);

    LinearLayoutManager recipeCardIngredientLayoutManager = new LinearLayoutManager(getContext());

    fragmentRecipeCardIngredientsBinding.rvRecipeCardIngredientFragment.setLayoutManager(recipeCardIngredientLayoutManager);

    // initialize the list
    if(recipeIngredientList == null)
    {
      recipeIngredientList = new ArrayList<>();
    }

    // initialize the adapter
    adapter = new RecipeCardIngredientAdapter(recipeIngredientList);

    // set the recycler view adapter
    fragmentRecipeCardIngredientsBinding.rvRecipeCardIngredientFragment.setAdapter(adapter);

    return fragmentRecipeCardIngredientsBinding.getRoot();
  }

  public void setRecipeIngredientList(List<RecipeIngredient> recipeIngredientList)
  {
    if(this.recipeIngredientList != null)
    {
      this.recipeIngredientList.addAll(recipeIngredientList);
    }
    else
    {
      this.recipeIngredientList = recipeIngredientList;
    }

    if(fragmentRecipeCardIngredientsBinding != null)
    {
      updateUi();
    }
  }

  private void updateUi()
  {
    adapter.notifyDataSetChanged();
  }
}
