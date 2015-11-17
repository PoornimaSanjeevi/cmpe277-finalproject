package tania277.project_final.Adapter;

/**
 * Created by Tania on 11/16/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import tania277.project_final.EventFragment;
import tania277.project_final.FriendFragment;
import tania277.project_final.UserFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                UserFragment tab1 = new UserFragment();

                return tab1;
            case 1:
                EventFragment tab2 = new EventFragment();
                return tab2;
            case 2:
                FriendFragment tab3 = new FriendFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}