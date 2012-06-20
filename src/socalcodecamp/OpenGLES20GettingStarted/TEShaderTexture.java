package socalcodecamp.OpenGLES20GettingStarted;

import java.util.LinkedList;

import android.opengl.GLES20;

public class TEShaderTexture extends TEShaderProgram {

	private int mPositionHandle;
	private int mTextureHandle;
	private int mCoordsHandle;
	private int mAlphaHandle;
	
	public TEShaderTexture(String vertexSource, String fragmentSource) {
		super(vertexSource, fragmentSource);
	}

	public void run(TERenderTarget target, LinkedList<RenderPrimative> primatives) {
	    activate(target);
	    
	    int primativeCount = primatives.size();
	    for (int i = 0;i < primativeCount;++i) {
	    	RenderPrimative primative = primatives.get(i);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, primative.mTextureName);
	        GLES20.glVertexAttrib2f(mCoordsHandle, primative.mX, primative.mY);
	        GLES20.glVertexAttribPointer(mTextureHandle, 2, GLES20.GL_FLOAT, false, 0, primative.mTextureBuffer);
	        GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, primative.mVertexBuffer);
	        GLES20.glUniform1f(mAlphaHandle, 1.0f);
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, primatives.get(i).vertexCount);
	    }
	    deactivate();
	}
	
	public final void create() {
		super.create();
	    mPositionHandle = GLES20.glGetAttribLocation(mProgramId, "aVertices");
	    mTextureHandle = GLES20.glGetAttribLocation(mProgramId, "aTextureCoords");
	    mCoordsHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
	    mAlphaHandle = GLES20.glGetUniformLocation(mProgramId, "uAlpha");		
	}
}
