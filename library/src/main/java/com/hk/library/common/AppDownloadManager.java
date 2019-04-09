package com.hk.library.common;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.lang.ref.WeakReference;

public class AppDownloadManager {
    public static final String TAG = "AppDownloadManager";
    public static final int REQUEST_INSTALL = 9999;
    private WeakReference<Activity> weakReference;
    private DownloadManager mDownloadManager;
    private DownloadChangeObserver mDownLoadChangeObserver;
    private DownloadReceiver mDownloadReceiver;
    private long mReqId = -1;
    private OnDownloadListener mDownloadListener;

    public AppDownloadManager(Activity activity) {
        weakReference = new WeakReference<Activity>(activity);
        mDownloadManager = (DownloadManager) weakReference.get().getSystemService(Context.DOWNLOAD_SERVICE);
        mDownLoadChangeObserver = new DownloadChangeObserver(new Handler());
        mDownloadReceiver = new DownloadReceiver();
    }

    public void setUpdateListener(OnDownloadListener mUpdateListener) {
        this.mDownloadListener = mUpdateListener;
    }

    public void downloadApk(String apkUrl, String title, String desc) {
        //先判断有没有任务在下载
        if (isDownLoading()){
            ToastUtils.showShort("有任务正在下载，请稍后...");
            return;
        }
        // fix bug : 装不了新版本，在下载之前应该删除已有文件
        File apkFile = new File(weakReference.get().getExternalFilesDir(Environment
                .DIRECTORY_DOWNLOADS), weakReference.get().getPackageName()+".apk");

        if (apkFile != null && apkFile.exists()) {
            apkFile.delete();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        //设置title
        request.setTitle(title);
        // 设置描述
        request.setDescription(desc);
        // 完成后显示通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(weakReference.get(), Environment.DIRECTORY_DOWNLOADS,
                weakReference.get().getPackageName()+".apk");
        //在手机SD卡上创建一个download文件夹
        // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir() ;
        //指定下载到SD卡的/download/my/目录下
        // request.setDestinationInExternalPublicDir("/codoon/","codoon_health.apk");

        request.setMimeType("application/vnd.android.package-archive");
        //
        mReqId = mDownloadManager.enqueue(request);
    }

    /**
     * 取消下载
     */
    public void cancel() {
        mDownloadManager.remove(mReqId);
    }

    /**
     * 对应 {@link Activity }
     */
    public void resume() {
        //设置监听Uri.parse("content://downloads/my_downloads")
        weakReference.get().getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true,
                mDownLoadChangeObserver);
        // 注册广播，监听APK是否下载完成
        weakReference.get().registerReceiver(mDownloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 对应{@link Activity#onPause()} ()}
     */
    public void onPause() {
        try{
            weakReference.get().getContentResolver().unregisterContentObserver(mDownLoadChangeObserver);
            weakReference.get().unregisterReceiver(mDownloadReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateView() {
        int[] bytesAndStatus = new int[]{0, 0, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mReqId);
        Cursor c = null;
        try {
            c = mDownloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                //已经下载的字节数
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        if (mDownloadListener != null) {
            mDownloadListener.update(bytesAndStatus[0], bytesAndStatus[1]);
        }

        Log.i(TAG, "下载进度：" + bytesAndStatus[0] + "/" + bytesAndStatus[1] + "");
    }

    class DownloadChangeObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateView();
        }
    }
    //检查下载状态,查看有没有正在下载的任务
    public boolean isDownLoading(){
        if (mReqId!=-1){
            switch (checkStatus()) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    //下载延迟
                case DownloadManager.STATUS_PENDING:
                    //正在下载
                case DownloadManager.STATUS_RUNNING:
                    return true;
            }
        }

        return false;
    }

    private String convertStatus(int status){
        String message = "";
        switch (status) {
            case -1:
                message = "取消下载！";
                break;
            //下载暂停
            case DownloadManager.STATUS_PAUSED:
                message = "下载暂停！";
                break;
            //下载延迟
            case DownloadManager.STATUS_PENDING:
                message = "下载延迟！";
                break;
            //正在下载
            case DownloadManager.STATUS_RUNNING:
                message = "正在下载！";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                message = "下载成功！";
                break;
            case DownloadManager.STATUS_FAILED:
                message = "下载失败！";
                break;
            default:
                message = "未知状态！";
                break;
        }
        return message;
    }

    private int checkStatus(long id) {
        int status = -1;
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(id);
        Cursor c = mDownloadManager.query(query);
        if (c.moveToFirst()) {
            status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
        }
        c.close();
        LogUtils.v("状态:"+status+"->"+convertStatus(status));
        return status;
    }
    private int checkStatus(){
        return checkStatus(mReqId);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_INSTALL)
            if (resultCode != Activity.RESULT_OK)
                if (mDownloadListener!=null)
                    mDownloadListener.onInstallCancel();
    }
    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            LogUtils.e(TAG, "收到广播->"+intent.toString());
            long completeDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            int status = checkStatus(completeDownLoadId);
            ToastUtils.showShort(convertStatus(status));
            if (status!=DownloadManager.STATUS_SUCCESSFUL){
                if (mDownloadListener != null) mDownloadListener.onDownloadComplete(false);
                return;
            }
            if (mDownloadListener != null) mDownloadListener.onDownloadComplete(true);
            boolean haveInstallPermission;
            // 兼容Android 8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                //先获取是否有安装未知来源应用的权限
                haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (!haveInstallPermission) {//没有权限
                    // 弹窗，并去设置页面授权
                    final AndroidOInstallPermissionListener listener = new AndroidOInstallPermissionListener() {
                        @Override
                        public void permissionSuccess() {
                            installApk(context, intent);
                        }

                        @Override
                        public void permissionFail() {
                            if (mDownloadListener!=null)mDownloadListener.onInstallCancel();
                            ToastUtils.showShort("授权失败，无法安装应用");
                        }
                    };

                    AndroidOPermissionActivity.sListener = listener;
                    Intent intent1 = new Intent(context, AndroidOPermissionActivity.class);
                    context.startActivity(intent1);


                } else {
                    installApk(context, intent);
                }
            } else {
                installApk(context, intent);
            }

        }
    }


    /**
     * @param context
     * @param intent
     */
    private void installApk(Context context, Intent intent) {
        long completeDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        LogUtils.e(TAG, "收到广播");
        Uri uri;
        Intent intentInstall = new Intent();
        intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentInstall.setAction(Intent.ACTION_VIEW);

        if (completeDownLoadId == mReqId) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0以下
                uri = mDownloadManager.getUriForDownloadedFile(completeDownLoadId);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // 6.0 - 7.0
                File apkFile = queryDownloadedApk(context, completeDownLoadId);
                uri = Uri.fromFile(apkFile);
            } else { // Android 7.0 以上
                uri = FileProvider.getUriForFile(context,
                        AppUtils.getAppPackageName() + ".provider",
                        new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                                AppUtils.getAppPackageName()+".apk"));
                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            // 安装应用
            LogUtils.e("zhouwei", "下载完成了");

            intentInstall.setDataAndType(uri, "application/vnd.android.package-archive");
            weakReference.get().startActivityForResult(intentInstall, REQUEST_INSTALL);
        }
    }

    //通过downLoadId查询下载的apk，解决6.0以后安装的问题
    public static File queryDownloadedApk(Context context, long downloadId) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    public interface OnDownloadListener {
        /**
         * 更新进度回调
         * @param currentByte 已下载字节数 可能为-1
         * @param totalByte 总字节数 可能为0
         */
        void update(int currentByte, int totalByte);
        void onDownloadComplete(boolean isSuccess);
        void onInstallCancel();
    }

    public interface AndroidOInstallPermissionListener {
        void permissionSuccess();

        void permissionFail();
    }

}
