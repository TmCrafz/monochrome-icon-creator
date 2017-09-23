package tmcrafz.org.monochromeiconcreator;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tim on 23.09.17.
 */

public class ApplicationOverviewAdapter extends
        RecyclerView.Adapter<ApplicationOverviewAdapter.ApplicationOverviewViewHolder> {

    public static class ApplicationOverviewViewHolder extends RecyclerView.ViewHolder {
        private ImageView m_imageViewIcon;
        private TextView m_txtName;

        public ApplicationOverviewViewHolder(View itemView) {
            super(itemView);
            m_imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView_appIcon);
            m_txtName = (TextView) itemView.findViewById(R.id.txt_appName);
        }
    }

    private Context m_context;
    private List<ApplicationData> m_appData;

    public ApplicationOverviewAdapter(List<ApplicationData> appData, Context context) {
        m_appData = appData;
        m_context = context;
    }

    @Override
    public ApplicationOverviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recycler_application_overview, parent, false);
        return new ApplicationOverviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ApplicationOverviewViewHolder holder, int position) {
        ApplicationData appDataIns = m_appData.get(position);
        if (appDataIns.icon != null) {
            holder.m_imageViewIcon.setImageDrawable(appDataIns.icon);
        }
        holder.m_txtName.setText(appDataIns.appLabel);
    }

    @Override
    public int getItemCount() {
        return m_appData.size();
    }


}
