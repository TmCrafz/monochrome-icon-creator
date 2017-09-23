package tmcrafz.org.monochromeiconcreator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

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

        Drawable monochromeIconDrawable = createMonochromeDrawableFrom(m_appData.get(0).icon);
        m_appData.get(0).icon.setAlpha(30);
        Bitmap icon = iconDrawableToBitmap(monochromeIconDrawable);
        createShortcut(icon, m_appData.get(0).appLabel, m_appData.get(0).packageName);
    }

    private Drawable createMonochromeDrawableFrom(Drawable drawable) {
        Drawable drawableNew = drawable.getConstantState().newDrawable().mutate();
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        drawableNew.setColorFilter(filter);
        return drawableNew;
    }

    private Bitmap iconDrawableToBitmap(Drawable icon) {
        /*
        if (icon instanceof BitmapDrawable) {
            BitmapDrawable iconBd = (BitmapDrawable) icon;
            iconBd.setColorFilter(icon.getColorFilter());
            return iconBd.getBitmap();
        }
        */
        Bitmap iconBm = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(iconBm);
        icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        icon.draw(canvas);
        return iconBm;
    }

    private void createShortcut(Bitmap icon, String label, String packageName /*ApplicationData appDataIns*/) {
        /*
        ImageView imageViewTest = (ImageView) findViewById(R.id.imageViewTest);
        imageViewTest.setImageDrawable(iconDrawable);
        imageViewTest.setImageBitmap(icon);
        */

        Intent shortcutIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        // Added this because of a bug (?) https://stackoverflow.com/questions/39911270/home-screen-shortcut-to-another-application
        shortcutIntent.putExtra(Intent.EXTRA_COMPONENT_NAME, "abc");

        Intent addShortcutIntent = new Intent();
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, label);
        //addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        //        Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
        addShortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addShortcutIntent.putExtra("duplicate", true);

        getApplicationContext().sendBroadcast(addShortcutIntent);
    }


}
