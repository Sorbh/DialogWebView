//package in.unicodelabs.view.utils;
//
//import android.content.Context;
//import android.support.design.widget.CoordinatorLayout;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.LinearLayout;
//
///**
// * Created by saurabh on 4/3/18.
// */
//
//public class HideBottomBarBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
//    public HideBottomBarBehavior(Context context, AttributeSet attrs) {
//        super();
//    }
//
//    @Override
//    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final LinearLayout child,
//                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
//        return true;
//    }
//
//    @Override
//    public void onNestedScroll(final CoordinatorLayout coordinatorLayout,
//                               final LinearLayout child,
//                               final View target, final int dxConsumed, final int dyConsumed,
//                               final int dxUnconsumed, final int dyUnconsumed) {
//
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
//
//        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
//            child.setVisibility(View.GONE);
//        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
//            child.setVisibility(View.VISIBLE);
//        }
//    }
//}