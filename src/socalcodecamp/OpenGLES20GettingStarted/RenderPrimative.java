package socalcodecamp.OpenGLES20GettingStarted;

import java.nio.FloatBuffer;

public class RenderPrimative {

	public FloatBuffer mVertexBuffer;
	public FloatBuffer mTextureBuffer;
	
	public float mR;
	public float mG;
	public float mB;
	public float mA;
	
	public float mX;
	public float mY;
	
	public int vertexCount = 4;
	
	public int mTextureName;
	
	public RenderPrimative copy() {
		RenderPrimative primative = new RenderPrimative();
		primative.mR = mR;
		primative.mG = mG;
		primative.mB = mB;
		primative.mA = mA;
		
		primative.mX = mX;
		primative.mY = mY;
		
		primative.mVertexBuffer = mVertexBuffer;
		primative.mTextureBuffer = mTextureBuffer;
		primative.mTextureName = mTextureName;
		
		return primative;
	}
}
