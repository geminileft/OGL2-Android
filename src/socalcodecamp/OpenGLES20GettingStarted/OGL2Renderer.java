package socalcodecamp.OpenGLES20GettingStarted;
import java.util.HashMap;
import java.util.Map.Entry;
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
	private int mCurrentTarget = -1;
	private TextureManager mTexMgr;
	
	private TEShaderTexture mTexProgram;
	private TEShaderPolygon mPolyProgram;

	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        GLES20.glEnable(GL10.GL_BLEND);
		GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        String vertexSource;
        String fragmentSource;
		
		SystemManager sysMgr = SystemManager.sharedManager();
		mTexMgr = TextureManager.sharedManager();
		
		vertexSource = sysMgr.readFileContents("colorbox.vs");
		fragmentSource = sysMgr.readFileContents("colorbox.fs");
		mPolyProgram = new TEShaderPolygon(vertexSource, fragmentSource);
	    mPolyProgram.addAttribute("aVertices");
		mPolyProgram.create();
		mShaderPrograms.put(TEShaderType.ShaderPolygon, mPolyProgram);
		
		vertexSource = sysMgr.readFileContents("texture.vs");
		fragmentSource = sysMgr.readFileContents("texture.fs");
		mTexProgram = new TEShaderTexture(vertexSource, fragmentSource);
		mTexProgram.addAttribute("aVertices");
		mTexProgram.addAttribute("aTextureCoords");
		mTexProgram.create();
		mShaderPrograms.put(TEShaderType.ShaderTexture, mTexProgram);

		int[] params = new int[1];
		GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, params, 0);
		mScreenFrameBuffer = params[0];
		mScreenTarget = new TERenderTarget(mScreenFrameBuffer, 0.75f, 0.5f, 0.3f, 1.0f);

		mRenderProvider.renderInitialized();
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
    	mScreenTarget.setSize(width, height);
	}

	public void onDrawFrame(GL10 arg0) {
		mTexMgr.loadTextures();
		copyToBuffer();
		
        runTargetShaders(mScreenTarget);

	}

	public void copyToBuffer() {
		synchronized(mBackBuffer) {
			mBackBuffer.mTop = 0;
			mRenderProvider.copyToBuffer(mBackBuffer);
			synchronized(mScreenTarget) {
				mScreenTarget.resetPrimatives();
				mScreenTarget.addPrimatives(mBackBuffer);
			}
		}
	}

    public void runTargetShaders(TERenderTarget target) {
    	HashMap<TEShaderType, PrimativeBuffer> shaderData;
        TEShaderProgram rp;
        TEShaderType type;

        final int frameBuffer = target.getFrameBuffer();
        if (mCurrentTarget != frameBuffer) {
            target.activate();
            mCurrentTarget = frameBuffer;
        }
        
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        shaderData = target.getShaderData();
		if (shaderData != null) {
			for (Entry<TEShaderType, PrimativeBuffer> entry : shaderData.entrySet()) {
				rp = null;
				type = entry.getKey();
				if (type == TEShaderType.ShaderPolygon)
					rp = mPolyProgram;
				else if (type == TEShaderType.ShaderTexture)
					rp = mTexProgram;
				if (rp != null) {
					    rp.activate(target);
					    rp.run(target, entry.getValue());
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
