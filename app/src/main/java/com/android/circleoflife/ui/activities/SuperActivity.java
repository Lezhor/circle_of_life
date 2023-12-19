package com.android.circleoflife.ui.activities;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Provides some basic methods which are used all the time for all activities throughout the project.
 */
public class SuperActivity extends AppCompatActivity {

    /**
     * Inflates a menu with given ID and colors EVERY Icon of every item to the color textColorSecondary specified by the theme<br>
     * Should be used like this:
     * <pre>{@code
     *     @Override
     *     public boolean onCreateOptionsMenu(Menu menu) {
     *         setUpMenu(menu, R.menu.menu_id);
     *         // Other code
     *         return true;
     *     }
     * }</pre>
     * @param menu menu item which is passed as parameter in {@link AppCompatActivity#onCreateOptionsMenu(Menu) onCreateOptionsMenu(Menu)}
     * @param menuID menuID which should be only accessed via {@code R.menu.menu_id}
     */
    protected void setUpMenu(Menu menu, int menuID) {
        getMenuInflater().inflate(menuID, menu);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr = obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorSecondary});
        int primaryColor = arr.getColor(0, -1);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
            }
        }

    }

}
