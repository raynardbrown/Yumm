package com.example.android.yumm.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.yumm.R;
import com.example.android.yumm.databinding.RecipeCardIngredientListItemBinding;
import com.example.android.yumm.model.RecipeIngredient;

import java.util.List;
import java.util.Locale;

public class RecipeCardIngredientAdapter extends RecyclerView.Adapter<RecipeCardIngredientAdapter.RecipeCardIngredientViewHolder>
{
  private List<RecipeIngredient> recipeIngredientList;

  RecipeCardIngredientAdapter(List<RecipeIngredient> recipeIngredientList)
  {
    this.recipeIngredientList = recipeIngredientList;
  }

  @Override
  public RecipeCardIngredientViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType)
  {
    Context context = parent.getContext();
    int layoutIdForListItem = R.layout.recipe_card_ingredient_list_item;
    LayoutInflater inflater = LayoutInflater.from(context);
    final boolean shouldAttachToParentImmediately = false;

    RecipeCardIngredientListItemBinding itemBinding = DataBindingUtil.inflate(inflater, layoutIdForListItem, parent, shouldAttachToParentImmediately);

    return new RecipeCardIngredientViewHolder(itemBinding);
  }

  @Override
  public void onBindViewHolder(RecipeCardIngredientViewHolder holder,
                               int position)
  {
    holder.itemBinding.tvRecipeCardIngredientName.setText(recipeIngredientList.get(position).getName());
    holder.itemBinding.tvRecipeCardIngredientQuantity.setText(String.format(Locale.getDefault(), "%4.2f", recipeIngredientList.get(position).getQuantity()));
    holder.itemBinding.tvRecipeCardIngredientMeasure.setText(recipeIngredientList.get(position).getMeasure());
  }

  @Override
  public int getItemCount()
  {
    return recipeIngredientList.size();
  }

  class RecipeCardIngredientViewHolder extends RecyclerView.ViewHolder
  {
    RecipeCardIngredientListItemBinding itemBinding;

    RecipeCardIngredientViewHolder(RecipeCardIngredientListItemBinding itemBinding)
    {
      super(itemBinding.getRoot());

      this.itemBinding = itemBinding;
    }
  }
}
