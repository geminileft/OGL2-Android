package socalcodecamp.OpenGLES20GettingStarted;

import java.util.HashMap;
import java.util.Set;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class OGL11Renderer implements RenderConsumer {

	private enum PrimativeType {
		PolyPrimative
		, TexPrimative
	};
	
	private HashMap<PrimativeType, PrimativeBuffer> mPrimBuffer = new HashMap<PrimativeType, PrimativeBuffer>();
	private PrimativeBuffer mBackBuffer = new PrimativeBuffer();
	private RenderProvider mRenderProvider;
	
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
		TextureManager mgr = TextureManager.sharedManager();
		mgr.setGL(gl);
		mgr.setVersion(1);
		mRenderProvider.renderInitialized();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);			
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glClearColor(0.75f, 0.5f, 0.3f, 1.0f);
		
		mPrimBuffer.put(PrimativeType.PolyPrimative, new PrimativeBuffer());
		mPrimBuffer.put(PrimativeType.TexPrimative, new PrimativeBuffer());
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
		
		textureRender(gl, mPrimBuffer.get(PrimativeType.TexPrimative));
		polygonRender(gl, mPrimBuffer.get(PrimativeType.PolyPrimative));
    	Log.v("OGLRenderer:onDrawFrame", "Here");

	}
	
	public void textureRender(GL10 gl, PrimativeBuffer buffer) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		final int size = buffer.size();
		for (int i = 0;i < size;++i) {
			RenderPrimative primative = buffer.get(i);
			gl.glVertexPointer(2, GL10.GL_FLOAT, 0, primative.mVertexBuffer);
	    	gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, primative.mTextureBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, primative.mTextureName);
			
			gl.glPushMatrix();
			gl.glTranslatef(primative.mX, primative.mY, 0.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		}
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public void polygonRender(GL10 gl, PrimativeBuffer buffer) {
		final int size = buffer.size();
		for (int i = 0;i < size;++i) {
			RenderPrimative primative = buffer.get(i);
			gl.glVertexPointer(2, GL10.GL_FLOAT, 0, primative.mVertexBuffer);
			gl.glColor4f(primative.mR, primative.mG, primative.mB, primative.mA);
			
			gl.glPushMatrix();
			gl.glTranslatef(primative.mX, primative.mY, 0.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			gl.glPopMatrix();
		}
	}
	
	public void copyToBuffer() {
		synchronized(mBackBuffer) {
			mBackBuffer.reset();
			mRenderProvider.copyToBuffer(mBackBuffer);
			synchronized(mPrimBuffer) {
				Set<PrimativeType> keys = mPrimBuffer.keySet();
				for (PrimativeType key : keys) {
					mPrimBuffer.get(key).reset();
				}
				
				int size = mBackBuffer.size();
				for (int i = 0;i < size;++i) {
					RenderPrimative primative = mBackBuffer.get(i).copy();
					PrimativeType ptype;
					if (primative.mTextureBuffer == null) {
						ptype = PrimativeType.PolyPrimative;
					} else {
						ptype = PrimativeType.TexPrimative;
					}
					
					mPrimBuffer.get(ptype).add(primative);
				}
			}
		}
	}

	public void setRenderProvider(RenderProvider provider) {
		mRenderProvider = provider;
	}
}
