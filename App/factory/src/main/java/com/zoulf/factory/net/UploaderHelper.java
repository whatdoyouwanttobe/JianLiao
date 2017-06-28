package com.zoulf.factory.net;

import android.text.format.DateFormat;
import android.util.Log;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.zoulf.factory.Factory;
import com.zoulf.utils.HashUtil;
import java.io.File;
import java.util.Date;

/**
 * 上传工具类，用于上传任意文件到阿里OSS存储
 *
 * @author Zoulf.
 */

public class UploaderHelper {

  private static final String TAG = UploaderHelper.class.getSimpleName();

  // 终结点，与存储区域有关系
  private static final String ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";
  // 上传的仓库名
  private static final String BUCKET_NAME = "jian-liao";

  private static OSS getClient() {
    // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
    OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
        "LTAIVyRI82HpCa4U", "1wGZjQ0tRv22F1ND8SMAX7ri2lCtUe");
    return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
  }

  /**
   * 上传最终方法，成功返回一个路径
   *
   * @param objKey 上传上去后，在服务器上独立的KEY
   * @param path 需要上传的文件的路径
   * @return 存储的地址
   */
  private static String upload(String objKey, String path) {
    // 构造一个上传请求
    PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objKey, path);

    try {
      // 初始化上传的Client
      OSS client = getClient();
      // 开始同步上传
      PutObjectResult result = client.putObject(request);
      // 得到一个外网可访问的地址
      String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
      // 格式打印输出
      Log.d(TAG, String.format("PublicObjectURL:%s", url));
      return url;
    } catch (Exception e) {
      e.printStackTrace();
      // 如果有异常则返回空
      return null;
    }
  }

  /**
   * 上传普通图片
   *
   * @param path 本地地址
   * @return 服务器地址
   */
  public static String uploadImage(String path) {
    String key = getImageObjKey(path);
    return upload(key, path);
  }

  /**
   * 上传头像
   *
   * @param path 本地地址
   * @return 服务器地址
   */
  public static String uploadPortrait(String path) {
    String key = getPortraitObjKey(path);
    return upload(key, path);
  }

  /**
   * 上传音频
   *
   * @param path 本地地址
   * @return 服务器地址
   */
  public static String uploadAudio(String path) {
    String key = getAudioObjKey(path);
    return upload(key, path);
  }

  /**
   * 分月存储，避免一个文件夹太多
   *
   * @return yyyyMM
   */
  private static String getDateString() {
    return DateFormat.format("yyyyMM", new Date()).toString();
  }

  // image/201706/dsadhsuaidhsa153dsa.jpg
  public static String getImageObjKey(String path) {
    String fileMD5 = HashUtil.getMD5String(new File(path));
    String dateString = getDateString();
    return String.format("image/%s/%s.jpg", dateString, fileMD5);
  }

  // portrait/201706/dsadhsuaidhsa153dsa.jpg
  public static String getPortraitObjKey(String path) {
    String fileMD5 = HashUtil.getMD5String(new File(path));
    String dateString = getDateString();
    return String.format("portrait/%s/%s.jpg", dateString, fileMD5);
  }

  // audio/201706/dsadhsuaidhsa153dsa.mp3
  public static String getAudioObjKey(String path) {
    String fileMD5 = HashUtil.getMD5String(new File(path));
    String dateString = getDateString();
    return String.format("audio/%s/%s.mp3", dateString, fileMD5);
  }

}
