package com.sendtion.poteviodemo.http;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.sendtion.poteviodemo.entry.ArticleDataEntry;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 接口请求工具类
 */
public class RequestUtils {
    private static RequestUtils requestUtils;
    private static final Lifecycle.Event UTIL_EVENT = Lifecycle.Event.ON_DESTROY;
    private LifecycleOwner owner;

    private RequestUtils(LifecycleOwner owner){
        this.owner = owner;
    }

    /**
     * 单例模式，传入LifecycleOwner
     */
    public static RequestUtils getInstance(LifecycleOwner owner){
        if (requestUtils == null) {
            synchronized (RequestUtils.class){
                if (requestUtils == null){
                    requestUtils = new RequestUtils(owner);
                }
            }
        }
        return requestUtils;
    }

    /**
     * 统一处理
     */
    private <T> void toSubscribe(Observable<T> observable, Observer<T> observer){
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, UTIL_EVENT)))
                .subscribe(observer);
    }

    //**************************************************************************************

    /**
     * Get 请求demo
     */
    public void listRepos(int page, BaseObserver<ArticleDataEntry> observer){
        Observable<BaseResponse<ArticleDataEntry>> observable = RetrofitUtils.getInstance().getService().listRepos2(page);
        toSubscribe(observable, observer);
//        RetrofitUtils.getInstance().getService()
//                .listRepos2(page)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))
//                .subscribe(observer);
    }

//    /**
//     * Post 请求demo
//     */
//    public static void postDemo(RxAppCompatActivity context, String name, String password, Observer<Demo> consumer){
//        RetrofitUtils.getApiUrl()
//                .postUser(name,password).compose(RxLifecycleHelper.observableIO2Main(context))
//                .subscribe(consumer);
//    }
//
//    /**
//     * Put 请求demo
//     */
//    public static void putDemo(RxFragment context, String access_token,Observer<Demo> consumer){
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Accept","application/json");
//        headers.put("Authorization",access_token);
//        RetrofitUtils.getApiUrl()
//                .put(headers,"厦门").compose(RxLifecycleHelper.observableIO2Main(context))
//                .subscribe(consumer);
//    }
//
//    /**
//     * Delete 请求demo
//     */
//    public static void deleteDemo(RxFragment context, String access_token,Observer<Demo> consumer){
//        RetrofitUtils.getApiUrl()
//                .delete(access_token,1).compose(RxLifecycleHelper.observableIO2Main(context))
//                .subscribe(consumer);
//    }
//
//    /**
//     * 上传图片
//     */
//    public static void upImagView(RxFragment context, String  access_token,String str, Observer<Demo>  observer){
//        File file = new File(str);
////        File file = new File(imgPath);
//        Map<String,String> header = new HashMap<String, String>();
//        header.put("Accept","application/json");
//        header.put("Authorization",access_token);
////        File file =new File(filePath);
//        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
////        RequestBody requestFile =
////                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("file", file.getName(), reqFile);
//        RetrofitUtils.getApiUrl().uploadImage(header,body).compose(RxLifecycleHelper.observableIO2Main(context))
//                .subscribe(observer);
//    }
//
//    /**
//     * 上传多张图片
//     */
//    public static void upLoadImg(RxFragment context, String access_token, List<File> files, Observer<Demo>  observer1){
//        Map<String,String> header = new HashMap<String, String>();
//        header.put("Accept","application/json");
//        header.put("Authorization",access_token);
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);//表单类型
//        for (int i = 0; i < files.size(); i++) {
//            File file = files.get(i);
//            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
//            builder.addFormDataPart("file", file.getName(), photoRequestBody);
//        }
//        List<MultipartBody.Part> parts = builder.build().parts();
//        RetrofitUtils.getApiUrl().uploadImage1(header,parts).compose(RxLifecycleHelper.observableIO2Main(context))
//                .subscribe(observer1);
//    }
}
