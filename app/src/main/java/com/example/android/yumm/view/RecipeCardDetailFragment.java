package com.example.android.yumm.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.yumm.R;
import com.example.android.yumm.databinding.FragmentRecipeCardDetailBinding;
import com.example.android.yumm.model.Recipe;

public class RecipeCardDetailFragment extends Fragment
{
  private Recipe recipe;

  private IRecipeCardDetailListItemClickListener recipeCardDetailListItemClickListener;

  public RecipeCardDetailFragment()
  {

  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);

    try
    {
      recipeCardDetailListItemClickListener = (IRecipeCardDetailListItemClickListener)context;
    }
    catch(ClassCastException e)
    {
      throw new ClassCastException(context.toString() + " must implement IRecipeCardDetailListItemClickListener interface");
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    FragmentRecipeCardDetailBinding fragmentRecipeCardDetailBinding;
    fragmentRecipeCardDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_card_detail, container, false);

    LinearLayoutManager recipeCardDetailLayoutManager = new LinearLayoutManager(getContext());

    fragmentRecipeCardDetailBinding.rvRecipeCardDetailFragment.setLayoutManager(recipeCardDetailLayoutManager);

    // Create the adapter
    RecipeCardDetailAdapter recipeCardDetailAdapter = new RecipeCardDetailAdapter(recipe, recipeCardDetailListItemClickListener);

    // Register the adapter on the recycler view
    fragmentRecipeCardDetailBinding.rvRecipeCardDetailFragment.setAdapter(recipeCardDetailAdapter);

    return fragmentRecipeCardDetailBinding.getRoot();
  }

  public void setRecipe(Recipe recipe)
  {
    this.recipe = recipe;
  }
}
