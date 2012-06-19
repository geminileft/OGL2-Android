package socalcodecamp.OpenGLES20GettingStarted;

import java.util.LinkedList;

import android.opengl.GLES20;
import android.util.Log;

public abstract class TEShaderProgram {
	private String mVertexSource;
	private String mFragmentSource;
	public int mProgramId;
	private LinkedList<String> mAttributes = new LinkedList<String>();
	
	TEShaderProgram(String vertexSource, String fragmentSource) {
		setVertexSource(vertexSource);
		setFragmentSource(fragmentSource);
	}
	
	public final void setVertexSource(String source) {
		mVertexSource = source;
	}
	
	public final void setFragmentSource(String source) {
		mFragmentSource = source;
	}

    public final void create() {
        mProgramId = GLES20.glCreateProgram();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexSource);
        GLES20.glAttachShader(mProgramId, vertexShader);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentSource);
        GLES20.glAttachShader(mProgramId, fragmentShader);
        GLES20.glLinkProgram(mProgramId);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(mProgramId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e("Error", "Could not link program: ");
            Log.e("Error", GLES20.glGetProgramInfoLog(mProgramId));
            GLES20.glDeleteProgram(mProgramId);
            mProgramId = 0;
        }
    }

    public final void activate(TERenderTarget target) {
        
        GLES20.glUseProgram(mProgramId);
        
        int count = mAttributes.size();
        String attribute = "";
        if (count > 0) {
        	int i = 0;
        	while (i < count) {
	        	attribute = mAttributes.get(i);
	            int handle = GLES20.glGetAttribLocation(mProgramId, attribute);
	            GLES20.glEnableVertexAttribArray(handle);
	        	++i;
        	}
        }
        
        //target.activate();
        int mProjHandle  = GLES20.glGetUniformLocation(mProgramId, "uProjectionMatrix");
        int mViewHandle = GLES20.glGetUniformLocation(mProgramId, "uViewMatrix");
        GLES20.glUniformMatrix4fv(mProjHandle, 1, false, target.getProjMatrix(), 0);
        GLES20.glUniformMatrix4fv(mViewHandle, 1, false, target.getViewMatrix(), 0);
    }
    
    public final void addAttribute(String attribute) {
    	mAttributes.add(attribute);
    }
    
    public abstract void run(TERenderTarget target, LinkedList<RenderPrimative> primatives);
    
    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, source);
        checkGlError("glShaderSource");
        GLES20.glCompileShader(shader);
        checkGlError("glCompileShader");
        return shader;
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
        	String errorMsg = GLES20.glGetProgramInfoLog(mProgramId);
            Log.e("info", op + ": glError " + error + errorMsg);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

	public final void deactivate() {
		int size = mAttributes.size();
	    if (size > 0) {
	        for (int i = 0;i < size;++i) {
	            int handle = GLES20.glGetAttribLocation(mProgramId, mAttributes.get(i));
	            GLES20.glDisableVertexAttribArray(handle);
	            checkGlError("glDisableVertexAttribArray");
	        }
	    }
	}
}
