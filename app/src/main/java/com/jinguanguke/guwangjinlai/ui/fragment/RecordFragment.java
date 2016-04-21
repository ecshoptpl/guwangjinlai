package com.jinguanguke.guwangjinlai.ui.fragment;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

//import com.alibaba.sdk.android.AlibabaSDK;
//import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
//import com.duanqu.qupai.sdk.android.QupaiService;
import com.jinguanguke.guwangjinlai.R;


/**
 * Created by jin on 16/4/16.
 */
public class RecordFragment extends BaseFragment{
    @Override protected int getFragmentLayout() {
        return R.layout.fragment_account;
//        QupaiService qupaiService = AlibabaSDK.getService(QupaiService.class);
//        VideoSessionCreateInfo info =new VideoSessionCreateInfo.Builder()
//                .setOutputDurationLimit(Contant.DEFAULT_DURATION_LIMIT)
//                .setOutputVideoBitrate(Contant.DEFAULT_DURATION_LIMIT)
//                .setHasImporter( true)
//                .setWaterMarkPath(Contant.WATER_MARK_PATH)
//                .setWaterMarkPosition(1)
//                .setHasEditorPage(true)
//                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)
//                .setBeautyProgress(80)
//                .setBeautySkinOn(true)
//                .build();
//        qupaiService.initRecord(info);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mToolbar.setTitle(R.string.toolbar_title_account);
    }

    @Override public void onResume() {
        super.onResume();
    }


}
