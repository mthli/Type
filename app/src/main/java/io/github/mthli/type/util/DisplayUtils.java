/*
 * Copyright 2016 Matthew Lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.github.mthli.type.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DisplayUtils {
    public static float dp2px(@NonNull Context context, @FloatRange(from = 0.0f, to = Float.MAX_VALUE) float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static float getDensity(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenHeight(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getStatusBarHeight(@NonNull Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }

        return 0;
    }
}
