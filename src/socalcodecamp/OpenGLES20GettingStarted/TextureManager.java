package socalcodecamp.OpenGLES20GettingStarted;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureManager {
	private static TextureManager mSharedManager;
	private GL10 mGL;
	private HashMap<Integer, LinkedList<RenderPrimative>> mTexturePrimatives = new HashMap<Integer, LinkedList<RenderPrimative>>();
	private HashMap<Integer, Integer> mTextures = new HashMap<Integer, Integer>();
	
	public static TextureManager sharedManager() {
		if (mSharedManager == null) {
			mSharedManager = new TextureManager();
		}
		return mSharedManager;
	}
	
	public int bitmapTexture(Bitmap bitmap) {
		int mTextures[] = new int[1];
		if (GenericRenderer.RENDER_VERSION == 1) {
			mGL.glGenTextures(1, mTextures, 0);
			mGL.glBindTexture(GL10.GL_TEXTURE_2D, mTextures[0]);
			
	        mGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	        mGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
	        mGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
	        mGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
	
	        mGL.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE); //GL10.GL_REPLACE);
	        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);		
		} else if (GenericRenderer.RENDER_VERSION == 2) {
			GLES20.glGenTextures(1, mTextures, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
			
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	
	        //GLES20.glTexEnvf(GLES20.GL_TEXTURE_ENV, GLES20.GL_TEXTURE_ENV_MODE, GLES20.GL_MODULATE); //GL10.GL_REPLACE);
	        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		}

		return mTextures[0];
	}
	
	public void setGL(GL10 gl) {
		mGL = gl;
	}
	
	public int resourceTexture(int resourceId, RenderPrimative primative) {
		int textureId = 0;
		if (!mTextures.containsKey(resourceId)) {
			synchronized(mTexturePrimatives) {
				if (mTexturePrimatives.containsKey(resourceId)) {
					LinkedList<RenderPrimative> list = mTexturePrimatives.get(resourceId);
					list.add(primative);
				} else {
					LinkedList<RenderPrimative> list = new LinkedList<RenderPrimative>();
					list.add(primative);
					mTexturePrimatives.put(resourceId, list);
				}
			}
		} else {
			textureId = mTextures.get(resourceId);
		}
		return textureId;
	}
	
	public void loadTextures() {
        SystemManager sysMgr = SystemManager.sharedManager();
        synchronized(mTexturePrimatives) {
			if (!mTexturePrimatives.isEmpty()) {
				Set<Integer> keyset = mTexturePrimatives.keySet();
				for (Integer key : keyset) {
			        InputStream is = sysMgr.getContext().getResources().openRawResource(key);
					Bitmap bitmap = null;
					try {
						bitmap = BitmapFactory.decodeStream(is);
	
					} finally {
						try {
							is.close();
							is = null;
						} catch (IOException e) {
						}
					}
					
					int textureId = bitmapTexture(bitmap);
					bitmap.recycle();
					mTextures.put(key, textureId);
					LinkedList<RenderPrimative> primatives = mTexturePrimatives.get(key);
					for (int i = 0;i < primatives.size();++i) {
						primatives.get(i).mTextureName = textureId;
					}
				}			
				Log.v("TextureManager:loadTextures", "We are here");
				mTexturePrimatives.clear();
			}
		}
	}
}
