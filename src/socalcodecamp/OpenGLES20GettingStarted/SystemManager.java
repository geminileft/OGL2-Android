package socalcodecamp.OpenGLES20GettingStarted;

import android.content.Context;

public class SystemManager {

	private static SystemManager mSharedManager;
	private Context mContext;
	
	public static SystemManager sharedManager() {
		if (mSharedManager == null) {
			mSharedManager = new SystemManager();
		}
		return mSharedManager;
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public Context getContext() {
		return mContext;
	}
}
