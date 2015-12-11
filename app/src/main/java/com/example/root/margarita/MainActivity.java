package com.example.root.margarita;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;

public class MainActivity extends NavigationLiveo implements OnItemClickListener,ContactFragment.OnContactsInteractionListener {

    private HelpLiveo mHelpLiveo;
    private boolean isSearchResultView = false;

    @Override
    public void onInt(Bundle savedInstanceState) {
// User Information
        this.userName.setText("John Mathai");
        this.userEmail.setText("johnrejimathai@gmail.com");
        this.userPhoto.setImageResource(R.mipmap.ic_launcher);
        this.userBackground.setImageResource(R.drawable.ic_user_background_first);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.profile), R.mipmap.ic_person_add_black_24dp);
        mHelpLiveo.addSubHeader(getString(R.string.categories)); //Item subHeader
        mHelpLiveo.add(getString(R.string.report_location), R.mipmap.ic_add_location_black_24dp);
        mHelpLiveo.add(getString(R.string.documents), R.mipmap.ic_folder_black_24dp);
        mHelpLiveo.add(getString(R.string.camera), R.mipmap.ic_local_see_black_24dp);
        mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add(getString(R.string.trash), R.mipmap.ic_delete_black_24dp);
        mHelpLiveo.add(getString(R.string.report), R.mipmap.ic_report_black_24dp, 120);
        mHelpLiveo.add(getString(R.string.friends), R.mipmap.ic_group_black_24dp);

        //with(this, Navigation.THEME_DARK). add theme dark
        //with(this, Navigation.THEME_LIGHT). add theme light

        with(this) // default theme is dark
                .startingPosition(2) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .footerItem(R.string.settings, R.mipmap.ic_settings_black_24dp)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();

        int position = this.getCurrentPosition();
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment = null;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        Bundle args = new Bundle();

        Toast.makeText(this.getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();

        switch (position){
            case 0:
                mFragment = new ProfileFragment();
                args.putString("title", mHelpLiveo.get(position).getName());
                mFragment.setArguments(args);
                break;
            case 2:
                mFragment = new LocationFragment();
                args.putString("title", mHelpLiveo.get(position).getName());
                mFragment.setArguments(args);
                break;
            case 8:
                mFragment = new ContactFragment();
                args.putString("title", mHelpLiveo.get(position).getName());
                mFragment.setArguments(args);
                break;
            default:
//                mFragment = mFragment.newInstance(mHelpLiveo.get(position).getName());
                break;
        }

        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).addToBackStack(null).commit();
        }

        setElevationToolBar(position != 2 ? 15 : 0);
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact has been selected.
     *
     * @param contactUri The contact Uri to the selected contact.
     */
    @Override
    public void onContactSelected(Uri contactUri) {

            final Uri uri = contactUri;
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, ContactDetailFragment.newInstance(uri));
            transaction.addToBackStack(null);
            transaction.commit();

    }

    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact is no longer selected.
     */
    @Override
    public void onSelectionCleared() {
        ContactDetailFragment mContactDetailFragment = (ContactDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.container);
        if (mContactDetailFragment != null) {
            mContactDetailFragment.setContact(null);
        }
    }

    @Override
    public boolean onSearchRequested() {
        // Don't allow another search if this activity instance is already showing
        // search results. Only used pre-HC.
        return !isSearchResultView && super.onSearchRequested();
    }

}
