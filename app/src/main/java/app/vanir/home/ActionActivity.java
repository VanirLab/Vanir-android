package app.vanir.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import app.vanir.R;

public class ActionActivity extends AppCompatActivity {

  ActionFragment f;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SharedPreferences themePrefs = getSharedPreferences("THEME", 0);
    if (themePrefs.getBoolean("isDark", false))
      setTheme(R.style.DarkTheme);
    else
      setTheme(R.style.AppTheme);
    setContentView(R.layout.main_content);
    if (findViewById(R.id.frame_main) != null) {
      if (savedInstanceState != null) {
        return;
      }
      f = new ActionFragment();
      getSupportFragmentManager().beginTransaction()
              .add(R.id.frame_main, f).commit();
    }
  }

  @Override
  public void onBackPressed() {
    f.onBackPressed();
  }
}
