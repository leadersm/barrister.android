package com.lsm.barrister.utils;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.lsm.barrister.R;
import com.lsm.barrister.app.Constants;
import com.lsm.barrister.ui.UIHelper;

import java.io.File;
import java.util.HashMap;

public class DownloadHelper {

    private static final String TAG = "DownloadHelper";

    private DownloadManager dm;

    private DownloadHelper() {
    }

    private static DownloadHelper instance = null;

    public static DownloadHelper instance() {
        if (instance == null) {
            instance = new DownloadHelper();
        }
        return instance;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                    Query query = new Query();
                    query.setFilterById(downloadId);
                    Cursor c = dm.query(query);

                    if (c.moveToFirst()) {

                        String url = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));

                        DLog.d(TAG, "download.url:" + url);

                        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        String fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));


                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);

                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            statusMap.remove(url);

                            DLog.d(TAG, "下载完成：" + uriString + "\nfileName:" + fileName);

                            UIHelper.showToast(context, "下载完成:" + fileName);

                        } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {

                            statusMap.remove(url);

                            UIHelper.showToast(context, R.string.tip_download_failed);

                        }
                    }

                    context.unregisterReceiver(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    HashMap<String, Boolean> statusMap = new HashMap<String, Boolean>();

    /**
     * 存在 打开，不存在，下载。
     */
    public void download(Context context, String url) {

        if (statusMap.containsKey(url) && statusMap.get(url)) {
            UIHelper.showToast(context, R.string.tip_downloading);
            return;
        }

        if (TextUtils.isEmpty(url) || !URLUtil.isValidUrl(url)) {
            UIHelper.showToast(context, R.string.tip_download_url_empty);
            return;
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            UIHelper.showToast(context, R.string.tip_sdcard_error);
            return;
        }

        statusMap.put(url, true);

        int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {

            // Cannot download using download manager
            DLog.e(TAG, "无法使用下载器进行下载");
            UIHelper.showWebview(context, url);
            return;
        }

        File dir = Environment.getExternalStoragePublicDirectory(Constants.downloadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, System.currentTimeMillis() + url.substring(url.lastIndexOf(".")));

        if (file.exists()) {
            file.delete();
        }

        DLog.i(TAG, "download dir:" + dir);

        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Request request = new Request(Uri.parse(url));
        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(Request.NETWORK_WIFI | Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle(context.getString(R.string.app_name));
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription(context.getString(R.string.tip_downloading));
        //Set the local destination for the downloaded file to a path within the application's external files directory
//	   request.setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS,FILE_NAME);

        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(Constants.downloadDir, file.getName());

        dm.enqueue(request);

//		Intent i = new Intent();
//		i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
//		startActivity(i);

    }
}
