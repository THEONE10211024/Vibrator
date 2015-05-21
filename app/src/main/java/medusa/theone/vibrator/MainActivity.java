package medusa.theone.vibrator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import medusa.theone.vibrator.lib.Vibrator;


public class MainActivity extends ActionBarActivity {

    private Vibrator vibrator;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LinearLayout l = (LinearLayout) findViewById(R.id.linearLayout);
//        WaveProgress waveProgress = new WaveProgress(this);
//        l.addView(waveProgress);
        vibrator = (Vibrator)findViewById(R.id.myvibrators);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "startAnimation", Toast.LENGTH_SHORT).show();
                vibrator.startAnimation();
            }
        });

//        img = (ImageView) findViewById(R.id.imageView);
//        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(img,"y",100,350);
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        valueAnimator.setDuration(1000);
//        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
//        valueAnimator.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
