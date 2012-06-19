package socalcodecamp.OpenGLES20GettingStarted;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OGL11Renderer implements RenderConsumer {

	private PrimativeBuffer mPrimBuffer = new PrimativeBuffer();
	private PrimativeBuffer mBackBuffer = new PrimativeBuffer();
	private RenderProvider mRenderProvider;
	
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);			
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		TextureManager mgr = TextureManager.sharedManager();
		mgr.setGL(gl);
		mgr.setVersion(1);

		gl.glClearColor(0.75f, 0.5f, 0.3f, 1.0f);
		
		mRenderProvider.renderInitialized();
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
			if (true) {
				mBackBuffer.reset();
				mRenderProvider.copyToBuffer(mBackBuffer);
			}
			mPrimBuffer.reset();
			int size = mBackBuffer.size();
			for (int i = 0;i < size;++i) {
				mPrimBuffer.add(mBackBuffer.get(i).copy());
			}
		}
	}

	public void setRenderProvider(RenderProvider provider) {
		mRenderProvider = provider;
	}
}
