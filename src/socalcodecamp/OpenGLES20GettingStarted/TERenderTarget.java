package socalcodecamp.OpenGLES20GettingStarted;

import java.util.HashMap;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class TERenderTarget {
	private final int MAX_BUFFER = 5;
	private int mTopBuffer = 0;
	
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
    	, ShaderKernel
    	, ShaderTransparentColor
    	, ShaderGrayscale
    };
    
    private PrimativeBuffer mBuffers[];
    
	HashMap<TEShaderType, PrimativeBuffer> mShaders = new HashMap<TEShaderType, PrimativeBuffer>();
	HashMap<TEShaderType, PrimativeBuffer> mShaderBuffer = new HashMap<TEShaderType, PrimativeBuffer>();

	public TERenderTarget(int frameBuffer, float r, float g, float b, float a) {
		mR = r;
		mG = g;
		mB = b;
		mA = a;
		mFrameBuffer = frameBuffer;
		
		mBuffers = new PrimativeBuffer[MAX_BUFFER];
		for (int i = 0;i < MAX_BUFFER;++i) {
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
	    mShaders.clear();
	    mTopBuffer = 0;
	    for (int i = 0;i < MAX_BUFFER;++i) {
	    	mBuffers[i].reset();
	    }
	}

	public void activate() {
	    GLES20.glViewport(0, 0, mFrameWidth, mFrameHeight);
	    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer);
	    GLES20.glClearColor(mR, mG, mB, mA);
	}

	public void addPrimatives(PrimativeBuffer buffer) {
		final int size = buffer.size();

		for (int i = 0;i < size;++i) {
			RenderPrimative primative = buffer.get(i).copy();
			TEShaderType type;
		    PrimativeBuffer primatives;
		    
		    if (primative.mTextureBuffer == null) {
		        type = TEShaderType.ShaderPolygon;
		    } else {
	            type = TEShaderType.ShaderTexture;
		    }
		    
		    if (mShaders.containsKey(type))
		        primatives = mShaders.get(type);
		    else {
		    	primatives = new PrimativeBuffer();
		    	mShaders.put(type, primatives);
		    }
		    primatives.add(primative);
		}
	}
	
	public void addPrimative(RenderPrimative primative) {
	    
		TEShaderType type;
	    PrimativeBuffer primatives;
	    
	    if (primative.mTextureBuffer == null) {
	        type = TEShaderType.ShaderPolygon;
	    } else {
            type = TEShaderType.ShaderTexture;
	    }
	    
	    if (mShaders.containsKey(type))
	        primatives = mShaders.get(type);
	    else {
	    	primatives = new PrimativeBuffer();
	    	//primatives = getNextBuffer();
	    	mShaders.put(type, primatives);
	    }
	    primatives.add(primative);
	}

	public HashMap<TEShaderType, PrimativeBuffer> getShaderData() {
	    return mShaders;
	}
	
	public PrimativeBuffer getNextBuffer() {
		return mBuffers[mTopBuffer++];
	}
}
