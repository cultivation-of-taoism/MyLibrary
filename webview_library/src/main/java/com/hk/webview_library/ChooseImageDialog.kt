package com.lingniu.shiyingguoji


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import com.blankj.utilcode.util.SDCardUtils
import com.blankj.utilcode.util.ToastUtils
import com.hk.webview_library.R
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.dialog_image.*


/**
 * A simple [Fragment] subclass.
 * PictureSelector 默认只申请了读取文件权限
 * 如果要使用拍照功能，需要申请写入文件权限
 */
class ChooseImageDialog : BaseDialogFragment() {
    lateinit var mUploadMessage: ValueCallback<Array<Uri>>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dialog.window.setGravity(Gravity.BOTTOM)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //HINT 由于点击其他地方关闭弹窗时，不会回调ValueCallback<Array<Uri>>.onReceiveValue(Array<Uri>)
        //HINT 所以要设置弹窗点击其他地方不可取消
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.dialog_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cancel.setOnClickListener {
            dismiss()
            mUploadMessage.onReceiveValue(null)
        }
        tv_photo.setOnClickListener {//拍照时只能使用外置存储卡
            if (Environment.getExternalStorageState()!=Environment.MEDIA_MOUNTED){
                ToastUtils.showShort("SD卡不可用！")
                return@setOnClickListener
            }
            PictureSelector.create(activity)
                    .openCamera(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .setOutputCameraPath("/${Environment.DIRECTORY_DCIM}/Camera/")
                    .enableCrop(true)
                    .circleDimmedLayer(true)
                    .previewImage(true)
                    .isCamera(false)
                    .scaleEnabled(true)
                    .showCropFrame(false)
                    .showCropGrid(false)
                    .hideBottomControls(false)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
            dismiss()
        }
        tv_image.setOnClickListener {//从相册中选择可以使用内置存储卡
            if (!SDCardUtils.isSDCardEnable()){
                ToastUtils.showShort("SD卡不可用！")
                return@setOnClickListener
            }
            PictureSelector.create(activity)
                    .openGallery(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .enableCrop(true)
                    .circleDimmedLayer(true)
                    .previewImage(true)
                    .isCamera(false)
                    .scaleEnabled(true)
                    .showCropFrame(false)
                    .showCropGrid(false)
                    .hideBottomControls(false)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
            dismiss()
        }
    }


}// Required empty public constructor
