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

import org.tmcrafz.flipiconchecker.FlipIconChecker;

import java.util.ArrayList;
import java.util.List;

public class ApplicationOverviewAdapter extends
        RecyclerView.Adapter<ApplicationOverviewAdapter.ApplicationOverviewViewHolder> {

    private final static String TAG = MainActivity.class.getSimpleName();

    public static class ApplicationOverviewViewHolder extends RecyclerView.ViewHolder {
        private View m_rootLayout;
        //private ImageView m_imageViewIcon;
        private FlipIconChecker m_flipIconChecker;
        private ImageView m_flipIconCheckerFrontView;
        private ImageView m_flipIconCheckerBackView;
        private TextView m_txtName;

        public ApplicationOverviewViewHolder(View itemView) {
            super(itemView);
            m_rootLayout = itemView;
            //m_imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView_appIcon);
            m_flipIconChecker = (FlipIconChecker) itemView.findViewById(R.id.flipChecker_icon);
            m_flipIconCheckerFrontView = (ImageView)
                    m_flipIconChecker.getFrontView().findViewById(R.id.imageView_flipIconImage);
            m_flipIconCheckerBackView = (ImageView)
                    m_flipIconChecker.getBackView().findViewById(R.id.imageView_flipIconImage);
            m_txtName = (TextView) itemView.findViewById(R.id.txt_appName);
            /*
            m_flipIconChecker.setOnFlipIconCheckerClickedListener(
                    new FlipIconChecker.OnFlipIconCheckerClickedListener() {
                @Override
                public void onFlipIconCheckerClicked() {

                }
            });
            */
        }

        public void deselect() {
            if (m_flipIconChecker.isChecked()) {
                m_flipIconChecker.changeState();
            }
            m_rootLayout.setBackgroundColor(Color.WHITE);
        }
    }

    private Context m_context;
    private List<ApplicationData> m_appData;
    // The viewholder whiche exists currently in the recyclerview
    private List<ApplicationOverviewViewHolder> m_currentExistingViewHolder;
    private boolean m_isInSelectionMode = false;

    public ApplicationOverviewAdapter(List<ApplicationData> appData, Context context) {
        m_appData = appData;
        m_context = context;
        m_currentExistingViewHolder = new ArrayList<ApplicationOverviewViewHolder>();
    }

    @Override
    public ApplicationOverviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recycler_application_overview, parent, false);
        return new ApplicationOverviewViewHolder(itemView);
    }

    @Override
    public void onViewAttachedToWindow(ApplicationOverviewViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // Add new holder to list
        m_currentExistingViewHolder.add(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ApplicationOverviewViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        // Remove the removed holder from list
        m_currentExistingViewHolder.remove(holder);
    }

    @Override
    public void onBindViewHolder(final ApplicationOverviewViewHolder holder, int position) {
        final ApplicationData appDataIns = m_appData.get(position);
        if (appDataIns.icon != null) {
            // Show the normal icon on front and the monochrom icon on the back (when its checked)
            holder.m_flipIconCheckerFrontView.setImageDrawable(appDataIns.icon);
            holder.m_flipIconCheckerBackView.setImageDrawable(appDataIns.iconMonochrome);
        }
        holder.m_txtName.setText(appDataIns.appLabel);

        holder.m_flipIconChecker.setOnFlipIconCheckerClickedListener(
                new FlipIconChecker.OnFlipIconCheckerClickedListener() {
            @Override
            public void onFlipIconCheckerClicked() {
                m_isInSelectionMode = true;
                handleSelectionChange(appDataIns, holder.itemView);
            }
        });
        changeBackroundColor(holder.m_rootLayout, appDataIns.isSelected);
        holder.m_flipIconChecker.setChecked(appDataIns.isSelected);



        //Log.d(TAG, "ADT onBindViewHolder Size: " + mHolderViews.size());
    }

    @Override
    public int getItemCount() {
        return m_appData.size();
    }

    private void handleSelectionChange(ApplicationData appDataIns, View rootView) {
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
        for (ApplicationOverviewViewHolder holder : m_currentExistingViewHolder) {
            holder.deselect();
        }
        //notifyDataSetChanged();
    }
}
