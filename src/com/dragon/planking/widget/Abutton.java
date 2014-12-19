package com.dragon.planking.widget;



import com.dragon.planking.R;
import com.dragon.planking.abutton.cation.Action;
import com.dragon.planking.abutton.cation.BackAction;
import com.dragon.planking.abutton.cation.DrawerAction;
import com.dragon.planking.abutton.cation.LineSegment;
import com.dragon.planking.abutton.util.UiHelper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;


/**
 * ActionView allows you to dynamically display action and automatically animate from one action to another.
 * <p/>
 * ActionView can be included in your layout xml quite easy:
 * <pre>
 * {@code
 * <at.markushi.ui.ActionView
 *      android:id="@+id/action"
 *      android:layout_width="56dip"
 *      android:layout_height="56dip"
 *      android:padding="16dip"
 *      app:av_color="@android:color/white"
 *      app:av_action="drawer"/>
 * }</pre>
 *
 * In order to start a transition to another {@link at.markushi.ui.action.Action} call {@link #setActionId(at.markushi.ui.action.Action)}
 *
 * @see at.markushi.ui.action.BackAction
 * @see at.markushi.ui.action.CloseAction
 * @see at.markushi.ui.action.DrawerAction
 * @see at.markushi.ui.action.PlusAction
 */
public class Abutton extends View {

	public static final int ROTATE_CLOCKWISE = 0;
	public static final int ROTATE_COUNTER_CLOCKWISE = 1;
	private static final int STROKE_SIZE_DIP = 2;      //change button line big or small
	
	public static final int DRAWER = 0;
	public static final int BLACK = 1;

	private int color;
	private Action oldAction;
	private Action currentAction;
	private Action animatedAction;

	private float animationProgress;
	private float scale;
	private int padding;
	private Path path;
	private Paint paint;

	private float centerX;
	private float centerY;
	private boolean ready = false;
	private boolean animateWhenReady = false;
	private int rotation = ROTATE_CLOCKWISE;
	private long animationDuration;
	private int size;
	private int av_animationDuration;
	
	private int actionId;

	public Abutton(Context context) {
		this(context, null);
	}

	public Abutton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Abutton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionView);
		animationDuration = a.getInt(R.styleable.ActionView_animationDuration, 200);
		animatedAction = new Action(new float[Action.ACTION_SIZE], null);

//		final float strokeSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STROKE_SIZE_DIP, context.getResources().getDisplayMetrics());


		if (attrs == null) {
			return;
		}
		
		final int color = a.getColor(R.styleable.ActionView_av_color, 0xDDFFFFF);
		final float strokeSize = a.getDimension(R.styleable.ActionView_strokeSize, STROKE_SIZE_DIP);
		actionId = a.getInt(R.styleable.ActionView_av_action, 0);
		
	        path = new Path();
	        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	        paint.setColor(0xDDFFFFFF);
	        paint.setStrokeWidth(strokeSize);
	        paint.setStyle(Paint.Style.STROKE);
		a.recycle();

		paint.setColor(color);
//		final Action action = getActionFromEnum(actionId);
		
		setActionId(actionId);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.centerX = w / 2;
		this.centerY = h / 2;
		padding = getPaddingLeft();
		size = Math.min(w, h);
		scale = Math.min(w, h) - (2 * padding);
		ready = true;
		transformActions();

		if (animateWhenReady) {
			animateWhenReady = false;
			startAnimation();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (currentAction == null) {
			return;
		}

		if (oldAction == null) {
			updatePath(currentAction);
		} else {
			final float inverseProgress = 1f - animationProgress;
			final float[] current = currentAction.getLineData();
			final float[] old = oldAction.getLineData();
			final float[] animated = animatedAction.getLineData();
			for (int i = 0; i < animated.length; i++) {
				animated[i] = current[i] * animationProgress + old[i] * inverseProgress;
			}
			updatePath(animatedAction);
		}

		canvas.rotate((rotation == ROTATE_CLOCKWISE ? 180f : -180f) * animationProgress, centerX, centerY);
		canvas.drawPath(path, paint);
	}

	@SuppressWarnings("UnusedDeclaration")
	public float getAnimationProgress() {
		return animationProgress;
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setAnimationProgress(float animationProgress) {
		this.animationProgress = animationProgress;
		UiHelper.postInvalidateOnAnimation(this);
	}

	/**
	 * Set the color used for drawing an {@link at.markushi.ui.action.Action}.
	 *
	 * @param color
	 */
	public void setColor(final int color) {
		this.color = color;
		paint.setColor(color);
		UiHelper.postInvalidateOnAnimation(this);
	}

	/**
	 * @return the current action
	 */
	public int getActionId() {
		return actionId;
	}

	/**
	 * Sets the new action. If an action was set before a transition will be started.
	 *
	 * @param action
	 * @see #setAction(at.markushi.ui.action.Action, boolean)
	 * @see #setAction(at.markushi.ui.action.Action, boolean, int)
	 * @see #setAction(at.markushi.ui.action.Action, at.markushi.ui.action.Action, int, long)
	 */
	public void setActionId(final int actionid) {
	    
	    setActionId(actionid, true, ROTATE_CLOCKWISE);
	}

	/**
	 * Sets the new action. If an action was set before a transition will be started.
	 *
	 * @param action
	 * @param rotation Can be either {@link #ROTATE_CLOCKWISE} or {@link #ROTATE_COUNTER_CLOCKWISE}.
	 */
	public void setActionId(final int actionid, final int rotation) {
	    setActionId( actionid, true, rotation);
	}

	/**
	 * Sets the new action.
	 *
	 * @param action
	 * @param animate If a prior action was set and {@code true} a transition will be started, otherwise not.
	 */
	public void setActionId(final int actionid, final boolean animate) {
	    setActionId(actionid, animate, ROTATE_CLOCKWISE);
	}


	/**
	 * Set the animation duration used for Action transitions
	 *
	 * @param animationDuration the duration in ms
	 */
	public void setAnimationDuration(long animationDuration) {
		this.animationDuration = animationDuration;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final SavedState ss = new SavedState(superState);
		ss.currentAction = currentAction;
		ss.color = color;
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		this.color = ss.color;
		this.currentAction = ss.currentAction;
		this.animationProgress = 1f;
	}

	private void setActionId(int actionid, boolean animate, int rotation) {
	    actionId = actionid;
	    Action action = getActionFromEnum(actionId);

		this.rotation = rotation;
		if (currentAction == null) {
			currentAction = action;
			currentAction.flipHorizontally();
			animationProgress = 1f;
			UiHelper.postInvalidateOnAnimation(this);
			return;
		}

		if (currentAction.getClass().equals(action.getClass())) {
			return;
		}

		oldAction = currentAction;
		currentAction = action;

		if (animate) {
			animationProgress = 0f;
			if (ready) {
				startAnimation();
			} else {
				animateWhenReady = true;
			}
		} else {
			animationProgress = 1f;
			UiHelper.postInvalidateOnAnimation(this);
		}
	}

	private void updatePath(Action action) {
		path.reset();

		final float[] data = action.getLineData();
		// Once we're near the end of the animation we use the action segments to draw linked lines
		if (animationProgress > 0.95f && !action.getLineSegments().isEmpty()) {
			for (LineSegment s : action.getLineSegments()) {
				path.moveTo(data[s.getStartIdx() + 0], data[s.getStartIdx() + 1]);
				path.lineTo(data[s.getStartIdx() + 2], data[s.getStartIdx() + 3]);
				for (int i = 1; i < s.indexes.length; i++) {
					path.lineTo(data[s.indexes[i] + 0], data[s.indexes[i] + 1]);
					path.lineTo(data[s.indexes[i] + 2], data[s.indexes[i] + 3]);
				}
			}
		} else {
			for (int i = 0; i < data.length; i += 4) {
				path.moveTo(data[i + 0], data[i + 1]);
				path.lineTo(data[i + 2], data[i + 3]);
			}
		}
	}

	private void transformActions() {
		if (currentAction != null && !currentAction.isTransformed()) {
			currentAction.transform(padding, padding, scale, size);
		}

		if (oldAction != null && !oldAction.isTransformed()) {
			oldAction.transform(padding, padding, scale, size);
		}
	}

	private void startAnimation() {
		oldAction.flipHorizontally();
		currentAction.flipHorizontally();

		transformActions();

		animatedAction.setLineSegments(currentAction.getLineSegments());
		final ObjectAnimator animator = ObjectAnimator.ofFloat(Abutton.this, "animationProgress", 0f, 1f);
//		animator.setInterpolator(BakedBezierInterpolator.getInstance());
		animator.setInterpolator(new AccelerateInterpolator());
		animator.setDuration(animationDuration).start();
	}

	private Action getActionFromEnum(int id) {
		switch (id) {
		case 0:
			return new DrawerAction();
		case 1:
			return new BackAction();
		}

		return null;
	}

	static class SavedState extends BaseSavedState {

		Action currentAction;
		int color;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel source) {
			super(source);
			currentAction = source.readParcelable(getClass().getClassLoader());
			color = source.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeParcelable(currentAction, 0);
			dest.writeInt(color);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

			@Override
			public SavedState createFromParcel(Parcel parcel) {
				return new SavedState(parcel);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
