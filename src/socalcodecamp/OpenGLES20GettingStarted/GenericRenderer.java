package socalcodecamp.OpenGLES20GettingStarted;

import android.content.Context;
import android.opengl.GLSurfaceView;


public class GenericRenderer {
	
	private RenderConsumer mRenderer;
	private PrimativeBuffer mRenderPrimatives = new PrimativeBuffer();
	
	public GenericRenderer() {
		super();
        switch(1) {
        case 1:
        	mRenderer = new OGL11Renderer();
        	break;
        case 2:
        	mRenderer = new OGL2Renderer();
        	break;
        }
	}

	public GLSurfaceView getGenericView() {
		SystemManager sysMgr = SystemManager.sharedManager();
		Context context = sysMgr.getContext();
        GLSurfaceView view  = new GLSurfaceView(context);
        switch(1) {
        case 1:
            view.setEGLContextClientVersion(1);
       		view.setRenderer(mRenderer);
        	break;
        case 2:
            view.setEGLContextClientVersion(2);
       		view.setRenderer(mRenderer);
        	break;
        }
		return view;
	}
	
	public void resetPrimatives() {
		mRenderPrimatives.reset();
	}
	
	public void setGraphicsCallback(GraphicsCompletedCallback callback) {
		mRenderer.setGraphicsCallback(callback);
	}
}
