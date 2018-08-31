package com.example.android.yumm.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.yumm.R;
import com.example.android.yumm.databinding.RecipeCardListItemBinding;
import com.example.android.yumm.model.Recipe;

import java.util.List;
import java.util.Locale;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardViewHolder>
{
  private List<Recipe> recipeList;

  RecipeCardAdapter(List<Recipe> recipeList)
  {
    this.recipeList = recipeList;
  }

  @Override
  public RecipeCardViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType)
  {
    Context context = parent.getContext();
    int layoutIdForListItem = R.layout.recipe_card_list_item;
    LayoutInflater inflater = LayoutInflater.from(context);
    final boolean shouldAttachToParentImmediately = false;

    RecipeCardListItemBinding itemBinding = DataBindingUtil.inflate(inflater, layoutIdForListItem, parent, shouldAttachToParentImmediately);

    return new RecipeCardViewHolder(itemBinding);
  }

  @Override
  public void onBindViewHolder(RecipeCardViewHolder holder, int position)
  {
    holder.itemBinding.tvRecipeCardItemTitle.setText(recipeList.get(position).getName());
    holder.itemBinding.tvRecipeCardItemServings.setText(String.format(Locale.getDefault(), "%d", recipeList.get(position).getServings()));
  }

  @Override
  public int getItemCount()
  {
    return recipeList.size();
  }

  class RecipeCardViewHolder extends RecyclerView.ViewHolder
  {
    RecipeCardListItemBinding itemBinding;

    RecipeCardViewHolder(RecipeCardListItemBinding itemBinding)
    {
      super(itemBinding.getRoot());
      this.itemBinding = itemBinding;
    }
  }
}
