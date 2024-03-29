/*
Copyright 2019 Denelle Britton Linebarger, Alana Chigbrow, Anita Martin, David Nelson

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package edu.cnm.deepdive.abqtrailsclientside.model.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;
import edu.cnm.deepdive.abqtrailsclientside.BuildConfig;
import edu.cnm.deepdive.abqtrailsclientside.model.Review;
import edu.cnm.deepdive.abqtrailsclientside.model.dao.TrailDao;
import edu.cnm.deepdive.abqtrailsclientside.model.database.TrailsDatabase;
import edu.cnm.deepdive.abqtrailsclientside.model.entity.Trail;
import edu.cnm.deepdive.abqtrailsclientside.service.AbqTrailsService;
import edu.cnm.deepdive.abqtrailsclientside.service.GoogleSignInService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Gets list of trails.
 */
public class TrailViewModel extends AndroidViewModel implements LifecycleObserver {

  private MutableLiveData<Long> cabqId = new MutableLiveData<>();
  private LiveData<Trail> trail;
  private MutableLiveData<List<Review>> reviews;
  private CompositeDisposable pending = new CompositeDisposable();
  private AbqTrailsService service;
  private TrailsDatabase db;
  private TrailDao trailDao;
  private String oauthHeader;

  /**
   * Initializes this instance with the specified {@link Application}
   */
  public TrailViewModel(@NonNull Application application) {
    super(application);
    service = AbqTrailsService.getInstance();
    oauthHeader = String.format(BuildConfig.AUTHORIZATION_FORMAT,
        GoogleSignInService.getInstance().getAccount().getIdToken());
    db = TrailsDatabase.getInstance(application);
    trailDao = db.trailDao();
    trail = Transformations.switchMap(cabqId, (id) -> trailDao.findById(id));
  }


  public void setCabqId(long cabqId) {
    this.cabqId.setValue(cabqId);
  }

  public LiveData<Trail> getTrail() {
    return trail;
  }

  public LiveData<List<Review>> getReviews(long cabqId) {
    if (reviews == null) {
      reviews = new MutableLiveData<>();
    }
    pending.add(
        service.searchByCabqId(cabqId)
            .subscribeOn(Schedulers.io())
            .subscribe((result) -> reviews.postValue(result))
    );
    return reviews;
  }
  @OnLifecycleEvent(Event.ON_STOP)
  private void deletePending() {
    pending.clear();
  }
    }

