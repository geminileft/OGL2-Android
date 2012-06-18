package socalcodecamp.OpenGLES20GettingStarted;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class TextureManager {
	private static TextureManager mSharedManager;
	private GL10 mGL;
	private int mVersion;
	
	public static TextureManager sharedManager() {
		if (mSharedManager == null) {
			mSharedManager = new TextureManager();
		}
		return mSharedManager;
	}
	
	public int bitmapTexture(Bitmap bitmap) {
		int mTextures[] = new int[1];
		if (mVersion == 1) {
			mGL.glGenTextures(1, mTextures, 0);
			mGL.glBindTexture(GL10.GL_TEXTURE_2D, mTextures[0]);
			
	        mGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	        mGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
	        mGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
	        mGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
	
	        mGL.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE); //GL10.GL_REPLACE);
	        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);		
		}

		return mTextures[0];
	}
	
	public void setGL(GL10 gl) {
		mGL = gl;
	}
	
	public void setVersion(int version) {
		mVersion = version;
	}
}
