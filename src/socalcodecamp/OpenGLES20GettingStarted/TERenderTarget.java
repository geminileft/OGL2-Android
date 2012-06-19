package socalcodecamp.OpenGLES20GettingStarted;

import java.util.HashMap;
import java.util.LinkedList;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class TERenderTarget {
	private int mFrameBuffer;
	private int mFrameWidth;
    private int mFrameHeight;
    
    private float mR;
    private float mG;
    private float mB;
    private float mA;
    
    float mProjMatrix[] = new float[16];
    float mViewMatrix[] = new float[16];
    public enum TEShaderType {
    	ShaderTexture
    	, ShaderPolygon
    	, ShaderKernel
    	, ShaderTransparentColor
    	, ShaderGrayscale
    };
    
	HashMap<TEShaderType, LinkedList<RenderPrimative>> mShaders = new HashMap<TEShaderType, LinkedList<RenderPrimative>>();
	HashMap<TEShaderType, LinkedList<RenderPrimative>> mShaderBuffer = new HashMap<TEShaderType, LinkedList<RenderPrimative>>();

	public TERenderTarget(int frameBuffer, float r, float g, float b, float a) {
		mR = r;
		mG = g;
		mB = b;
		mA = a;
		mFrameBuffer = frameBuffer;
	}

	public void setSize(int width, int height) {
	    mFrameWidth = width;
	    mFrameHeight = height;
	    
	    float zDepth;
	    float ratio;
	    
	    zDepth = (float)mFrameHeight / 2;
	    ratio = (float)mFrameWidth/(float)mFrameHeight;
	    
	    Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1.0f, 1000.0f);
	    Matrix.setIdentityM(mViewMatrix, 0);
	    Matrix.translateM(mViewMatrix, 0, 0.0f, 0.0f, -zDepth);    
	}

	public int getFrameBuffer() {
		return mFrameBuffer;
	}

	public int getFrameWidth()  {
		return mFrameWidth;
	}

	public int getFrameHeight() {
	    return mFrameHeight;
	}

	public void resetPrimatives() {
	    mShaders.clear();
	}

	public void activate() {
	    GLES20.glViewport(0, 0, mFrameWidth, mFrameHeight);
	    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer);
	    GLES20.glClearColor(mR, mG, mB, mA);
	}

	public float[] getProjMatrix() {
	    return mProjMatrix;
	}

	public float[] getViewMatrix() {
	    return mViewMatrix;
	}

	public void addPrimative(RenderPrimative primative) {
	    
		TEShaderType type;
	    LinkedList<RenderPrimative> primatives;
	    
	    if (primative.mTextureBuffer == null) {
	        type = TEShaderType.ShaderPolygon;
	    } else {
            type = TEShaderType.ShaderTexture;
	    }
	    
	    if (mShaders.containsKey(type))
	        primatives = mShaders.get(type);
	    else {
	    	primatives = new LinkedList<RenderPrimative>();
	    	mShaders.put(type, primatives);
	    }
	    primatives.add(primative);
	}

	public HashMap<TEShaderType, LinkedList<RenderPrimative>> getShaderData() {
		/*
		Log.v("getShaderData", "trying to locate error");
		synchronized(mShaderBuffer) {
			mShaderBuffer.clear();
			for (TEShaderType key : mShaders.keySet()) {
				LinkedList<TERenderPrimative> list = new LinkedList<TERenderPrimative>();
				for (TERenderPrimative primative : mShaders.get(key)) {
					list.add(primative);
				}
				mShaderBuffer.put(key, list);
			}
		}
		mShaders.clear();
		*/
	    return mShaders;
	}
}
