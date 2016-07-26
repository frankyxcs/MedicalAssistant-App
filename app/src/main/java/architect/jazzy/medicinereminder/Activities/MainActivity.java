package architect.jazzy.medicinereminder.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.AnimRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.ContextCompatApi24;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import architect.jazzy.medicinereminder.BuildConfig;
import architect.jazzy.medicinereminder.CustomComponents.FragmentBackStack;
import architect.jazzy.medicinereminder.CustomViews.ColorSelectorFragment;
import architect.jazzy.medicinereminder.CustomViews.DaySelectorFragmentDialog;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.AddDoctorFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.AddMedicineFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.BrowserFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.DashboardFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.DoctorDetailFragments.DoctorDetailFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.DoctorDetailFragments.DoctorMedicineListFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.DoctorListFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.EmojiSelectFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.MedicineListFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.NewsFragments.NewsListFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.SearchFragments.SearchFragment;
import architect.jazzy.medicinereminder.Fragments.OfflineActivity.SettingsFragment;
import architect.jazzy.medicinereminder.Fragments.OnlineActivity.UserDetailsFragment;
import architect.jazzy.medicinereminder.HelperClasses.Constants;
import architect.jazzy.medicinereminder.HelperClasses.FirebaseConstants;
import architect.jazzy.medicinereminder.Manifest;
import architect.jazzy.medicinereminder.Models.Doctor;
import architect.jazzy.medicinereminder.Models.FeedItem;
import architect.jazzy.medicinereminder.Models.Medicine;
import architect.jazzy.medicinereminder.R;
import architect.jazzy.medicinereminder.Services.AlarmSetterService;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        EmojiSelectFragment.OnFragmentInteractionListener, DaySelectorFragmentDialog.OnFragmentInteractionListener,
        MedicineListFragment.FragmentInteractionListener, AddMedicineFragment.FragmentInteractionListener,
        NewsListFragment.FeedClickListener, DoctorListFragment.OnMenuItemClickListener,
        DoctorMedicineListFragment.FragmentInteractionListener, DoctorListFragment.OnFragmentInteractionListenr,
        DoctorDetailFragment.ImageChangeListener, DashboardFragment.OnFragmentInteractionListener,
        AddDoctorFragment.OnFragmentInteractionListener, ColorSelectorFragment.OnColorChangeListener{

    public static final String TAG = "MainActivity";
    private static final int WRITE_PERMISSION_CODE = 8529;

    FragmentBackStack fragmentBackStack = new FragmentBackStack();
    final int SHOW_LIST_REQUEST_CODE = 123;
    FrameLayout frameLayout;
    DrawerLayout drawerLayout;
    ActivityClickListener activityClickListener;
    ActivityResultListener activityResultListener;
    EditText searchQuery;
    NavigationView navigationView;
    Toolbar toolbar;
    private InterstitialAd interstitialAd;
    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getSharedPreferences("illustration", MODE_PRIVATE).getBoolean("shown1", false)) {
            startActivity(new Intent(this, Illustration.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Constants.getThemeColor(this));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        frameLayout = (FrameLayout) findViewById(R.id.frame);

        Intent startAlarmServiceIntent = new Intent(this, AlarmSetterService.class);
        startAlarmServiceIntent.setAction("CREATE");
        startService(startAlarmServiceIntent);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerLayout = navigationView.getHeaderView(0);
        searchQuery = (EditText) headerLayout.findViewById(R.id.searchQuery);
//        Log.e(TAG, navigationView.getChildCount()+" "+navigationView.getChildAt(0).toString());
        headerLayout.findViewById(R.id.searchButton).setVisibility(View.GONE);
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.getMenu().findItem(R.id.add).setChecked(true);

        headerLayout.findViewById(R.id.back).setBackgroundColor(Constants.getThemeColor(this));
        searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(v.getText().toString());
                    searchQuery.setText("");
                    hideKeyboard();
                }
                return false;
            }
        });

        dimNotificationBar();
//        displayFragment(new AddAppointmentFragment(), true);
        displayFragment(new DashboardFragment(), true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, 0);
                    builder.setTitle("Permission required")
                            .setMessage("WRITE_EXTERNAL_STORAGE permission is required for caching and reduce internet data usage.")
                            .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            WRITE_PERMISSION_CODE);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, "Some features like search and news might not work without this permission", Toast.LENGTH_LONG).show();
                                }
                            })
                    .show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_PERMISSION_CODE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
//        testCalendar();
    }

    @Override
    public void onThemeColorChange(int color) {
        toolbar.setBackgroundColor(color);
        getSharedPreferences(Constants.SETTING_PREF, MODE_PRIVATE)
                .edit()
                .putInt(Constants.THEME_COLOR, color)
                .apply();
//        if(color==getResources().getColor(R.color.themeColorLight)){
//            toolbar.setTitleTextColor(Color.parseColor("#111111"));
//        }
        findViewById(R.id.back).setBackgroundColor(Constants.getThemeColor(this));
        try {
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
            drawerLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void testCalendar() {
        Calendar calendar = Calendar.getInstance();
        Log.e(TAG, "Calendar Test: " + calendar.toString());
    }

    private void dimNotificationBar() {
        final View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                            }
                        });
                    }
                }, 5000);
            }
        });
    }


    public void displayInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }

    }

    private void performSearch(String s) {
        displayFragment(SearchFragment.initiate(s), true);
        drawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (activityKeyClickListener != null) {
            if (activityKeyClickListener.onBackKeyPressed()) {
                return;
            }
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else if (!fragmentBackStack.empty()) {
            Fragment fragment = fragmentBackStack.pop();
            if (fragment == null) {
                android.support.v4.app.Fragment fragment1 = fragmentBackStack.popSupport();
                if (fragment1 == null) {
                    super.onBackPressed();
                    return;
                }
//                Log.e(TAG,"popping support fragment: "+fragment1.toString());
                displaySupportFragment(fragment1, false);
                return;
            }
//            Log.e(TAG,"popping fragment");
            displayFragment(fragment, false);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        AddMedicineFragment.setAlarm(MainActivity.this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        getSharedPreferences(Constants.INTERNAL_PREF, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(Constants.SEARCH_RESULT, false)
                .apply();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmpMR");
                File file = new File(folder, Constants.SEARCH_FILE_NAME);
                if (file.exists()) {
                    try {
                        file.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        Fragment fragment = null;
        android.support.v4.app.Fragment supportFragment = null;
        try {
            fragment = getFragmentManager().findFragmentById(R.id.frame);
        } catch (Exception e) {
            supportFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        }
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
        switch (id) {
            case R.id.showMedicineList:
                if (!(fragment instanceof MedicineListFragment)) {
                    FirebaseConstants.Analytics.logCurrentScreen(this, "MedicineList");
                    showMedicines();
                }
                break;
            case R.id.myAccount:
                FirebaseConstants.Analytics.logCurrentScreen(this, "OnlineActivity");
                Intent i=new Intent(this, OnlineActivity.class);
                i.putExtra("fragment", UserDetailsFragment.TAG);
                startActivity(i);
                break;
            case R.id.action_settings:
                FirebaseConstants.Analytics.logCurrentScreen(this, "Settings");
                showSettings();
                break;
            case R.id.remedies:
                FirebaseConstants.Analytics.logCurrentScreen(this, "RemedyList");
                startActivity(new Intent(this, OnlineActivity.class));
                break;
//            case R.id.add:
//                addMedicine(false);
//                break;
//            case R.id.credits:
//                showCredits();
//                break;
            case R.id.news:
                if (!(fragment instanceof NewsListFragment)) {
                    FirebaseConstants.Analytics.logCurrentScreen(this, "NewsList");
                    displayFragment(new NewsListFragment(), true);
                }
                break;
            case R.id.addDoctor:
                if (!(fragment instanceof DoctorListFragment)) {
                    FirebaseConstants.Analytics.logCurrentScreen(this, "DoctorList");
                    displayFragment(new DoctorListFragment(), true);
                }
                break;
            case R.id.circularTest:
                if (!(fragment instanceof DashboardFragment)) {
                    FirebaseConstants.Analytics.logCurrentScreen(this, "Dashboard");
                    displayFragment(new DashboardFragment(), true);
                }
                break;
            default:
                break;
        }
        drawerLayout.closeDrawers();
        return false;
    }


    void showSettings() {
//        Intent prefIntent = new Intent(this, BasicPreferences.class);
//        startActivity(prefIntent);
        displayFragment(new SettingsFragment(), true);
    }

    void showMedicines() {
        Fragment fragment = new MedicineListFragment();
        displayFragment(fragment, true);
    }


    void addMedicineToView(boolean add) {
        Fragment fragment = new AddMedicineFragment();
        displayFragment(fragment, add);

    }

    public void displayFragment(Fragment fragment) {
        displayFragment(fragment, false);
    }

    public void displayFragment(Fragment fragment, boolean add) {
        displayFragment(fragment, add, R.anim.fragment_in_from_left, R.anim.fragment_out_from_right);
    }

    public void displayFragment(Fragment fragment, boolean add, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        Log.e(TAG, "display fragment");
        if (add) {
            addToBackStack();
        }
        toolbar.getMenu().clear();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(enterAnim,exitAnim);
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }


    void addToBackStack() {
        try {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.frame);
            if (fragment == null) {
                throw new Exception();
            }
//            if (!(fragment instanceof SearchFragment))
                fragmentBackStack.push(fragment);
            toolbar.getMenu().clear();
        } catch (Exception e) {
            android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
            if (fragment == null) {
                return;
            }
            fragmentBackStack.push(fragment);
        }
    }

    public void displaySupportFragment(android.support.v4.app.Fragment fragment, boolean add) {
        displaySupportFragment(fragment, add, R.anim.fragment_in_from_left, R.anim.fragment_out_from_left);
    }


    public void displaySupportFragment(android.support.v4.app.Fragment fragment, boolean add, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (fragment == null) {
            return;
        }
        toolbar.getMenu().clear();
        if (add) {
            addToBackStack();
        }

        try {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frame)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        Log.e(TAG,"display Support fragment: "+fragment.toString());

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(enterAnim,exitAnim);
        transaction.replace(R.id.frame, fragment).commit();
    }

    void showCredits() {
        startActivity(new Intent(this, AboutUs.class));
    }


    public static void setAlarm(Context context) {
        Intent startAlarmServiceIntent = new Intent(context, AlarmSetterService.class);
//        startAlarmServiceIntent.setAction("CANCEL");
//        context.startService(startAlarmServiceIntent);
        startAlarmServiceIntent.setAction("CREATE");
        context.startService(startAlarmServiceIntent);
    }


    public void showDaySelection(View v) {
        activityClickListener.daySelectionClick();
    }


    public void showFeed(String url) {
        displayFragment(BrowserFragment.getInstance(url), true);
    }

    public void showFeed(String url, boolean isNews) {
        displayFragment(BrowserFragment.getInstance(url, isNews), true);
    }


    @Override
    public void onEmojiSelected(int position) {
        activityClickListener.emojiClick(position);
    }

    @Override
    public void onDaySelectionChanged(int position, Boolean isChecked) {
        activityClickListener.daySelectionChanged(position, isChecked);
    }

    @Override
    public void addMedicine() {
        addMedicineToView(true);
    }

    @Override
    public void showMedicineList() {
        displayFragment(new MedicineListFragment(), true);
    }


    @Override
    public void onAddDoctorClicked() {
        displayFragment(new AddDoctorFragment(), true);
    }

    @Override
    public void onDoctorSelected(Doctor doctor) {
        displaySupportFragment(DoctorDetailFragment.newInstance(doctor), true);
    }

    @Override
    public void addDoctor() {
        AddDoctorFragment fragment = new AddDoctorFragment();
        displayFragment(fragment, true);
    }

    @Override
    public void addMedicine(boolean addToBackStack) {
        addMedicineToView(addToBackStack);
    }

    @Override
    public void onDoctorImageChange(int resultCode, Intent data) {
        if (doctorDetailImageChangeListener != null) {
            doctorDetailImageChangeListener.onDoctorImageChanged(resultCode, data);
        }
    }

    @Override
    public void showDoctors() {
        displayFragment(new DoctorListFragment(), true);
    }

    @Override
    public void showNews() {
        displayFragment(new NewsListFragment(), true);
    }

    @Override
    public void showSearch() {
        displayFragment(new SearchFragment(), true);
    }

    @Override
    public void showDetails(int position, ArrayList<Medicine> medicines) {
        Intent i = new Intent(this, MedicineDetails.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.MEDICINE_NAME_LIST, medicines);
        i.putExtra(Constants.MEDICINE_POSITION, position);
        i.putExtras(bundle);
        startActivityForResult(i, SHOW_LIST_REQUEST_CODE);
    }

    @Override
    public void onFeedClick(FeedItem item) {
        showFeed(item.getUrl());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "Activity Result " + requestCode);
        if (requestCode == SHOW_LIST_REQUEST_CODE) {
            activityResultListener.medicineListActivityResult(requestCode, resultCode, data);
        }
    }


    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    ActivityKeyClickListener activityKeyClickListener;
    DoctorDetailImageChangeListener doctorDetailImageChangeListener;


    public void setDoctorDetailImageChangeListener(DoctorDetailImageChangeListener doctorDetailImageChangeListener) {
        this.doctorDetailImageChangeListener = doctorDetailImageChangeListener;
    }

    public void setActivityKeyClickListener(ActivityKeyClickListener activityKeyClickListener) {
        this.activityKeyClickListener = activityKeyClickListener;
    }

    public void setActivityResultListener(ActivityResultListener activityResultListener) {
        this.activityResultListener = activityResultListener;
    }

    public void setActivityClickListener(ActivityClickListener activityClickListener) {
        this.activityClickListener = activityClickListener;
    }

    public interface ActivityClickListener {
        void daySelectionChanged(int position, boolean isCheck);

        void daySelectionClick();

        void emojiClick(int position);
    }

    public interface ActivityResultListener {
        void medicineListActivityResult(int requestCode, int resultCode, Intent data);
    }

    public interface DoctorDetailImageChangeListener {
        void onDoctorImageChanged(int resultCode, Intent data);
    }

    public interface ActivityKeyClickListener {
        boolean onBackKeyPressed();
    }
}

