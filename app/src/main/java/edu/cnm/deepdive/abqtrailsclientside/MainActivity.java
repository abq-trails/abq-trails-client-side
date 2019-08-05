package edu.cnm.deepdive.abqtrailsclientside;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import edu.cnm.deepdive.abqtrailsclientside.controller.MapsActivity;
import edu.cnm.deepdive.abqtrailsclientside.fragment.TrailViewFragment;
import edu.cnm.deepdive.abqtrailsclientside.model.entity.Trail;
import edu.cnm.deepdive.abqtrailsclientside.model.viewmodel.TrailViewModel;
import edu.cnm.deepdive.abqtrailsclientside.service.AbqTrailsService;

//David Nelson put this here to commit.
public class MainActivity extends AppCompatActivity {

  private TrailViewModel viewModel;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


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
    if (id == R.id.action_maps) {
      Intent intent = new Intent(this, MapsActivity.class);
      startActivity(intent);
//    } else if (id == R.id.action_settings) {
//      // Hack needs to be removed.
      FragmentManager manager = getSupportFragmentManager();
      Fragment fragment = TrailViewFragment.newInstance();
      String tag = fragment.getClass().getSimpleName() + "";
      if (manager.findFragmentByTag(tag) != null) {
        manager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
      }
      FragmentTransaction transaction = manager.beginTransaction();
      transaction.replace(R.id.container, fragment, tag);
      transaction.addToBackStack(tag);
      transaction.commit();
      // End of hack.
      return true;
    }
    // TODO add back in when all is combined
//    if (id == R.id.action_reviews) {
//      Intent intent = new Intent(this, MapsActivity.class);
//      startActivity(intent);    }
//    if (id == R.id.action_upload_profile) {
//      Intent intent = new Intent(this, MapsActivity.class);
//      startActivity(intent);    }
//    if (id == R.id.action_user_profile) {
//      Intent intent = new Intent(this, MapsActivity.class);
//      startActivity(intent);    }
    return super.onOptionsItemSelected(item);

  }

//  private void loadDb() {
//    new Thread(() -> {
//      viewModel.getTrails();
//      viewModel.loadOnlineDb();
//    });
//  }
}
