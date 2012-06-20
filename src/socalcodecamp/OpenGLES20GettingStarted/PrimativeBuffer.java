package socalcodecamp.OpenGLES20GettingStarted;


public class PrimativeBuffer {
	private final int MAX_SIZE = 1000;
	
	public RenderPrimative mRenderPrimatives[] = new RenderPrimative[MAX_SIZE];
	public int mTop = 0;

	public void add(RenderPrimative primative) {
		if (mTop < MAX_SIZE) {
			mRenderPrimatives[mTop] = primative;
			++mTop;
		}
	}
	
	public RenderPrimative get(int index) {
		RenderPrimative primative;
		if (index >= 0 && index < mTop)
			primative = mRenderPrimatives[index];
		else
			primative = null;
		return primative;
	}
}
