package team.fcisquare;

import android.content.Intent;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter pagerAdapter;
    private int[] tabIcons = {R.drawable.home, R.drawable.messages, R.drawable.notification, R.drawable.followers};
    private int currentOpenedNavigationItem = -1;
    private User user;
    private Bundle bundle;
    private MainFragment mainFragment;
    private NotificationFragment notificationFragment;
    private MessagesFragment messagesFragment;
    private FollowersFragment followersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //receiving user object from sign in class
        user = (User) getIntent().getSerializableExtra("user");
        bundle = new Bundle();
        bundle.putSerializable("user", user); // carry data to be send to fragment
        mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);

        notificationFragment = new NotificationFragment();
        notificationFragment.setArguments(bundle);

        messagesFragment = new MessagesFragment();
        messagesFragment.setArguments(bundle);

        followersFragment = new FollowersFragment();
        followersFragment.setArguments(bundle);

        //setting toolbar
        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //initializing drawer for navigation veiw
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView = (NavigationView)findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                if(currentOpenedNavigationItem == item.getItemId())
                    return false;
                switch (item.getItemId()){
                    case R.id.profile_nav_header:
                        navigationView.getMenu().getItem(0).setCheckable(true);
                        navigationView.getMenu().getItem(2).setCheckable(false);
                        navigationView.getMenu().getItem(2).setCheckable(false);
                        navigationView.getMenu().getItem(3).setCheckable(false);
                        break;
                    case R.id.following_nav_header:
                        navigationView.getMenu().getItem(1).setCheckable(true);
                        navigationView.getMenu().getItem(0).setCheckable(false);
                        navigationView.getMenu().getItem(2).setCheckable(false);
                        navigationView.getMenu().getItem(3).setCheckable(false);
                        break;
                    case R.id.add_place_nav_header:
                        break;
                    case R.id.credits_nav_header:
                        navigationView.getMenu().getItem(2).setCheckable(true);
                        navigationView.getMenu().getItem(0).setCheckable(false);
                        navigationView.getMenu().getItem(1).setCheckable(false);
                        navigationView.getMenu().getItem(3).setCheckable(false);
                        break;
                    case R.id.sign_out_nav_header:
                        navigationView.getMenu().getItem(3).setCheckable(true);
                        navigationView.getMenu().getItem(0).setCheckable(false);
                        navigationView.getMenu().getItem(1).setCheckable(false);
                        navigationView.getMenu().getItem(2).setCheckable(false);
                }
                currentOpenedNavigationItem = item.getItemId();
                return true;
            }
        });

        // drawer toggle is used for hamburger shape synchronization
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //view pager used for tabs
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(mainFragment);
        pagerAdapter.addFragment(messagesFragment);
        pagerAdapter.addFragment(notificationFragment);
        pagerAdapter.addFragment(followersFragment);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        //assign the pager for the tabs
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);


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
            return null;
        }

    }

    public void signOut(MenuItem item){
        //// TODO: 4/17/2016 remove preferences from phone
        startActivity(new Intent(this, Login.class));
    }
    public void profile(MenuItem item){
        Intent intent = new Intent(this, UserProfile.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", getIntent().getSerializableExtra("user"));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void addPlace(MenuItem item){
        startActivity(new Intent(this, AddPlace.class));
    }

}
