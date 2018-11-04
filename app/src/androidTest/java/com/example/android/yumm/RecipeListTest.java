package com.example.android.yumm;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.yumm.view.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeListTest
{
  @Rule
  public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

  private IdlingResource idlingResource;

  @Before
  public void registerIdlingResource()
  {
    idlingResource = activityTestRule.getActivity().getIdlingResource();

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
  public void testRecipeListIsDisplayed()
  {
    // Test the first item
    Espresso.onView(ViewMatchers.withId(R.id.rv_recipe_card_fragment))
            .perform(RecyclerViewActions.scrollToPosition(0));

    Espresso.onView(ViewMatchers.withText(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_one_name_string)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    // Test the second item
    Espresso.onView(ViewMatchers.withId(R.id.rv_recipe_card_fragment))
            .perform(RecyclerViewActions.scrollToPosition(1));

    Espresso.onView(ViewMatchers.withText(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_two_name_string)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    // Test the third item
    Espresso.onView(ViewMatchers.withId(R.id.rv_recipe_card_fragment))
            .perform(RecyclerViewActions.scrollToPosition(2));

    Espresso.onView(ViewMatchers.withText(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_three_name_string)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    // Test the fourth item
    Espresso.onView(ViewMatchers.withId(R.id.rv_recipe_card_fragment))
            .perform(RecyclerViewActions.scrollToPosition(3));

    Espresso.onView(ViewMatchers.withText(InstrumentationRegistry.getTargetContext().getString(R.string.recipe_four_name_string)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
  }
}
