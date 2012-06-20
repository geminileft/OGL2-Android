package socalcodecamp.OpenGLES20GettingStarted;
import java.util.HashMap;
import java.util.Set;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import socalcodecamp.OpenGLES20GettingStarted.TERenderTarget.TEShaderType;
import android.opengl.GLES20;
import android.util.Log;


public class OGL2Renderer implements RenderConsumer {
	private RenderProvider mRenderProvider;
    private HashMap<TEShaderType, TEShaderProgram> mShaderPrograms = new HashMap<TEShaderType, TEShaderProgram>();
    private int mScreenFrameBuffer;
    private TERenderTarget mScreenTarget;
	private PrimativeBuffer mBackBuffer = new PrimativeBuffer();

	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        GLES20.glEnable(GL10.GL_BLEND);
		GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        String vertexSource;
        String fragmentSource;
		TEShaderProgram program;
		
		SystemManager sysMgr = SystemManager.sharedManager();
		
		vertexSource = sysMgr.readFileContents("colorbox.vs");
		fragmentSource = sysMgr.readFileContents("colorbox.fs");
		program = new TEShaderPolygon(vertexSource, fragmentSource);
	    program.addAttribute("aVertices");
		program.create();
		mShaderPrograms.put(TEShaderType.ShaderPolygon, program);
		
		vertexSource = sysMgr.readFileContents("texture.vs");
		fragmentSource = sysMgr.readFileContents("texture.fs");
		program = new TEShaderTexture(vertexSource, fragmentSource);
	    program.addAttribute("aVertices");
	    program.addAttribute("aTextureCoords");
		program.create();
		mShaderPrograms.put(TEShaderType.ShaderTexture, program);

		int[] params = new int[1];
		GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, params, 0);
		mScreenFrameBuffer = params[0];
		mScreenTarget = new TERenderTarget(mScreenFrameBuffer, 0.75f, 0.5f, 0.3f, 1.0f);

		TextureManager mgr = TextureManager.sharedManager();
		mgr.setVersion(2);
		mRenderProvider.renderInitialized();
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
    	mScreenTarget.setSize(width, height);
	}

	public void onDrawFrame(GL10 arg0) {
		TextureManager texMgr = TextureManager.sharedManager();
		texMgr.loadTextures();
		copyToBuffer();
		
        runTargetShaders(mScreenTarget);

	}

	public void copyToBuffer() {
		synchronized(mBackBuffer) {
			mBackBuffer.reset();
			mRenderProvider.copyToBuffer(mBackBuffer);
			synchronized(mScreenTarget) {
				mScreenTarget.resetPrimatives();
				mScreenTarget.addPrimatives(mBackBuffer);
				/*
				int size = mBackBuffer.size();
				for (int i = 0;i < size;++i) {
					RenderPrimative primative = mBackBuffer.get(i).copy();
					mScreenTarget.addPrimative(primative);
				}
				*/
			}
		}
	}

    public void runTargetShaders(TERenderTarget target) {

    	HashMap<TEShaderType, PrimativeBuffer> shaderData;
        TEShaderProgram rp;

        target.activate();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        shaderData = target.getShaderData();
        Set<TEShaderType> keySet;
		if (shaderData != null) {
			keySet = shaderData.keySet();
			for (TEShaderType type : keySet) {
				rp = mShaderPrograms.get(type);
				if (rp != null) {
					    rp.activate(target);
					    PrimativeBuffer primatives = shaderData.get(type);
					    rp.run(target, primatives);
				} else {
					Log.v("No Shader", "hrm");
				}
			}
		}
    }

	public void setRenderProvider(RenderProvider provider) {
		mRenderProvider = provider;
	}
}
