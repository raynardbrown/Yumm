package com.example.android.yumm.view;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.yumm.R;
import com.example.android.yumm.databinding.FragmentRecipeCardBinding;
import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.utils.YummConstants;

import java.util.ArrayList;
import java.util.List;

public class RecipeCardFragment extends Fragment
{
  private IRecipeCardListItemClickListener recipeCardListItemClickListener;

  private int layoutSizeHint;

  private List<Recipe> recipeList;

  private FragmentRecipeCardBinding fragmentRecipeCardBinding;

  private RecipeCardAdapter recipeCardAdapter;

  public RecipeCardFragment()
  {

  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);

    try
    {
      recipeCardListItemClickListener = (IRecipeCardListItemClickListener)context;
    }
    catch(ClassCastException e)
    {
      throw new ClassCastException(context.toString() + " must implement IRecipeCardListItemClickListener interface");
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    fragmentRecipeCardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_card, container, false);

    LinearLayoutManager recipeCardLayoutManager;

    if(layoutSizeHint == YummConstants.TABLET_SIZED_DEVICE)
    {
      int numberOfColumns;
      if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
      {
        numberOfColumns = 3;
      }
      else
      {
        // portrait mode
        numberOfColumns = 2;
      }

      recipeCardLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
    }
    else
    {
      recipeCardLayoutManager = new LinearLayoutManager(getContext());
    }

    fragmentRecipeCardBinding.rvRecipeCardFragment.setLayoutManager(recipeCardLayoutManager);

    if(recipeList == null)
    {
      recipeList = new ArrayList<>();
    }

    // Create the adapter
    recipeCardAdapter = new RecipeCardAdapter(recipeList, recipeCardListItemClickListener);

    // Register the adapter on the recycler view
    fragmentRecipeCardBinding.rvRecipeCardFragment.setAdapter(recipeCardAdapter);

    return fragmentRecipeCardBinding.getRoot();
  }

  public void setLayoutSizeHint(int layoutSizeHint)
  {
    this.layoutSizeHint = layoutSizeHint;
  }

  List<Recipe> getRecipeList()
  {
    return this.recipeList;
  }

  public void setRecipeList(List<Recipe> recipeList)
  {
    if(this.recipeList != null)
    {
      this.recipeList.addAll(recipeList);
    }
    else
    {
      this.recipeList = recipeList;
    }

    if(fragmentRecipeCardBinding != null)
    {
      updateUi();
    }
  }

  private void updateUi()
  {
    recipeCardAdapter.notifyDataSetChanged();
  }

  public void hideListAndShowError()
  {
    fragmentRecipeCardBinding.rvRecipeCardFragment.setVisibility(View.GONE);

    fragmentRecipeCardBinding.tvRecipeCardNetworkError.setVisibility(View.VISIBLE);
  }

  public void showListAndClearError()
  {
    fragmentRecipeCardBinding.rvRecipeCardFragment.setVisibility(View.VISIBLE);

    fragmentRecipeCardBinding.tvRecipeCardNetworkError.setVisibility(View.GONE);
  }
}
