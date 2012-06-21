package socalcodecamp.OpenGLES20GettingStarted;

import java.util.HashMap;

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
    
    public float mProjMatrix[] = new float[16];
    public float mViewMatrix[] = new float[16];
    public enum TEShaderType {
    	ShaderTexture
    	, ShaderPolygon
    };
    
	HashMap<TEShaderType, PrimativeBuffer> mShaders = new HashMap<TEShaderType, PrimativeBuffer>();
	HashMap<TEShaderType, PrimativeBuffer> mShaderBuffer = new HashMap<TEShaderType, PrimativeBuffer>();

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

	public void addPrimatives(PrimativeBuffer buffer) {
		final int size = buffer.mTop;

	    PrimativeBuffer polyPrimatives = new PrimativeBuffer();
	    PrimativeBuffer texPrimatives = new PrimativeBuffer();

	    for (int i = 0;i < size;++i) {
			RenderPrimative primative = buffer.get(i).copy();
		    
		    if (primative.mTextureBuffer == null) {
		    	polyPrimatives.add(primative);
		    } else {
		    	texPrimatives.add(primative);
		    }

		}
	    if (texPrimatives.mTop > 0) {
	    	mShaders.put(TEShaderType.ShaderTexture, texPrimatives);
	    }
	    if (polyPrimatives.mTop > 0) {
	    	mShaders.put(TEShaderType.ShaderPolygon, polyPrimatives);
	    }		    
	}
		
	public HashMap<TEShaderType, PrimativeBuffer> getShaderData() {
	    return mShaders;
	}

}
