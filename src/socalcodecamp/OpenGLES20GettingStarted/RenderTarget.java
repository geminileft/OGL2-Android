package socalcodecamp.OpenGLES20GettingStarted;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class RenderTarget {
	private final int MAX_BUFFERS = 2;
	
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
    
    private PrimativeBuffer mBuffers[] = new PrimativeBuffer[MAX_BUFFERS];
    
	ShaderData mShaderData = new ShaderData();
	
	public RenderTarget(int frameBuffer, float r, float g, float b, float a) {
		mR = r;
		mG = g;
		mB = b;
		mA = a;
		mFrameBuffer = frameBuffer;
		for (int i = 0;i < MAX_BUFFERS;++i) {
			mBuffers[i] = new PrimativeBuffer();
		}

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
		mShaderData.reset();
		for (int i = 0;i < MAX_BUFFERS;++i) {
			mBuffers[i].mTop = 0;
		}
	}

	public void activate() {
	    GLES20.glViewport(0, 0, mFrameWidth, mFrameHeight);
	    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer);
	    GLES20.glClearColor(mR, mG, mB, mA);
	}

	public void addPrimatives(PrimativeBuffer buffer) {
		final int size = buffer.mTop;

	    PrimativeBuffer polyPrimatives = mBuffers[0];
	    PrimativeBuffer texPrimatives = mBuffers[1];
	    
	    polyPrimatives.mTop = 0;
	    texPrimatives.mTop = 0;

	    for (int i = 0;i < size;++i) {
			RenderPrimative primative = buffer.get(i).copy();
		    
		    if (primative.mTextureBuffer == null) {
		    	polyPrimatives.add(primative);
		    } else {
		    	texPrimatives.add(primative);
		    }

		}
	    if (texPrimatives.mTop > 0) {
	    	mShaderData.add(TEShaderType.ShaderTexture, texPrimatives);
	    }
	    if (polyPrimatives.mTop > 0) {
	    	mShaderData.add(TEShaderType.ShaderPolygon, polyPrimatives);
	    }		    
	}
		
	public ShaderData getShaderData() {
		return mShaderData;
	}
}
