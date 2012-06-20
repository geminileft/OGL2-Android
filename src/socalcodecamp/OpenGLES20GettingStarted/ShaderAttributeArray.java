package socalcodecamp.OpenGLES20GettingStarted;

public class ShaderAttributeArray {
	private final int MAX_ATTRIBUTES = 10;
	private int mTop = 0;
	private String mValues[] = new String[MAX_ATTRIBUTES];

	public void add(String attribute) {
		mValues[mTop] = attribute;
		++mTop;
	}
	
	public String get(int i) {
		String result = "";
		if ((i >= 0) && (i < MAX_ATTRIBUTES)) {
			result = mValues[i];
		}
		return result;
	}
	
	public int size() {
		return mTop;
	}
}
