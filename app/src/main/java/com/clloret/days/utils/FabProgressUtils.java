package com.clloret.days.utils;

import android.widget.ImageView;
import com.clloret.days.R;
import com.github.jorgecastilloprz.FABProgressCircle;

public class FabProgressUtils {

  public static final int PROGRESS_DELAY = 151;

  public static void fixFinalIconPosition(FABProgressCircle fabProgressCircle) {

    if (fabProgressCircle == null) {
      return;
    }

    fabProgressCircle.addOnLayoutChangeListener(
        (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {

          ImageView imgView = v.findViewById(R.id.completeFabIcon);
          if ((imgView != null) && (imgView.getScaleType()
              != ImageView.ScaleType.CENTER_INSIDE)) {
            imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
          }
        });
  }
}
