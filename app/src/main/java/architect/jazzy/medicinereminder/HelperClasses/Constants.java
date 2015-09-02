package architect.jazzy.medicinereminder.HelperClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Jibin_ism on 25-Mar-15.
 */
public class Constants {

    public static final String BUNDLE_SELECTED_NEWS="selected";

    public Constants() {
        super();
    }

    public static final String BUNDLE_MEDICINE_TAG="MEDICINE_NAME";
    public static final String MEDICINE_POSITION="medicinePosition";

    public static final String INPUT_SHARED_PREFS="TimePrefs";

    public static final String BEFORE_BREAKFAST_HOUR="bbh";
    public static final String BEFORE_BREAKFAST_MINUTE="bbm";
    public static final String AFTER_BREAKFAST_HOUR="abh";
    public static final String AFTER_BREAKFAST_MINUTE="abm";

    public static final String BEFORE_LUNCH_HOUR="blh";
    public static final String BEFORE_LUNCH_MINUTE="blm";
    public static final String AFTER_LUNCH_HOUR="alh";
    public static final String AFTER_LUNCH_MINUTE="alm";

    public static final String BEFORE_DINNER_HOUR="bdh";
    public static final String BEFORE_DINNER_MINUTE="bdm";
    public static final String AFTER_DINNER_HOUR="adh";
    public static final String AFTER_DINNER_MINUTE="adm";

    public static final String MEDICINE_NAME_LIST="medicineNameList";

    public static final String IS_24_HOURS_FORMAT="is24hrs";

    public static final String FROM_MAIN_ACTIVITY="fromMain";


    public static final String MEDICINE_NAME="medicineName";
    public static final String MORNING_TIME="mornTime";
    public static final String NOON_TIME="noonTime";
    public static final String NIGHT_TIME="nightTime";
    public static final String DOCTOR_ID="doctorId";
    public static final String DAY_SUNDAY="sunday";
    public static final String DAY_MONDAY="monday";
    public static final String DAY_TUESDAY="tuesday";
    public static final String DAY_WEDNESDAY="wednesday";
    public static final String DAY_THURSDAY="thursday";
    public static final String DAY_FRIDAY="friday";
    public static final String DAY_SATURDAY="saturday";
    public static final String START_DATE="startDate";
    public static final String END_DATE="endDate";
    public static final String BREAKFAST="breakfast";
    public static final String LUNCH="lunch";
    public static final String DINNER="dinner";
    public static final String ICON="icon";
    public static final String CUSTOM_TIME_HOUR="custTimeH";
    public static final String CUSTOM_TIME_MINUTE="custTimeM";
    public static final String NOTES="note";

    public static final String INDEFINITE_SCHEDULE="indefinite";

    public static final String SEARCH_FILE_NAME="tmpMR_Search.txt";
    public static final String BUNDLE_SEARCH_TERM="searchTerm";
    public static final String BUNDLE_WEB_DOCUMENT="webDocument";
    public static final String BUNDLE_DOCTOR="doctorBundle";

    /**
     * Check if phone is connected to internet
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static Spanned getSuggestionText(String o,String suggestion){
        return Html.fromHtml("Nothing found for <i>"+o+"</i>. Showing result for <i><b>"+suggestion+"</b></i>");
    }


    public static class MedicineNetUrls{
        public static final ArrayList<Pair<String, String>> urls=new ArrayList<>();

        private static void setup(){
            urls.clear();
            urls.add(Pair.create("Daily News", FeedParser.feedUrl));
            urls.add(Pair.create("Allergies", "http://www.medicinenet.com/rss/general/allergies.xml"));
            urls.add(Pair.create("Alzheimer's Disease", "http://www.medicinenet.com/rss/general/alzheimers.xml"));
            urls.add(Pair.create("Arthritis", "http://www.medicinenet.com/rss/general/arthritis.xml"));
            urls.add(Pair.create("Asthma", "http://www.medicinenet.com/rss/general/asthma.xml"));
            urls.add(Pair.create("Cancer", "http://www.medicinenet.com/rss/general/cancer.xml"));
            urls.add(Pair.create("Cholesterol", "http://www.medicinenet.com/rss/general/cholesterol.xml"));
            urls.add(Pair.create("Chronic Pain", "http://www.medicinenet.com/rss/general/chronic_pain.xml"));
            urls.add(Pair.create("Cold & Flu", "http://www.medicinenet.com/rss/general/cold_and_flu.xml"));
            urls.add(Pair.create("Depression", "http://www.medicinenet.com/rss/general/depression.xml"));
            urls.add(Pair.create("Diabetes", "http://www.medicinenet.com/rss/general/diabetes.xml"));
            urls.add(Pair.create("Diet", "http://www.medicinenet.com/rss/general/diet_and_weight_management.xml"));
            urls.add(Pair.create("Digestion", "http://www.medicinenet.com/rss/general/digestion.xml"));
            urls.add(Pair.create("Eyesight", "http://www.medicinenet.com/rss/general/eyesight.xml"));
            urls.add(Pair.create("Hearing", "http://www.medicinenet.com/rss/general/hearing.xml"));
            urls.add(Pair.create("Heart", "http://www.medicinenet.com/rss/general/heart.xml"));
            urls.add(Pair.create("High Blood Pressure", "http://www.medicinenet.com/rss/general/high_blood_pressure.xml"));
            urls.add(Pair.create("HIV", "http://www.medicinenet.com/rss/general/hiv.xml"));
            urls.add(Pair.create("Infectious Disease", "http://www.medicinenet.com/rss/general/infectious_disease.xml"));
            urls.add(Pair.create("Lung Conditions", "http://www.medicinenet.com/rss/general/lung_conditions.xml"));
            urls.add(Pair.create("Medications", "http://www.medicinenet.com/rss/general/medications.xml"));
            urls.add(Pair.create("Menopause", "http://www.medicinenet.com/rss/general/menopause.xml"));
            urls.add(Pair.create("Men's Health", "http://www.medicinenet.com/rss/general/mens_health.xml"));
            urls.add(Pair.create("Mental Health", "http://www.medicinenet.com/rss/general/mental_health.xml"));
            urls.add(Pair.create("Migraine", "http://www.medicinenet.com/rss/general/migraine.xml"));
            urls.add(Pair.create("Neurology", "http://www.medicinenet.com/rss/general/neurology.xml"));
            urls.add(Pair.create("Oral Health", "http://www.medicinenet.com/rss/general/oral_health.xml"));
            urls.add(Pair.create("Kids Health", "http://www.medicinenet.com/rss/general/kids_health.xml"));
            urls.add(Pair.create("Pregnancy", "http://www.medicinenet.com/rss/general/pregnancy.xml"));
            urls.add(Pair.create("Prevention & Wellness", "http://www.medicinenet.com/rss/general/prevention_and_wellness.xml"));
            urls.add(Pair.create("Senior Health", "http://www.medicinenet.com/rss/general/senior_health.xml"));
            urls.add(Pair.create("Sexual Health", "http://www.medicinenet.com/rss/general/sexual_health.xml"));
            urls.add(Pair.create("Skin", "http://www.medicinenet.com/rss/general/skin.xml"));
            urls.add(Pair.create("Sleep", "http://www.medicinenet.com/rss/general/sleep.xml"));
            urls.add(Pair.create("Thyroid", "http://www.medicinenet.com/rss/general/thyroid.xml"));
            urls.add(Pair.create("Travel Health", "http://www.medicinenet.com/rss/general/travel_health.xml"));
            urls.add(Pair.create("Women's Health", "http://www.medicinenet.com/rss/general/womens_health.xml"));
        }

        public static ArrayList<Pair<String, String>> getUrls(){
            setup();
            return urls;
        }
    }

}