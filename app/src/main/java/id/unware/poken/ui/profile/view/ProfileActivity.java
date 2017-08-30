package id.unware.poken.ui.profile.view;

import android.content.Intent;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Customer;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivityWithup;
import id.unware.poken.ui.customerorder.view.OrdersFragment;
import id.unware.poken.ui.profile.view.adapter.SectionsPagerAdapter;
import id.unware.poken.ui.shoppingorder.view.OrderActivity;

public class ProfileActivity extends BaseActivityWithup implements OrdersFragment.OnOrderFragmentListener {

    private static final String TAG = "ProfileActivity";

    @BindView(R.id.parentProfileInfo) ViewGroup parentProfileInfo;
    @BindView(R.id.ivUserAvatar) ImageView ivUserAvatar;
    @BindView(R.id.tvProfileUser) TextView tvProfileUser;
    @BindView(R.id.tvProfileIdetifier) TextView tvProfileIdetifier;

    @BindView(R.id.tabs) android.support.design.widget.TabLayout tabLayout;
    @BindView(R.id.container) android.support.v4.view.ViewPager mViewPager;


    private SectionsPagerAdapter mSectionsPagerAdapter;


    private boolean isLaunchFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        // In case launch Favorite tab from Home screen
        if (getIntent().getExtras() != null) {
                isLaunchFavorite = getIntent().getExtras().getBoolean(Constants.EXTRA_IS_LAUNCH_FAVORITE, false);
            if (isLaunchFavorite) {
                mViewPager.setCurrentItem(1 /*Favorite tab*/);
            }
        }

        initView();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);

        // Setup logged in user
        setupLoggedUserView();
    }

    private void setupLoggedUserView() {
        String strCustomerData = SPHelper.getInstance().getSharedPreferences(Constants.SP_AUTH_CUSTOMER_DATA, "");

        if (!StringUtils.isEmpty(strCustomerData)) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Customer customer = gson.fromJson(strCustomerData, Customer.class);

            if (customer != null) {
                tvProfileIdetifier.setText(customer.related_user.email);
                tvProfileUser.setText(customer.related_user.getFullName());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            proceedSignout();
            return true;
        } else if (id == android.R.id.home) {
            Utils.Log(TAG, "Home navigation cliked.");
            this.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void proceedSignout() {
        PokenCredentials.getInstance().setCredential(null);
        this.finish();
    }

    @Override
    public void onListFragmentInteraction(ShoppingOrder item) {
        Utils.Log(TAG, "Interaction with order fragment.");
        openOrderScreen(item);
    }

    private void openOrderScreen(ShoppingOrder item) {
        Intent orderScreenIntent = new Intent(this, OrderActivity.class);
        orderScreenIntent.putExtra(Constants.EXTRA_ORDER_ID, item.id);
        this.startActivity(orderScreenIntent);
    }
}
