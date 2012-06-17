package socalcodecamp.OpenGLES20GettingStarted;


public class PrimativeBuffer {
	private final int MAX_SIZE = 1000;
	
	private RenderPrimative mRenderPrimatives[] = new RenderPrimative[MAX_SIZE];
	private int mTop = 0;

	public void add(RenderPrimative primative) {
		if (mTop < MAX_SIZE) {
			mRenderPrimatives[mTop] = primative;
			++mTop;
		}
	}
	
	public void reset() {
		mTop = 0;
	}

}
