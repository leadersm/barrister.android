package com.lsm.barrister.app;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.Version;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.app.GetLatestVersionReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.ui.activity.BaseActivity;
import com.lsm.barrister.utils.DLog;

import java.io.File;

public class VersionHelper {

    private static final String TAG = "VersionHelper";
    private static final String FILE_NAME = "CP9.apk";

    private long enqueue;
    private DownloadManager dm;

    private VersionHelper() {
    }

    private int versionCode = 0;
    private String versionName;

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void initPackageInfo(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.versionCode = info.versionCode;
            this.versionName = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static VersionHelper instance = null;

    public static VersionHelper instance() {
        if (instance == null) {
            instance = new VersionHelper();
        }
        return instance;
    }

    public void check(Context context, boolean show) {
        showToast = show;
        try {

            initPackageInfo(context);

            forceCheck(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean showToast = true;
    boolean isCheckingUpdate = false;

    public boolean isCheckingUpdate() {
        return isCheckingUpdate;
    }

    ProgressDialog progressDialog = null;

    public void forceCheck(final Context context) {

        isCheckingUpdate = true;

        BaseActivity baseActivity;
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
            progressDialog = baseActivity.progressDialog;
            progressDialog.setMessage(context.getString(R.string.tip_loading));
            progressDialog.show();
        }

        new GetLatestVersionReq(context)
                .setExpire(-1)
                .execute(new Action.Callback<Version>() {

                    @Override
                    public void onError(int errorCode, String msg) {

                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();

                        isCheckingUpdate = false;
                    }

                    @Override
                    public void onCompleted(Version t) {

                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        isCheckingUpdate = false;

                        if (t != null) {

                            DLog.d(TAG, "当前版本：" + versionCode + ">>" + t.toString());

                            if (t.getVersionCode() > versionCode) {
                                //TODO 发现新版本>提示下载
                                showNewVersionDialog(context, t);
                            } else {
                                if (showToast)
                                    Toast.makeText(context, R.string.tip_latest_version,Toast.LENGTH_LONG).show();

                                //取消强制更新检测
                                AppConfig.getInstance().setForceUpdate(false);

                                DLog.d(TAG, "当前是最新版本");
                            }

                        } else {
                            if (showToast)
                                Toast.makeText(context, R.string.tip_no_version,Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void progress() {
                        // TODO Auto-generated method stub

                    }

                });
    }


    /**
     * 显示新版本dialog
     *
     * @param context
     * @param t
     */
    private void showNewVersionDialog(final Context context, final Version t) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title_update_dialog))
                .setMessage("发现新版本v" + t.getVersionName() + "\n" + t.getVersionInfo())
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        download(context, t.getUrl());

                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
            }
        }).create().show();

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

                isDownloading = false;

                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                Query query = new Query();
                query.setFilterById(enqueue);
                Cursor c = dm.query(query);

                if (c.moveToFirst()) {

                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);

                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                        final String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        final String fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

                        DLog.d(TAG, "下载完成：" + uriString + "\nfileName:" + fileName);

                        //安装。
                        install(context, Uri.parse(uriString));

                    } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {

                        UIHelper.showToast(context, R.string.tip_download_failed);

                    }
                }

                h.removeMessages(0);

                context.unregisterReceiver(this);
            }
        }
    };


    public static void install(Context ctx, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    public static void install(Context ctx, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }


    boolean isDownloading = false;

    /**
     * 存在 打开，不存在，下载。
     *
     * @param context
     */
    private void download(Context context, String url) {

        if (isDownloading) {
            Toast.makeText(context, R.string.tip_downloading,Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(url) || !URLUtil.isValidUrl(url)) {
            Toast.makeText(context, R.string.tip_download_url_empty,Toast.LENGTH_LONG).show();
            return;
        }


        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, R.string.tip_sdcard_error,Toast.LENGTH_LONG).show();
            return;
        }

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

        File apk = new File(dir, FILE_NAME);
        if (apk.exists()) {
            apk.delete();
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
        request.setDestinationInExternalPublicDir(Constants.downloadDir, FILE_NAME);

        enqueue = dm.enqueue(request);

//		Intent i = new Intent();
//		i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
//		startActivity(i);

        h.sendEmptyMessageDelayed(0, 100);
    }

    Handler h = new Handler() {
        public void handleMessage(android.os.Message msg) {

            Query query = new Query();
            query.setFilterById(enqueue);
            Cursor c = dm.query(query);
            if (c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_RUNNING == c.getInt(columnIndex)) {

                    isDownloading = true;

                    long sofar = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    long total = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    try {
                        DLog.i(TAG, String.format("正在下载...%d", (int) sofar * 100 / total) + "%");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            c.close();

            h.sendEmptyMessageDelayed(0, 500);
        }
    };


}
