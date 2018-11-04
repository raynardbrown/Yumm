package com.example.android.yumm.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.yumm.R;
import com.example.android.yumm.databinding.RecipeCardListItemBinding;
import com.example.android.yumm.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardViewHolder>
{
  private List<Recipe> recipeList;
  private IRecipeCardListItemClickListener recipeCardListItemClickListener;

  RecipeCardAdapter(List<Recipe> recipeList,
                    IRecipeCardListItemClickListener recipeCardListItemClickListener)
  {
    this.recipeList = recipeList;
    this.recipeCardListItemClickListener = recipeCardListItemClickListener;
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
    // Add the image if it is available

    final String imagePath = recipeList.get(position).getImagePath();
    if(imagePath != null && imagePath.length() > 0)
    {
      ImageView imageView = holder.itemBinding.ivRecipeCardItemImage;

      imageView.setVisibility(View.VISIBLE);

      Picasso.with(imageView.getContext())
              .load(imagePath)
              .into(imageView);
    }
    else
    {
      holder.itemBinding.ivRecipeCardItemImage.setVisibility(View.GONE);
    }

    holder.itemBinding.tvRecipeCardItemTitle.setText(recipeList.get(position).getName());
    holder.itemBinding.tvRecipeCardItemServings.setText(String.format(Locale.getDefault(), "%d", recipeList.get(position).getServings()));
  }

  @Override
  public int getItemCount()
  {
    return recipeList.size();
  }

  class RecipeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
  {
    RecipeCardListItemBinding itemBinding;

    RecipeCardViewHolder(RecipeCardListItemBinding itemBinding)
    {
      super(itemBinding.getRoot());

      this.itemBinding = itemBinding;

      this.itemBinding.getRoot().setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
      final int clickedIndex = getAdapterPosition();
      Recipe recipe = recipeList.get(clickedIndex);

      RecipeCardAdapter.this.recipeCardListItemClickListener.onRecipeCardListItemClick(recipe);
    }
  }
}
