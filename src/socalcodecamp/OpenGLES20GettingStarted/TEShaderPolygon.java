package socalcodecamp.OpenGLES20GettingStarted;

import android.opengl.GLES20;

public class TEShaderPolygon extends TEShaderProgram {
	private int mVertexHandle;
    private int mColorHandle;
    private int mPosHandle;

	public TEShaderPolygon(String vertexSource, String fragmentSource) {
		super(vertexSource, fragmentSource);
	}

	public void run(TERenderTarget target, PrimativeBuffer primatives) {
	    
	    RenderPrimative p;
	    
	    int primativeCount = primatives.size();
	    for (int i = 0;i < primativeCount;++i) {
	        p = primatives.get(i);
	        
	        GLES20.glVertexAttribPointer(mVertexHandle, 2, GLES20.GL_FLOAT, false, 0, p.mVertexBuffer);
	        GLES20.glUniform4f(mColorHandle, p.mR, p.mG, p.mB, p.mA);
	        GLES20.glVertexAttrib2f(mPosHandle, p.mX, p.mY);
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, p.vertexCount);        
	    }
	    deactivate();
	}

	public final void create() {
		super.create();
	    mVertexHandle = GLES20.glGetAttribLocation(mProgramId, "aVertices");
	    mColorHandle = GLES20.glGetUniformLocation(mProgramId, "aColor");
	    mPosHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
	}

}
