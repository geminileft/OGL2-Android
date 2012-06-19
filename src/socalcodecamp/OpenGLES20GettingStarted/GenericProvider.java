package socalcodecamp.OpenGLES20GettingStarted;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GenericProvider implements Runnable, RenderProvider {
	private RenderPrimative mPrimative;
	private PrimativeBuffer mPrimatives = new PrimativeBuffer();
	private PrimativeBuffer mCopyBuffer = new PrimativeBuffer();

    public void init() {
		mPrimative = new RenderPrimative();
		TextureManager texMgr = TextureManager.sharedManager();
		mPrimative.mTextureName = texMgr.resourceTexture(R.drawable.mg, mPrimative);
		
		final float coordinates[] = {    		
				// Mapping coordinates for the vertices
				0.0f, 1.0f,		// top left		(V2)
				0.0f, 0.0f,		// bottom left	(V1)
				1.0f, 1.0f,		// top right	(V4)
				1.0f, 0.0f		// bottom right	(V3)
		};
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(coordinates.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		mPrimative.mTextureBuffer = byteBuf.asFloatBuffer();
		mPrimative.mTextureBuffer.put(coordinates);
		mPrimative.mTextureBuffer.position(0);

		final int width = 64;
		final int height = 64;
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
		mPrimative.mVertexBuffer = byteBuf.asFloatBuffer();
		mPrimative.mVertexBuffer.put(vertices);
		mPrimative.mVertexBuffer.position(0);
		
		mPrimative.mR = 1.0f;
		mPrimative.mG = 0.0f;
		mPrimative.mB = 1.0f;
		mPrimative.mA = 1.0f;
    }
    
	public void run() {
		init();
		while (true) {
			mPrimatives.reset();
			mPrimatives.add(mPrimative);
			synchronized(mCopyBuffer) {
				mCopyBuffer.reset();
				for (int i = 0;i < mPrimatives.size();++i) {
					mCopyBuffer.add(mPrimatives.get(i).copy());
				}
			}
		}
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
