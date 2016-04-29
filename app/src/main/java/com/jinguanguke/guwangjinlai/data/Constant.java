package com.jinguanguke.guwangjinlai.data;

//import com.alibaba.sdk.android.util.FileUtils;
//import com.jinguanguke.guwangjinlai.util;
import com.jinguanguke.guwangjinlai.util.FileUtils;

/**
 * Created by jin on 16/4/16.
 */
public class Constant {
    /**
     * 默认时长
     */
    public static  int DEFAULT_DURATION_LIMIT = 8;
    /**
     * 默认最小时长
     */
    public static  int DEFAULT_DURATION_MIN = 2;
    /**
     * 默认码率
     */
    public static  int DEFAULT_BITRATE =2000 * 1000;
    /**
     * 默认Video保存路径，请开发者自行指定
     */
    public static  String VIDEOPATH = FileUtils.newOutgoingFilePath();
    /**
     * 默认缩略图保存路径，请开发者自行指定
     */
    public static  String THUMBPATH =  VIDEOPATH + ".jpg";
    /**
     * 水印本地路径，文件必须为rgba格式的PNG图片
     */
    public static  String WATER_MARK_PATH ="assets://Qupai/watermark/qupai-logo.png";

    public static String HOST = "http://www.jinguanguke.com/plus/io/";
}
