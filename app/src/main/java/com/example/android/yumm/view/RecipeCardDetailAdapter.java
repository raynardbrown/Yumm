package com.example.android.yumm.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.yumm.R;
import com.example.android.yumm.databinding.RecipeCardDetailListItemBinding;
import com.example.android.yumm.model.Recipe;

public class RecipeCardDetailAdapter extends RecyclerView.Adapter<RecipeCardDetailAdapter.RecipeCardDetailViewHolder>
{
  private Recipe recipe;
  private IRecipeCardDetailListItemClickListener recipeCardDetailListItemClickListener;

  RecipeCardDetailAdapter(Recipe recipe,
                          IRecipeCardDetailListItemClickListener recipeCardDetailListItemClickListener)
  {
    this.recipe = recipe;
    this.recipeCardDetailListItemClickListener = recipeCardDetailListItemClickListener;
  }

  @Override
  public RecipeCardDetailViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType)
  {
    Context context = parent.getContext();
    int layoutIdForListItem = R.layout.recipe_card_detail_list_item;
    LayoutInflater inflater = LayoutInflater.from(context);
    final boolean shouldAttachToParentImmediately = false;

    RecipeCardDetailListItemBinding itemBinding = DataBindingUtil.inflate(inflater, layoutIdForListItem, parent, shouldAttachToParentImmediately);

    return new RecipeCardDetailViewHolder(itemBinding);
  }

  @Override
  public void onBindViewHolder(RecipeCardDetailViewHolder holder,
                               int position)
  {
    if(position == 0)
    {
      TextView textView = holder.itemBinding.tvRecipeCardDetailItem;
      textView.setText(textView.getContext().getString(R.string.recipe_ingredients_label));
    }
    else
    {
      // position - 1: because the first position in the master list is the ingredients item.
      holder.itemBinding.tvRecipeCardDetailItem.setText(recipe.getRecipeSteps().get(position - 1).getShortDescription());
    }
  }

  @Override
  public int getItemCount()
  {
    return recipe.getRecipeSteps().size() + 1;
  }

  class RecipeCardDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
  {
    RecipeCardDetailListItemBinding itemBinding;

    RecipeCardDetailViewHolder(RecipeCardDetailListItemBinding itemBinding)
    {
      super(itemBinding.getRoot());

      this.itemBinding = itemBinding;

      this.itemBinding.getRoot().setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
      final int clickedIndex = getAdapterPosition();

      RecipeCardDetailAdapter.this.recipeCardDetailListItemClickListener.onRecipeCardDetailListItemClick(recipe, clickedIndex);
    }
  }
}
