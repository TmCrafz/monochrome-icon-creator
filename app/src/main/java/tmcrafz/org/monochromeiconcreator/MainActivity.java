package tmcrafz.org.monochromeiconcreator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private List<ApplicationData> m_appData;
    private RecyclerView m_recyclerAppOverview;
    private RecyclerView.LayoutManager m_appOverviewLayoutManager;
    private ApplicationOverviewAdapter m_appOverviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packs = pm.getInstalledApplications(0);
        m_appData = new ArrayList<ApplicationData>();
        for (ApplicationInfo appi : packs) {
            String packageName = appi.packageName;
            // We only need non System apps which are launchable
            if (pm.getLaunchIntentForPackage(packageName) == null) {
                continue;
            }

            ApplicationData appDataIns = new ApplicationData();
            appDataIns.packageName = packageName;
            appDataIns.appLabel = pm.getApplicationLabel(appi).toString();
            try {
                appDataIns.icon = getApplicationContext().getPackageManager().
                        getApplicationIcon(appDataIns.packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            m_appData.add(appDataIns);
        }



        m_recyclerAppOverview = (RecyclerView) findViewById(R.id.recyclerView_appOverview);
        m_appOverviewLayoutManager = new LinearLayoutManager(this);
        m_recyclerAppOverview.setLayoutManager(m_appOverviewLayoutManager);
        m_appOverviewAdapter = new ApplicationOverviewAdapter(m_appData, this);
        m_recyclerAppOverview.setAdapter(m_appOverviewAdapter);


        /*
        createShortcut("Aragorn", "de.timsterzel.vocabularyskill");
        createShortcut("Gimli", "timsterzel.de.doomsdayclock");
        */
    }

    private void createShortcut(String shortcutName, String packageStr) {
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);

        Canvas can = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        can.drawRect(0, 0, 200, 200, paint);

        /*
        Drawable shortcutIcon = getResources().getDrawable(R.mipmap.ic_launcher);
        BitmapDrawable shortcutIconBm = (BitmapDrawable) shortcutIcon;
        shortcutIconBm.setAlpha(50);
        Bitmap shortcutIconBitmap = shortcutIconBm.getBitmap();
        */

        // Launch Main Activity of current app
        //Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
        //shortcutIntent.setAction(Intent.ACTION_MAIN);

        // Launch other app
        //Intent shortcutIntent = new Intent("de.timsterzel.vocabularyskill");
        Intent shortcutIntent = getPackageManager().getLaunchIntentForPackage(packageStr);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        // Added this because of a bug (?) https://stackoverflow.com/questions/39911270/home-screen-shortcut-to-another-application
        shortcutIntent.putExtra(Intent.EXTRA_COMPONENT_NAME, "abc");

        Intent addShortcutIntent = new Intent();
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        //addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        //        Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
        addShortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addShortcutIntent.putExtra("duplicate", true);

        getApplicationContext().sendBroadcast(addShortcutIntent);


    }
}
