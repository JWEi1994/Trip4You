package dcs.suc.trip.Homefragment.BookNow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import dcs.suc.trip.Homefragment.Fragment_One_Way;
import dcs.suc.trip.Homefragment.Fragment_Round_Trip;

class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){

            case 0:
                fragment = new Fragment_One_Way();
                break;

            case 1:
                fragment = new Fragment_Round_Trip();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
