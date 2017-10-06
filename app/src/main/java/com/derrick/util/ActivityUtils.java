package com.derrick.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by derrick.njeru on 8/20/2017.
 * Using fragment manager{@code fragmentManager} to add fragment{@code fragment} with id {@code contentFrame}
 */

public class ActivityUtils {
    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int contentFrame) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(contentFrame, fragment);
        transaction.commit();
    }
}
