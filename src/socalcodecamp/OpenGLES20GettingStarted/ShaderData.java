package socalcodecamp.OpenGLES20GettingStarted;

import socalcodecamp.OpenGLES20GettingStarted.TERenderTarget.TEShaderType;

public class ShaderData {
	private int MAX_DATA = 2;
	private int mTop = 0;
	
	private TEShaderType mTypes[] = new TEShaderType[MAX_DATA];
	private PrimativeBuffer mBuffers[] = new PrimativeBuffer[MAX_DATA];
	
	public void add(TEShaderType shaderType, PrimativeBuffer buffer) {
		mTypes[mTop] = shaderType;
		mBuffers[mTop] = buffer;
		++mTop;
	}
	
	public int size() {
		return mTop;
	}
	
	public void reset() {
		mTop = 0;
	}
	
	public TEShaderType getType(int index) {
		return mTypes[index];
	}
	
	public PrimativeBuffer getBuffer(int index) {
		return mBuffers[index];
	}
}
