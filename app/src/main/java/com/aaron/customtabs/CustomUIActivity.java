package com.aaron.customtabs;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

public class CustomUIActivity extends AppCompatActivity {

    private CheckBox mCheckBoxMenu;
    private CheckBox mCheckBoxTitle;
    private CheckBox mCheckBoxBack;
    private CheckBox mCheckBoxHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCheckBoxMenu = (CheckBox) findViewById(R.id.cb_menu);
        mCheckBoxTitle = (CheckBox) findViewById(R.id.cb_title);
        mCheckBoxBack = (CheckBox) findViewById(R.id.cb_back);
        mCheckBoxHide = (CheckBox) findViewById(R.id.cb_hide);

        findViewById(R.id.launch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTabs();
            }
        });
    }

    private static Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private void openCustomTabs() {
        String url = "https://github.com";

        int color = ContextCompat.getColor(this, R.color.colorPrimary);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder()
                .setToolbarColor(color);

        if (mCheckBoxMenu.isChecked()) {
            builder.addMenuItem("分享", createPendingIntent());
            VectorDrawableCompat icon = VectorDrawableCompat.create(getResources(), R.drawable
                    .ic_vector_search, null);
            PendingIntent pendingIntent = createPendingIntent();
            builder.setActionButton(getBitmap(icon), "分享", pendingIntent);
        }

        if (mCheckBoxHide.isChecked()) {
            builder.enableUrlBarHiding();
        }

        if (mCheckBoxBack.isChecked()) {
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable
                    .ic_arrow_back_24dp));
        }

        builder.setShowTitle(mCheckBoxTitle.isChecked());

        CustomTabActivityHelper.openCustomTab(
                this, builder.build(), Uri.parse(url), new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
    }

    private PendingIntent createPendingIntent() {
        Intent actionIntent = new Intent(getApplicationContext(), ShareBroadcastReceiver
                .class);

        return PendingIntent.getBroadcast(getApplicationContext(), 0, actionIntent, 0);
    }

}
