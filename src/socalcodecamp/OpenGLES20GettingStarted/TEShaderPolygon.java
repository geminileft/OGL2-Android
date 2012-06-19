package socalcodecamp.OpenGLES20GettingStarted;

import java.util.LinkedList;

import android.opengl.GLES20;

public class TEShaderPolygon extends TEShaderProgram {

	public TEShaderPolygon(String vertexSource, String fragmentSource) {
		super(vertexSource, fragmentSource);
	}

	public void run(TERenderTarget target, LinkedList<RenderPrimative> primatives) {
	    activate(target);
	    int vertexHandle = GLES20.glGetAttribLocation(mProgramId, "aVertices");
	    int colorHandle = GLES20.glGetUniformLocation(mProgramId, "aColor");
	    int posHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
	    
	    RenderPrimative p;
	    
	    int primativeCount = primatives.size();
	    for (int i = 0;i < primativeCount;++i) {
	        p = primatives.get(i);
	        
	        GLES20.glVertexAttribPointer(vertexHandle, 2, GLES20.GL_FLOAT, false, 0, p.mVertexBuffer);
	        GLES20.glUniform4f(colorHandle, p.mR, p.mG, p.mB, p.mA);
	        GLES20.glVertexAttrib2f(posHandle, p.mX, p.mY);
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, p.vertexCount);        
	    }
	    deactivate();
	}
}
