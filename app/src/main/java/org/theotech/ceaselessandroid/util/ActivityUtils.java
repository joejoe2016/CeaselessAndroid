package org.theotech.ceaselessandroid.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.util.Log;

import org.theotech.ceaselessandroid.R;
import org.theotech.ceaselessandroid.fragment.ContactUsFragment;
import org.theotech.ceaselessandroid.fragment.HelpFragment;
import org.theotech.ceaselessandroid.fragment.MainFragment;
import org.theotech.ceaselessandroid.fragment.PeopleFragment;
import org.theotech.ceaselessandroid.fragment.SettingsFragment;
import org.theotech.ceaselessandroid.fragment.VerseFragment;

/**
 * Created by uberx on 10/8/15.
 */
public class ActivityUtils {
    private static final String TAG = ActivityUtils.class.getSimpleName();

    public static void loadFragment(Activity activity, FragmentManager fragmentManager, NavigationView navigation, int itemId) {
        loadFragment(activity, fragmentManager, null, navigation, itemId);
    }

    public static void loadFragment(Activity activity, FragmentManager fragmentManager, Bundle bundle, NavigationView navigation, int itemId) {
        Fragment fragment = getFragmentForNavigationItemId(itemId);
        String newTitle = getTitleForNavigationItemId(activity, itemId);
        Fragment currentFragment = fragmentManager.findFragmentByTag(newTitle);
        if (fragment != null && (currentFragment == null || !currentFragment.isVisible())) {
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment, fragment, newTitle).addToBackStack(activity.getTitle().toString()).commit();
            navigation.setCheckedItem(itemId);
        } else {
            Log.d(TAG, String.format("Required fragment %s already visible, not reloading", newTitle));
        }
    }

    private static Fragment getFragmentForNavigationItemId(int itemId) {
        if (itemId == R.id.nav_home) {
            return new MainFragment();
        } else if (itemId == R.id.nav_people) {
            return new PeopleFragment();
        } else if (itemId == R.id.nav_verse) {
            return new VerseFragment();
        } else if (itemId == R.id.nav_settings) {
            return new SettingsFragment();
        } else if (itemId == R.id.nav_help) {
            return new HelpFragment();
        } else if (itemId == R.id.nav_contact_us) {
            return new ContactUsFragment();
        }
        return null;
    }

    private static String getTitleForNavigationItemId(Context context, int itemId) {
        if (itemId == R.id.nav_home) {
            return context.getString(R.string.app_name);
        } else if (itemId == R.id.nav_people) {
            return context.getString(R.string.nav_people);
        } else if (itemId == R.id.nav_verse) {
            return context.getString(R.string.nav_verse);
        } else if (itemId == R.id.nav_settings) {
            return context.getString(R.string.nav_settings);
        } else if (itemId == R.id.nav_help) {
            return context.getString(R.string.nav_help);
        } else if (itemId == R.id.nav_contact_us) {
            return context.getString(R.string.nav_contact_us);
        }
        return null;
    }

    public static Integer getNavigationItemResourceIdForFragmentName(Context context, String fragmentName) {
        if (fragmentName.equals(context.getString(R.string.app_name))) {
            return R.id.nav_home;
        } else if (fragmentName.equals(context.getString(R.string.nav_people))) {
            return R.id.nav_people;
        } else if (fragmentName.equals(context.getString(R.string.nav_verse))) {
            return R.id.nav_verse;
        } else if (fragmentName.equals(context.getString(R.string.nav_settings))) {
            return R.id.nav_settings;
        } else if (fragmentName.equals(context.getString(R.string.nav_help))) {
            return R.id.nav_help;
        } else if (fragmentName.equals(context.getString(R.string.nav_contact_us))) {
            return R.id.nav_contact_us;
        }
        return null;
    }

    public static Uri getContactPhotoUri(ContentResolver cr, String contactId, boolean highRes) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
        if (highRes) {
            return Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        } else {
            return Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        }
    }
}