package team.fcisquare;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sun.jna.platform.unix.X11;

import java.util.ArrayList;

//not used yet !!!!!
//// TODO: 3/24/2016 check if this activity is needed !!!!
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter pagerAdapter;
    private int[] tabIcons = {R.drawable.home, R.drawable.messages, R.drawable.notification, R.drawable.followers};
    private int currentOpennedNavigationItem = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView = (NavigationView)findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                if(currentOpennedNavigationItem == item.getItemId())
                    return false;
                switch (item.getItemId()){
                    case R.id.profile_nav_header:
                        navigationView.getMenu().getItem(0).setCheckable(true);
                        navigationView.getMenu().getItem(2).setCheckable(false);
                        navigationView.getMenu().getItem(2).setCheckable(false);
                        break;
                    case R.id.following_nav_header:
                        navigationView.getMenu().getItem(1).setCheckable(true);
                        navigationView.getMenu().getItem(0).setCheckable(false);
                        navigationView.getMenu().getItem(2).setCheckable(false);
                        break;
                    case R.id.credits_nav_header:
                        navigationView.getMenu().getItem(2).setCheckable(true);
                        navigationView.getMenu().getItem(0).setCheckable(false);
                        navigationView.getMenu().getItem(1).setCheckable(false);
                }
                currentOpennedNavigationItem = item.getItemId();
                return true;
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new MainFragment());
        pagerAdapter.addFragment(new MainFragment());
        pagerAdapter.addFragment(new MainFragment());
        pagerAdapter.addFragment(new MainFragment());
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        Toast.makeText(this, "sdsdsd", Toast.LENGTH_SHORT).show();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final ArrayList<Fragment> fragmentList = new ArrayList<>();
        private final ArrayList<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
     //       updateTabsIcons();

         //   tabLayout.getTabAt(position).setIcon(tabIcons[0]);
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment){
            fragmentList.add(fragment);
         //   fragmentTitleList.add(s);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Toast.makeText(getApplicationContext(),"this is" + position,Toast.LENGTH_SHORT).show();
            return null;
        }

    }
}
