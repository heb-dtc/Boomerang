package com.heb.boomerang;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class BoomerangActivity extends Activity {

    private static final String TYPE_TEXT_MARKER = "text";

    private TextView contentView;
    private Button boomerangButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boomerang);
        contentView = (TextView) findViewById(R.id.message_view);
        boomerangButton = (Button) findViewById(R.id.boomerang_button);

        boomerangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseAndBoomerangIntent(getIntent());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (shouldBoomerang() ) {
            boomerangButton.setEnabled(true);
            String data = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            contentView.setText(data);
        } else {
            boomerangButton.setEnabled(false);
            contentView.setText(R.string.nothing_to_do);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if(level == TRIM_MEMORY_UI_HIDDEN) {
            setIntent(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.boomerang, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean shouldBoomerang() {
        Intent intent = getIntent();

        if(intent != null) {
            String intentAction = intent.getAction();
            String intentType = intent.getType();

            if (intentAction == Intent.ACTION_SEND && intentType.contains(TYPE_TEXT_MARKER)) {
                return true;
            }
        }
        return false;
    }

    private void parseAndBoomerangIntent(Intent intent) {
        Uri uri = buildUriFromExtra(intent);
        if (uri != null) {
            Intent deepLinkIntent = createDeepLinkIntent(uri);
            sendIntent(deepLinkIntent);
        }
    }

    private Uri buildUriFromExtra(Intent intent) {
        String data = intent.getStringExtra(Intent.EXTRA_TEXT);
        return Uri.parse(data);
    }

    private Intent createDeepLinkIntent(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void sendIntent(Intent intent) {
        String title = getResources().getString(R.string.chooser_name);
        Intent chooser = Intent.createChooser(intent, title);

        startActivityForResult(chooser, 1551551);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1551551) {
            Log.e("Boomerang", "back from dialog chooser");
        }
    }
}
