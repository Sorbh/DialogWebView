package in.unicodelabs.view;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * Created by saurabh on 4/3/18.
 */

public class HideBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    public HideBehavior() {
        super();
    }

    public HideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * The layoutDependsOn method is CoordinatorLayout’s way to see which views your floating action button are dependent on.
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
//        return dependency instanceof WebView;
//    }

    /**
     * The Android documentation indicates that by setting up this dependency, you will receive calls to onDependentViewChanged when a dependent view changes its size or position.
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
//        return super.onDependentViewChanged(parent, child, dependency);
//    }
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull LinearLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull final LinearLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        Log.d("Be", "" + dyConsumed + " " + dyUnconsumed);

        if (dyConsumed > 0) {
            child.animate().setStartDelay(0).translationY(child.getHeight()).setDuration(100).withLayer().start();
        } else if (dyConsumed < 0) {
            child.animate().setStartDelay(0).translationY(0).setDuration(100).withLayer().start();
        }
    }

}
