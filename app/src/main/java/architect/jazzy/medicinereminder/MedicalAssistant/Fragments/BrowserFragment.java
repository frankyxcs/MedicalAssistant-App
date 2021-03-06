package architect.jazzy.medicinereminder.MedicalAssistant.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import architect.jazzy.medicinereminder.HelperClasses.Constants;
import architect.jazzy.medicinereminder.MedicalAssistant.Activities.MainActivity;
import architect.jazzy.medicinereminder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowserFragment extends Fragment {


  WebView webView;
  ProgressBar progressBar;
  Uri newsUrl;
  ImageView logo;
  View v;

  public BrowserFragment() {
    // Required empty public constructor
  }

  public static BrowserFragment getInstance(String url) {
    BrowserFragment fragment = new BrowserFragment();
    Bundle args = new Bundle();
    args.putString(Constants.BUNDLE_SELECTED_NEWS, url);
    args.putBoolean("isNews", true);
    fragment.setArguments(args);
    return fragment;
  }

  public static BrowserFragment getInstance(String url, boolean isNews) {
    BrowserFragment fragment = new BrowserFragment();
    Bundle args = new Bundle();
    args.putString(Constants.BUNDLE_SELECTED_NEWS, url);
    args.putBoolean("isNews", isNews);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    v = inflater.inflate(R.layout.fragment_news_detail, container, false);

    try {
      ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    } catch (Exception e) {
      e.printStackTrace();
    }

    boolean b = getArguments().getBoolean("isNews");
    try {
      String title = "";
      if (b) {
        title = "News";
      } else {
        title = "Search Result";
      }
      ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    logo = (ImageView) v.findViewById(R.id.logo);
    newsUrl = Uri.parse(getArguments().getString(Constants.BUNDLE_SELECTED_NEWS));
    progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

    webView = (WebView) v.findViewById(R.id.webView);
    webView.setWebViewClient(new NewsClient());
    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        progressBar.setProgress(newProgress);
        if (newProgress >= 50) {
          logo.setVisibility(View.GONE);
        }

        super.onProgressChanged(view, newProgress);
      }
    });
    webView.loadUrl(String.valueOf(newsUrl));
    try {
      ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return v;
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_common, menu);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).setActivityKeyClickListener(new MainActivity.ActivityKeyClickListener() {
      @Override
      public boolean onBackKeyPressed() {
        if (webView.canGoBack()) {
          webView.goBack();
          return true;
        }
        return false;
      }
    });
  }

  class NewsClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      return super.shouldOverrideUrlLoading(view, url);
    }
  }
}
