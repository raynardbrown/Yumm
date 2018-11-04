package com.example.android.yumm;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.ComponentNameMatchers;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.yumm.model.Recipe;
import com.example.android.yumm.model.RecipeIngredient;
import com.example.android.yumm.model.RecipeStep;
import com.example.android.yumm.view.MainActivity;
import com.example.android.yumm.view.RecipeCardDetailActivity;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RecipeViaIntentTest
{
  @Rule
  public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

  private IdlingResource idlingResource;

  @Before
  public void registerIdlingResource()
  {
    idlingResource = intentsTestRule.getActivity().getIdlingResource();

    IdlingRegistry.getInstance().register(idlingResource);
  }

  @After
  public void unregisterIdlingResource()
  {
    if(idlingResource != null)
    {
      IdlingRegistry.getInstance().unregister(idlingResource);
    }
  }

  @Test
  public void testRecipeDetailGetsTheFirstRecipeViaIntent()
  {
    Espresso.onView(ViewMatchers.withId(R.id.rv_recipe_card_fragment))
            .perform(RecyclerViewActions.scrollToPosition(0)).perform(ViewActions.click());

    Intents.intended(IntentMatchers.hasComponent(ComponentNameMatchers.hasClassName(RecipeCardDetailActivity.class.getName())));

    List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

    List<RecipeStep> recipeStepList = new ArrayList<>();

    Context context = InstrumentationRegistry.getTargetContext();

    Recipe recipe = new Recipe(1, context.getString(R.string.recipe_one_name_string), recipeIngredientList, recipeStepList, 8, "");

    Intent expectedIntent = new Intent();
    expectedIntent.putExtra(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_type_key), recipe);

    Assert.assertTrue(IntentMatchers.hasExtraWithKey(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_type_key)).matches(expectedIntent));

    // Make sure an empty intent does not match
    Intent emptyIntent = new Intent();
    Assert.assertFalse(IntentMatchers.hasExtraWithKey(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_type_key)).matches(emptyIntent));
  }

  @Test
  public void testRecipeDetailGetsTheLastRecipeViaIntent()
  {
    Espresso.onView(ViewMatchers.withId(R.id.rv_recipe_card_fragment))
            .perform(RecyclerViewActions.scrollToPosition(3)).perform(ViewActions.click());

    Intents.intended(IntentMatchers.hasComponent(ComponentNameMatchers.hasClassName(RecipeCardDetailActivity.class.getName())));

    List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

    List<RecipeStep> recipeStepList = new ArrayList<>();

    Context context = InstrumentationRegistry.getTargetContext();

    Recipe recipe = new Recipe(4, context.getString(R.string.recipe_four_name_string), recipeIngredientList, recipeStepList, 8, "");

    Intent expectedIntent = new Intent();
    expectedIntent.putExtra(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_type_key), recipe);

    Assert.assertTrue(IntentMatchers.hasExtraWithKey(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_type_key)).matches(expectedIntent));

    // Make sure an empty intent does not match
    Intent emptyIntent = new Intent();
    Assert.assertFalse(IntentMatchers.hasExtraWithKey(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_type_key)).matches(emptyIntent));
  }
}
