package socalcodecamp.OpenGLES20GettingStarted;

import android.content.Context;
import android.opengl.GLSurfaceView;


public class GenericRenderer {
	private final int RENDER_VERSION = 1;
	
	private RenderConsumer mRenderer;
	
	public GenericRenderer() {
		super();
        switch(RENDER_VERSION) {
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
        switch(RENDER_VERSION) {
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
	
	public void setRenderProvider(RenderProvider provider) {
		mRenderer.setRenderProvider(provider);
	}
}
