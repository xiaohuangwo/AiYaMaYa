package zsx.com.aiyamaya.model;

import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import zsx.com.aiyamaya.api.OkHttpHelp;
import zsx.com.aiyamaya.item.ResultItem;
import zsx.com.aiyamaya.listener.ResponseListener;
import zsx.com.aiyamaya.util.Constant;
import zsx.com.aiyamaya.util.MyUtil;
import zsx.com.aiyamaya.util.SpfUtil;

/**
 * Created by moram on 2017/1/6.
 */

public class UploadImageModel {

    private static final String TAG = "UploadImageModel";
    private String path;
    private Handler handler;

    public UploadImageModel(String path, Handler handler) {
        this.path = path;
        this.handler = handler;
    }

    public void imageUpload() {
        Map<String, String> params = new HashMap<>();
        params.put("image", MyUtil.fileBase64String(path));
        OkHttpHelp<ResultItem> httpHelp = OkHttpHelp.getInstance();
        httpHelp.httpRequest("", Constant.IMAGE_UPLOAD_URL, params, new ResponseListener<ResultItem>() {
            @Override
            public void onSuccess(ResultItem object) {
                Message msg = Message.obtain();
                msg.what = Constant.IMAGE_UPLOAD_OK;
                msg.obj = object;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailed(String message) {
                handler.sendEmptyMessage(Constant.IMAGE_UPLOAD_FAIL);
            }

            @Override
            public Class<ResultItem> getEntityClass() {
                return ResultItem.class;
            }
        });
    }

}