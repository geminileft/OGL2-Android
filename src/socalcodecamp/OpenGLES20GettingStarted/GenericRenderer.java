package socalcodecamp.OpenGLES20GettingStarted;

import android.content.Context;
import android.opengl.GLSurfaceView;


public class GenericRenderer {
		
	private GLSurfaceView.Renderer renderer;
	private PrimativeBuffer mRenderPrimatives = new PrimativeBuffer();
	
	public GLSurfaceView getGenericView() {
		SystemManager sysMgr = SystemManager.sharedManager();
		Context context = sysMgr.getContext();
        GLSurfaceView view  = new GLSurfaceView(context);
        switch(1) {
        case 1:
        	renderer = new OGL11Renderer();
            view.setEGLContextClientVersion(1);
       		view.setRenderer(renderer);
        	break;
        case 2:
        	renderer = new OGL2Renderer();
            view.setEGLContextClientVersion(2);
       		view.setRenderer(renderer);
        	break;
        }
		return view;
	}
	
	public void resetPrimatives() {
		mRenderPrimatives.reset();
	}
}
