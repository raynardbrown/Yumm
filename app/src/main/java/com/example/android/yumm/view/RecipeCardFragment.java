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
import com.example.android.yumm.databinding.FragmentRecipeCardBinding;
import com.example.android.yumm.model.Recipe;

import java.util.ArrayList;

public class RecipeCardFragment extends Fragment
{
  public RecipeCardFragment()
  {

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    FragmentRecipeCardBinding fragmentRecipeCardBinding;
    fragmentRecipeCardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_card, container, false);

    LinearLayoutManager recipeCardLayoutManager = new LinearLayoutManager(getContext());

    fragmentRecipeCardBinding.rvRecipeCardFragment.setLayoutManager(recipeCardLayoutManager);

    // Create the adapter
    RecipeCardAdapter recipeCardAdapter = new RecipeCardAdapter(new ArrayList<Recipe>());

    // Register the adapter on the recycler view
    fragmentRecipeCardBinding.rvRecipeCardFragment.setAdapter(recipeCardAdapter);

    return fragmentRecipeCardBinding.getRoot();
  }
}
