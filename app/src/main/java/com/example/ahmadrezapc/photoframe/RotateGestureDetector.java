package com.example.ahmadrezapc.photoframe;

import android.content.Context;
import android.view.MotionEvent;

/**
 * @author Almer Thie (code.almeros.com)
 */
public class RotateGestureDetector extends TwoFingerGestureDetector {

    /**
     * Listener which must be implemented which is used by RotateGestureDetector
     * to perform callbacks to any implementing class which is registered to a
     * RotateGestureDetector via the constructor.
     * 
     * @see SimpleOnRotateGestureListener
     */
    public interface OnRotateGestureListener {
        public boolean onRotate(RotateGestureDetector detector);
        public boolean onRotateBegin(RotateGestureDetector detector);
        public void onRotateEnd(RotateGestureDetector detector);
    }

    /**
     * Helper class which may be extended and where the methods may be
     * implemented. This way it is not necessary to implement all methods of
     * OnRotateGestureListener.
     */
    public static class SimpleOnRotateGestureListener implements OnRotateGestureListener {
        public boolean onRotate(RotateGestureDetector detector) {
            return false;
        }

        public boolean onRotateBegin(RotateGestureDetector detector) {
            return true;
        }

        public void onRotateEnd(RotateGestureDetector detector) {
            // Do nothing, overridden implementation may be used
        }
    }

    private final OnRotateGestureListener mListener;
    private boolean mSloppyGesture;

    public RotateGestureDetector(Context context, OnRotateGestureListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void handleStartProgressEvent(int actionCode, MotionEvent event) {
        switch (actionCode) {
        case MotionEvent.ACTION_POINTER_DOWN:
            // At least the second finger is on screen now

            resetState(); // In case we missed an UP/CANCEL event
            mPrevEvent = MotionEvent.obtain(event);
            mTimeDelta = 0;

            updateStateByEvent(event);

            // See if we have a sloppy gesture
            mSloppyGesture = isSloppyGesture(event);
            if (!mSloppyGesture) {
                // No, start gesture now
                mGestureInProgress = mListener.onRotateBegin(this);
            }
            break;

        case MotionEvent.ACTION_MOVE:
            if (!mSloppyGesture) {
                break;
            }

            // See if we still have a sloppy gesture
            mSloppyGesture = isSloppyGesture(event);
            if (!mSloppyGesture) {
                // No, start normal gesture now
                mGestureInProgress = mListener.onRotateBegin(this);
            }

            break;

        case MotionEvent.ACTION_POINTER_UP:
            if (!mSloppyGesture) {
                break;
            }

            break;
        }
    }

    @Override
    protected void handleInProgressEvent(int actionCode, MotionEvent event) {
        switch (actionCode) {
        case MotionEvent.ACTION_POINTER_UP:
            // Gesture ended but
            updateStateByEvent(event);

            if (!mSloppyGesture) {
                mListener.onRotateEnd(this);
            }

            resetState();
            break;

        case MotionEvent.ACTION_CANCEL:
            if (!mSloppyGesture) {
                mListener.onRotateEnd(this);
            }

            resetState();
            break;

        case MotionEvent.ACTION_MOVE:
            updateStateByEvent(event);

            // Only accept the event if our relative pressure is within
            // a certain limit. This can help filter shaky data as a
            // finger is lifted.
            if (mCurrPressure / mPrevPressure > PRESSURE_THRESHOLD) {
                final boolean updatePrevious = mListener.onRotate(this);
                if (updatePrevious) {
                    mPrevEvent.recycle();
                    mPrevEvent = MotionEvent.obtain(event);
                }
            }
            break;
        }
    }

    @Override
    protected void resetState() {
        super.resetState();
        mSloppyGesture = false;
    }

    /**
     * Return the rotation difference from the previous rotate event to the
     * current event.
     * 
     * @return The current rotation //difference in degrees.
     */
    public float getRotationDegreesDelta() {
        double diffRadians = Math.atan2(mPrevFingerDiffY, mPrevFingerDiffX)
                - Math.atan2(mCurrFingerDiffY, mCurrFingerDiffX);
        return (float) (diffRadians * 180 / Math.PI);
    }
}
