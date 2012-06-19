package socalcodecamp.OpenGLES20GettingStarted;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.util.Log;

public class GenericProvider implements Runnable, RenderProvider {
	private RenderPrimative mTexPrimative;
	private RenderPrimative mPolyPrimative;
	private PrimativeBuffer mPrimatives = new PrimativeBuffer();
	private PrimativeBuffer mCopyBuffer = new PrimativeBuffer();

    public void init() {
    	Log.v("GenericProvider:init", "Here");
		mTexPrimative = new RenderPrimative();
		TextureManager texMgr = TextureManager.sharedManager();
		mTexPrimative.mTextureName = texMgr.resourceTexture(R.drawable.mg, mTexPrimative);
		
		final float coordinates[] = {    		
				// Mapping coordinates for the vertices
				0.0f, 1.0f,		// top left		(V2)
				0.0f, 0.0f,		// bottom left	(V1)
				1.0f, 1.0f,		// top right	(V4)
				1.0f, 0.0f		// bottom right	(V3)
		};
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(coordinates.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		mTexPrimative.mTextureBuffer = byteBuf.asFloatBuffer();
		mTexPrimative.mTextureBuffer.put(coordinates);
		mTexPrimative.mTextureBuffer.position(0);

		final int width = 240;
		final int height = 240;
		final float leftX = -(float)width / 2;
		final float rightX = leftX + width;
		final float bottomY = -(float)height / 2;
		final float topY = bottomY + height;

		final float vertices[] = {
	    		leftX, bottomY
	    		, leftX, topY
	    		, rightX, bottomY
	    		, rightX, topY
	    };
	    
   		byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		FloatBuffer vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		mTexPrimative.mVertexBuffer = vertexBuffer;
		
		mTexPrimative.mX = -120.0f;
		mTexPrimative.mY = 0.0f;
		
		mPolyPrimative = new RenderPrimative();
		mPolyPrimative.mVertexBuffer = vertexBuffer;
		mPolyPrimative.mR = 1.0f;
		mPolyPrimative.mG = 0.0f;
		mPolyPrimative.mB = 0.0f;
		mPolyPrimative.mA = 1.0f;
		mPolyPrimative.mX = 120.0f;
		mPolyPrimative.mY = 0.0f;
		
    }
    
	public void run() {
		init();
		while (true) {
			mPrimatives.reset();
			frame();				
			synchronized(mCopyBuffer) {
				mCopyBuffer.reset();
				for (int i = 0;i < mPrimatives.size();++i) {
					mCopyBuffer.add(mPrimatives.get(i).copy());
				}
			}
		}
	}
	
	public void frame() {
		mPrimatives.add(mTexPrimative);
		mPrimatives.add(mPolyPrimative);
    	Log.v("GenericProvider:frame", "Here");
	}
	
	public void copyToBuffer(PrimativeBuffer buffer) {
		synchronized(mCopyBuffer) {
			for (int i = 0;i < mCopyBuffer.size();++i) {
				buffer.add(mCopyBuffer.get(i).copy());
			}
		}
		// TODO Auto-generated method stub
	}
	
	public void renderInitialized() {
        Thread thread = new Thread(this);
        thread.start();		
	}
}
