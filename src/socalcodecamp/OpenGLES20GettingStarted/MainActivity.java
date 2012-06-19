package socalcodecamp.OpenGLES20GettingStarted;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemManager sysMgr = SystemManager.sharedManager();
        sysMgr.setContext(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GenericRenderer renderer = new GenericRenderer();
        GenericProvider provider = new GenericProvider();
        renderer.setRenderProvider(provider);
        setContentView(renderer.getGenericView());
    }

}