package socalcodecamp.OpenGLES20GettingStarted;

import android.opengl.GLSurfaceView;

public interface RenderConsumer extends GLSurfaceView.Renderer {
	public void setGraphicsCallback(GraphicsCompletedCallback callback);
	public void setRenderProvider(RenderProvider provider);
}
