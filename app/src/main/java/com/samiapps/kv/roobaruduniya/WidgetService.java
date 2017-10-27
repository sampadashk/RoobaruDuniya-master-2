package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by KV on 24/8/17.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
