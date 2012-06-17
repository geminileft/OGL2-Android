package socalcodecamp.OpenGLES20GettingStarted;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

public class OGL11Renderer implements GLSurfaceView.Renderer {

	private RenderPrimative mPrimative;
	
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);			
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		mPrimative = new RenderPrimative();
		int mTextures[] = new int[1];
		gl.glGenTextures(1, mTextures, 0);
        mPrimative.mTextureName = mTextures[0];		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextures[0]);
		
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE); //GL10.GL_REPLACE);
        SystemManager sysMgr = SystemManager.sharedManager();
        InputStream is = sysMgr.getContext().getResources().openRawResource(R.drawable.mg);
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}
		
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		
		
		final float coordinates[] = {    		
				// Mapping coordinates for the vertices
				0.0f, 1.0f,		// top left		(V2)
				0.0f, 0.0f,		// bottom left	(V1)
				1.0f, 1.0f,		// top right	(V4)
				1.0f, 0.0f		// bottom right	(V3)
		};
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(coordinates.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		mPrimative.mTextureBuffer = byteBuf.asFloatBuffer();
		mPrimative.mTextureBuffer.put(coordinates);
		mPrimative.mTextureBuffer.position(0);

		final float leftX = -(float)width / 2;
		final float rightX = leftX + width;
		final float bottomY = -(float)height / 2;
		final float topY = bottomY + height;

		final float vertices[] = {
	    		leftX, bottomY
	    		, leftX, topY
	    		, rightX, bottomY
	    		, rightX, topY
	    };
	    
   		byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		mPrimative.mVertexBuffer = byteBuf.asFloatBuffer();
		mPrimative.mVertexBuffer.put(vertices);
		mPrimative.mVertexBuffer.position(0);
		
		mPrimative.mR = 1.0f;
		mPrimative.mG = 0.0f;
		mPrimative.mB = 1.0f;
		mPrimative.mA = 1.0f;
		gl.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    	final int zDepth = height / 2;
    	final float ratio = (float)width / height;
    	gl.glMatrixMode(GL10.GL_PROJECTION);
    	gl.glFrustumf(-ratio, ratio, -1, 1, 1, zDepth);
    	gl.glMatrixMode(GL10.GL_MODELVIEW);
    	gl.glLoadIdentity();
    	gl.glTranslatef(0, 0, -zDepth);	
	}
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mPrimative.mVertexBuffer);
    	gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mPrimative.mTextureBuffer);
   
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mPrimative.mTextureName);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);	

		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public void copyToBuffer(RenderPrimative primatives[], int count) {
		
	}
}
