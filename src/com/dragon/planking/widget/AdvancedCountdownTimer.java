package com.dragon.planking.widget;

import android.os.Handler;
import android.os.Message;

public abstract class AdvancedCountdownTimer {
	private static final int MSG_RUN = 1;

	private final long mCountdownInterval;
	private long mTotalTime;
	private long mRemainTime;

	public AdvancedCountdownTimer(long millisInFuture, long countDownInterval) {
		mTotalTime = millisInFuture;
		mCountdownInterval = countDownInterval;
		mRemainTime = millisInFuture;
	}

	// 取锟斤拷锟绞�
	public final void cancel() {
		mHandler.removeMessages(MSG_RUN);
	}

	// 锟斤拷锟铰匡拷始锟斤拷时
	public final void resume() {
		mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
	}

	// 锟斤拷停锟斤拷时
	public final void pause() {
		mHandler.removeMessages(MSG_RUN);
	}

	// 锟斤拷始锟斤拷时
	public synchronized final AdvancedCountdownTimer start() {
	    onStart();
		if (mRemainTime < 0) {// 锟斤拷时锟斤拷锟斤拷蠓祷锟�
			onFinish();
			return this;
		}
		// 锟斤拷锟矫硷拷时锟斤拷锟�
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN),
				mCountdownInterval);
		return this;
	}

	public abstract void onTick(long millisUntilFinished, int percent);// 锟斤拷时锟斤拷

	public abstract void onStart();// 锟斤拷时锟斤拷锟斤拷
	public abstract void onFinish();// 锟斤拷时锟斤拷锟斤拷

	// 通锟斤拷handler锟斤拷锟斤拷android UI锟斤拷锟斤拷示锟斤拷时时锟斤拷
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			synchronized (AdvancedCountdownTimer.this) {
				if (msg.what == MSG_RUN) {
					mRemainTime = mRemainTime - mCountdownInterval;

					if (mRemainTime <0) {
						onFinish();
					} else if (mRemainTime < mCountdownInterval) {
						sendMessageDelayed(obtainMessage(MSG_RUN), mRemainTime);
					} else {

						onTick(mRemainTime,
								new Long(100 * (mTotalTime - mRemainTime)
										/ mTotalTime).intValue());

						sendMessageDelayed(obtainMessage(MSG_RUN),
								mCountdownInterval);
					}
				}
			}
		}
	};
}