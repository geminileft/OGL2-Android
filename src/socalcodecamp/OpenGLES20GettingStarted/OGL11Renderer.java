package socalcodecamp.OpenGLES20GettingStarted;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class OGL11Renderer implements RenderConsumer {

	private PrimativeBuffer mPrimBuffer = new PrimativeBuffer();
	private PrimativeBuffer mBackBuffer = new PrimativeBuffer();
	private GraphicsCompletedCallback mGraphicsCallback;
	private RenderProvider mRenderProvider;
	
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);			
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		TextureManager mgr = TextureManager.sharedManager();
		mgr.setGL(gl);
		mgr.setVersion(1);

        SystemManager sysMgr = SystemManager.sharedManager();
        InputStream is = sysMgr.getContext().getResources().openRawResource(R.drawable.mg);
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
		
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		
		RenderPrimative primative;
		primative = new RenderPrimative();
		primative.mTextureName = mgr.bitmapTexture(bitmap);
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
		primative.mTextureBuffer = byteBuf.asFloatBuffer();
		primative.mTextureBuffer.put(coordinates);
		primative.mTextureBuffer.position(0);

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
		primative.mVertexBuffer = byteBuf.asFloatBuffer();
		primative.mVertexBuffer.put(vertices);
		primative.mVertexBuffer.position(0);
		
		primative.mR = 0.75f;
		primative.mG = 0.5f;
		primative.mB = 1.0f;
		primative.mA = 1.0f;
		
		mBackBuffer.add(primative);
		gl.glClearColor(0.75f, 0.5f, 0.3f, 1.0f);
		
		mGraphicsCallback.done();
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
		TextureManager texMgr = TextureManager.sharedManager();
		texMgr.loadTextures();
		copyToBuffer();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		final int size = mPrimBuffer.size();
		for (int i = 0;i < size;++i) {
			RenderPrimative primative = mPrimBuffer.get(i);
			gl.glVertexPointer(2, GL10.GL_FLOAT, 0, primative.mVertexBuffer);
	    	gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, primative.mTextureBuffer);
	   
			gl.glBindTexture(GL10.GL_TEXTURE_2D, primative.mTextureName);
	
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);		
		}

		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public void copyToBuffer() {
		synchronized(mBackBuffer) {
			//mBackBuffer.reset();
			//mRenderProvider.copyToBuffer(mBackBuffer);
			mPrimBuffer.reset();
			int size = mBackBuffer.size();
			for (int i = 0;i < size;++i) {
				mPrimBuffer.add(mBackBuffer.get(i).copy());
			}
		}
	}

	public void setGraphicsCallback(GraphicsCompletedCallback callback) {
		mGraphicsCallback = callback;
	}
	
	public void setRenderProvider(RenderProvider provider) {
		mRenderProvider = provider;
	}
}
