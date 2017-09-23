package tmcrafz.org.monochromeiconcreator;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ApplicationOverviewAdapter extends
        RecyclerView.Adapter<ApplicationOverviewAdapter.ApplicationOverviewViewHolder> {

    private final static String TAG = MainActivity.class.getSimpleName();

    public static class ApplicationOverviewViewHolder extends RecyclerView.ViewHolder {
        private View m_rootLayout;
        private ImageView m_imageViewIcon;
        private TextView m_txtName;

        public ApplicationOverviewViewHolder(View itemView) {
            super(itemView);
            m_rootLayout = itemView;
            m_imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView_appIcon);
            m_txtName = (TextView) itemView.findViewById(R.id.txt_appName);
        }
    }

    private Context m_context;
    private List<ApplicationData> m_appData;
    private boolean m_isInSelectionMode = false;

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
    public void onBindViewHolder(final ApplicationOverviewViewHolder holder, int position) {
        final ApplicationData appDataIns = m_appData.get(position);
        if (appDataIns.icon != null) {
            holder.m_imageViewIcon.setImageDrawable(appDataIns.icon);
        }
        holder.m_txtName.setText(appDataIns.appLabel);

        holder.m_rootLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                m_isInSelectionMode = true;
                handleClick(appDataIns, holder.itemView);
                return true;
            }
        });
        holder.m_rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_isInSelectionMode) {
                    handleClick(appDataIns, holder.itemView);
                }
            }
        });
        changeBackroundColor(holder.m_rootLayout, appDataIns.isSelected);
    }

    @Override
    public int getItemCount() {
        return m_appData.size();
    }

    private void handleClick(ApplicationData appDataIns, View rootView) {
        appDataIns.isSelected = !appDataIns.isSelected;
        changeBackroundColor(rootView, appDataIns.isSelected);
        if (getSelectionCnt() == 0) {
            m_isInSelectionMode = false;
        }
    }

    private void changeBackroundColor(View view , boolean isSelected) {
        view.setBackgroundColor(isSelected ? Color.LTGRAY : Color.WHITE);
    }

    public int getSelectionCnt() {
        int cnt = 0;
        for (ApplicationData appDataIns : m_appData) {
            if (appDataIns.isSelected) {
                cnt++;
            }
        }
        return cnt;
    }

    public void deselectAll() {
        for (ApplicationData appDataIns : m_appData) {
            appDataIns.isSelected = false;
        }
        notifyDataSetChanged();
    }
}
