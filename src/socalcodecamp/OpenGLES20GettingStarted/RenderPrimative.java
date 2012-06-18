package socalcodecamp.OpenGLES20GettingStarted;

import java.nio.FloatBuffer;

public class RenderPrimative {

	public FloatBuffer mVertexBuffer;
	public FloatBuffer mTextureBuffer;
	
	public float mR;
	public float mG;
	public float mB;
	public float mA;
	
	public int mTextureName;
	
	public RenderPrimative copy() {
		RenderPrimative primative = new RenderPrimative();
		primative.mR = mR;
		primative.mG = mG;
		primative.mB = mB;
		primative.mA = mA;
		
		primative.mVertexBuffer = mVertexBuffer;
		primative.mTextureBuffer = mTextureBuffer;
		primative.mTextureName = mTextureName;
		
		return primative;
	}
}
