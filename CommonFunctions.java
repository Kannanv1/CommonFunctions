package com.jasmis.customer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jasmis.customer.R;
import com.onesignal.OneSignal;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AMSHEER on 24,December,2018
 */
public class CommonFunctions {
    private static final CommonFunctions ourInstance = new CommonFunctions();

    public static CommonFunctions getInstance() {
        return ourInstance;
    }

    private CommonFunctions() {
    }

    /**
     * @param context          Context from activity or fragment
     * @param bundle           Bundle of values for next Activity
     * @param destinationClass Destination Activity
     * @param isFinish         Current activity need to finish or not
     */
    public void newIntent(Context context, Class destinationClass, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(context, destinationClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (isFinish) {
            ((Activity) context).finish();
        }
    }

    /**
     * @param context
     * @param message
     */
    public void showToast(Context context, String message) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                null);

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(android.R.drawable.ic_dialog_info);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    /**
     * @param content
     * @return
     */
    public Boolean nonNullEmpty(String content) {
        if (content != null && !content.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @param activity
     */
    public void HideSoftKeyboard(Activity activity) {

        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

       /* InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);*/
    }

    /**
     * Convert date format from one to another
     *
     * @param currentFormat
     * @param requiredFormat
     * @param dateString
     * @return
     */
    public String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = "";
        if (dateString == null || dateString.isEmpty()) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public void chaneTextColorBasedOnRestaurant(String colorCode, TextView... textViews) {
        for (int count = 0; count < textViews.length; count++) {
            textViews[count].setTextColor(Color.parseColor(colorCode));
        }

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public void chaneImageColorBasedOnRestaurant(String colorCode, ImageView... imageViews) {
        for (int count = 0; count < imageViews.length; count++) {
            ImageViewCompat.setImageTintList(imageViews[count], ColorStateList.valueOf(Color.parseColor(colorCode)));
        }

    }

    public void ChangeDirection(Activity mActivity, Toolbar toolbar) {
        if (SessionManager.AppProperties.getInstance().getAppDirection() == ConstantsInternal.AppDirection.RTL) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mActivity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mActivity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
        if (toolbar != null)
            ChangeToolbarLanguage(mActivity, toolbar);
    }

    public void ChangeToolbarLanguage(Activity mActivity, Toolbar mToolbar) {
        final int childCount = getAllViews(mToolbar).size();
        for (int i = 0; i < childCount; i++) {
            View view = getAllViews(mToolbar).get(i);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                if (imageView.getId() == R.id.tlbar_back) {
                    if (SessionManager.AppProperties.getInstance().getAppDirection() == ConstantsInternal.AppDirection.RTL) {
                        imageView.setScaleX(-1);
                    } else {
                        imageView.setScaleX(1);
                    }
                }
            }
        }

    }

    public void ValidField(Context context, String message) {
        String result = MessageFormat.format(LanguageConstants.EnterAValidField, message);
        Toast mdToast = Toast.makeText(context, result, Toast.LENGTH_LONG);
        mdToast.show();
    }

    public boolean isValidMobile(String phone) {
        String pattern = "[0-9]{7,15}";
        if (phone.matches(pattern)) {
            return true;
        }
        return false;
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        //final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        final String PASSWORD_PATTERN = "^((?=[A-Za-z@])(?=.*[0-9])(?![_\\\\\\\\-]).{6,})*$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void EmptyField(Context context, String message) {
        String result = MessageFormat.format(LanguageConstants.CannotbeEmpty, message);
        Toast mdToast = Toast.makeText(context, result, Toast.LENGTH_LONG);
        mdToast.show();
    }

    private List<View> getAllViews(View v) {
        if (!(v instanceof ViewGroup) || ((ViewGroup) v).getChildCount() == 0) // It's a leaf
        {
            List<View> r = new ArrayList<View>();
            r.add(v);
            return r;
        } else {
            List<View> list = new ArrayList<View>();
            list.add(v); // If it's an internal node add itself
            int children = ((ViewGroup) v).getChildCount();
            for (int i = 0; i < children; ++i) {
                list.addAll(getAllViews(((ViewGroup) v).getChildAt(i)));
            }
            return list;
        }
    }

    public void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void RegisterWithOneSignal() {
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                if (userId != null)
                    SessionManager.AppProperties.getInstance().setDeviceToken(userId);
            }
        });
    }

    public void drawLayout(AppCompatActivity activity, Toolbar toolbars) {
        final DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawerLayout);
//        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        activity.setSupportActionBar(toolbars);
        toolbars.setNavigationIcon(R.drawable.ic_menu);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbars, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                //inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(new View(activity).getWindowToken(), 0);
            }

        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

}
