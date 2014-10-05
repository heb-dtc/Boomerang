package com.heb.boomerang;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class BoomerangActivity extends Activity {

    private static final String TYPE_TEXT_MARKER = "text";
    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boomerang);
        messageView = (TextView) findViewById(R.id.message_view);

        if (shouldBoomerang() ) {
            messageView.setVisibility(View.GONE);
            parseAndBoomerangIntent(getIntent());
        } else {
            messageView.setVisibility(View.VISIBLE);
            messageView.setText(R.string.nothing_to_do);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        String intentAction = intent.getAction();
        String intentType = intent.getType();

        if (intentAction == Intent.ACTION_SEND && intentType.contains(TYPE_TEXT_MARKER)) {
            return true;
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

        startActivity(chooser);
    }
}
