package socalcodecamp.OpenGLES20GettingStarted;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity implements Runnable {
    /** Called when the activity is first created. */
	private GenericRenderer mRenderer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemManager sysMgr = SystemManager.sharedManager();
        sysMgr.setContext(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mRenderer = new GenericRenderer();
        setContentView(mRenderer.getGenericView());
        Thread thread = new Thread(this);
        thread.start();
    }

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			mRenderer.resetPrimatives();
			//mRenderer.addRenderPrimative(primative);
			Log.v("Here", "here");			
		}
	}
}