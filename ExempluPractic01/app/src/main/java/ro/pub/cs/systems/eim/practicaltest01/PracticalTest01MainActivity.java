package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private TextView pressMeTextView = null, pressMeTooTextView = null;
    private Button1ClickListener pressMeButtonClickListener = new Button1ClickListener();
    private Button2ClickListener pressMeTooButtonClickListener = new Button2ClickListener();

    private NavigateClickListener navigateClickListener = new NavigateClickListener();

    private boolean serviceStatus;
    private IntentFilter startedServiceIntentFilter;

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    private class Button1ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int old = Integer.parseInt(pressMeTextView.getText().toString());

            pressMeTextView.setText(Integer.toString(old + 1));

            int first = Integer.parseInt(pressMeTextView.getText().toString());
            int second = Integer.parseInt(pressMeTooTextView.getText().toString());

            if (first + second > Constants.Prag && serviceStatus == false) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra("firstNumber", first);
                intent.putExtra("secondNumber", second);
                startService(intent);
                serviceStatus = true;
            }

        }
    }

    private class Button2ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int old = Integer.parseInt(pressMeTooTextView.getText().toString());

            pressMeTooTextView.setText(Integer.toString(old + 1));
        }
    }

    private class NavigateClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
            intent.putExtra("totalClicks", Integer.parseInt(pressMeTextView.getText().toString()) + Integer.parseInt(pressMeTooTextView.getText().toString()));
            startActivityForResult(intent, 100);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pressMeTextView = (TextView) findViewById(R.id.press_me_text_view);
//        pressMeTextView.setText(Integer.toString(0));
        pressMeTooTextView = (TextView) findViewById(R.id.press_me_too_text_view);
//        pressMeTooTextView.setText(Integer.toString(0));

        if (savedInstanceState != null) {
            pressMeTextView.setText(savedInstanceState.getString("leftText", "0"));
            pressMeTooTextView.setText(savedInstanceState.getString("rightText", "0"));
        }
        else {
            pressMeTextView.setText("0");
            pressMeTooTextView.setText("0");
        }

        Button pressMeButton = (Button) findViewById(R.id.press_me_button);
        pressMeButton.setOnClickListener(pressMeButtonClickListener);
        Button pressMeTooButton = (Button) findViewById(R.id.press_me_too_button);
        pressMeTooButton.setOnClickListener(pressMeTooButtonClickListener);

        Button navigateButton = (Button) findViewById(R.id.second_activity_button);
        navigateButton.setOnClickListener(navigateClickListener);

        serviceStatus = false;

        for (int i = 0; i < Constants.actionTypes.length; i++) {
            startedServiceIntentFilter.addAction(Constants.actionTypes[i]);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 100:
                Toast.makeText(getApplication(), "First Activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString("leftText", pressMeTextView.getText().toString());
        bundle.putString("rightText", pressMeTooTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        pressMeTextView.setText(bundle.getString("leftText", "0"));
        pressMeTooTextView.setText(bundle.getString("rightText", "0"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, startedServiceIntentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test01_main, menu);
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
