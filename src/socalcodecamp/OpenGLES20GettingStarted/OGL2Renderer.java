package socalcodecamp.OpenGLES20GettingStarted;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;


public class OGL2Renderer implements RenderConsumer {
	private GraphicsCompletedCallback mGraphicsCallback;
	//private RenderProvider mRenderProvider;
	
	public void onDrawFrame(GL10 arg0) {
		// TODO Auto-generated method stub
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT);

	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);		
	}

	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        GLES20.glEnable(GL10.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		mGraphicsCallback.done();
	}

	public void setGraphicsCallback(GraphicsCompletedCallback callback) {
		mGraphicsCallback = callback;
	}

	public void setRenderProvider(RenderProvider provider) {
		//mRenderProvider = provider;
	}
}
