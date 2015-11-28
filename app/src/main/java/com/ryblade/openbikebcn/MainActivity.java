package com.ryblade.openbikebcn;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.ryblade.openbikebcn.Fragments.FavoritesFragment;
import com.ryblade.openbikebcn.Fragments.MapFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.updateBikesDatabase();
//        Intent intent = new Intent(this, OsmActivity.class);
//        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            MapFragment mapFragment = new MapFragment();
            currentFragment = mapFragment;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mapFragment)
                    .commit();
        }

    }

    private void updateBikesDatabase() {
        new FetchAPITask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        }

        if (id == R.id.action_add_favourites) {
            if (currentFragment.getClass().isInstance(MapFragment.class)) {
                MapFragment mapFragment = (MapFragment)currentFragment;

            }
            return true;
        } else if (id == R.id.action_start_route) {
            if (currentFragment.getClass().isInstance(MapFragment.class)) {
                MapFragment mapFragment = (MapFragment)currentFragment;
                mapFragment.startRouteClicked();
            }
            return true;
        } else if (id == R.id.action_end_route) {
            if (currentFragment.getClass().isInstance(MapFragment.class)) {
                MapFragment mapFragment = (MapFragment)currentFragment;
                mapFragment.endRouteClicked();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            // Open map fragment

            MapFragment newFragment = new MapFragment();
            currentFragment = newFragment;

//            Bundle args = new Bundle();
//            args.putString("lang", lang);
//            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();

        } else if (id == R.id.nav_fav) {
            // Open fav fragment

            FavoritesFragment newFragment = new FavoritesFragment();

            currentFragment = newFragment;

//            Bundle args = new Bundle();
//            args.putString("lang", lang);
//            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.station_info_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_add_favourites) {
                    if (MapFragment.class.isInstance(currentFragment)) {
                        MapFragment mapFragment = (MapFragment)currentFragment;

                    }
                    return true;
                } else if (id == R.id.action_start_route) {
                    if (MapFragment.class.isInstance(currentFragment)) {
                        MapFragment mapFragment = (MapFragment)currentFragment;
                        mapFragment.startRouteClicked();
                    }
                    return true;
                } else if (id == R.id.action_end_route) {
                    if (MapFragment.class.isInstance(currentFragment)) {
                        MapFragment mapFragment = (MapFragment)currentFragment;
                        mapFragment.endRouteClicked();
                    }
                    return true;
                }
                return true;
            }
        });
        popup.show();
    }

}
