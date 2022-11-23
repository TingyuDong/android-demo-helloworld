package com.thoughtworks.androidtrain.utils

import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Looper
import okhttp3.Cache
import okhttp3.Callback
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Call
import okhttp3.Request
import okhttp3.Response
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody
import okhttp3.MultipartBody
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import okio.Buffer
import okio.ForwardingSink
import okio.IOException
import okio.Sink
import okio.buffer
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.collections.HashMap
import kotlin.properties.Delegates


/**
 * author : Jeff  5899859876@qq.com
 * Csdn :https://blog.csdn.net/Jeff_YaoJie
 * Github: https://github.com/Jay-YaoJie
 * Created :  2019-08-17.
 * description ： OkHttp再次封装 借鉴https://github.com/zskingking/OkHttpUtils
 *
 *   implementation 'com.squareup.okhttp3:okhttp:4.1.0' //  //网络请求框架 https://github.com/square/okhttp
 *   implementation 'com.squareup.okio:okio:2.3.0' //i/0 流处理 https://github.com/square/okio
 * 使用方式:
 * 1. 先初始化 OkHttpUtils（context）
 * 2. 直接使用 mOkHttpUtils.{你想要使用的get 、 post 或 文件上传方法 }
 *       如 var strJson = mOkHttpUtils.get_Sync("http://t.weather.sojson.com/api/weather/city/101030100")

 *
 * OkHttp再次封装 https://github.com/zskingking/OkHttpUtils
 *  * 1.OkhttpClient为网络请求的一个中心，它会管理连接池、缓存、SocketFactory、代理
 *   、各种超时时间、DNS、请求执行结果的分发等许多内容。
 * 2.Request：Request是一个HTTP请求体，比如请求方法GET/POST、URL、Header、Body
 *   请求的换粗策略等。
 * 3.Call：通过OkhttpClient和Request来创建Call，Call是一个Task，它会执行网络请求
 *   并且获得响应。这个Task可以通过execute()同步执行，阻塞至请求成功。也可以通过
 *   enqueue()异步执行，会将Call放入一个异步执行队列，由ExecutorService后台执行。
 */
open class OkHttpUtils(context: Context) {
    companion object {
        var mOkHttpUtils: OkHttpUtils by Delegates.notNull()
    }

    init {
        //初始化okhttp对象
        mOkHttpUtils = into(context)
    }

    @Volatile
    private lateinit var mOkHttpClient: OkHttpClient
    private lateinit var mHandler: Handler

    /**
     * 初始化okhttp对象
     * @param context: Context
     * @return OkHttpUtils
     */
    private fun into(context: Context): OkHttpUtils {
        //缓存的文件夹
        val fileCache = File(context.externalCacheDir, "response")
        val cacheSize: Long = 10 * 1024 * 1024//缓存大小为10M
        val cache = Cache(fileCache, cacheSize)
        //进行OkHttpClient的一些设置
        mOkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)//设置缓存
            .cache(cache)
            .build()
        mHandler = Handler(Looper.getMainLooper())
        return this
    }


    /**
     * GET同步请求
     * @param url: String
     * @return String?
     */
    open fun getSync(url: String): String? {
        val request = Request.Builder()
            .get()
            .url(url)
            .build()
        val call = mOkHttpClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response.body?.string()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * GET异步请求
     * @param url: String
     * @param callback: OkHttpCallback
     */
    open fun getAsync(url: String, callback: OkHttpCallback) {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //在UI线程中执行回调
                mHandler.post { callback.onError(e) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //在UI线程中执行回调
                mHandler.post { callback.onResponse(response) }
            }
        })
    }

    /**
     * POST同步JSON
     * @param url: String
     * @param json: String
     * @return  String?
     */
    open fun postSyncJSON(url: String, json: String): String? {
        val mediaType = "application/json;charset=utf-8".toMediaTypeOrNull()
        val body = json.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val call = mOkHttpClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response.body!!.string()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * POST同步FORM
     * @param url: String
     * @param params: Map<String, String>
     * @return String?
     */
    fun potsSyncForm(url: String, params: Map<String, String>): String? {

        val body = buildParams(params)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val call = mOkHttpClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response.body!!.string()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * POST异步JSON
     * @param url: String
     * @param  json: String
     * @param callback: OkHttpCallback
     */
    open fun postAsyncJSON(url: String, json: String, callback: OkHttpCallback) {

        val mediaType = "application/json;charset=utf-8".toMediaTypeOrNull()
        val body = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.post { callback.onError(e) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                mHandler.post { callback.onResponse(response) }
            }
        })
    }

    /**
     * POST异步FORM
     * @param url: String, params
     * @param params: Map<String, String>
     * @param  callback: OkHttpCallback
     */
    open fun postAsyncForm(url: String, params: Map<String, String>, callback: OkHttpCallback) {

        val body = buildParams(params)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.post { callback.onError(e) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                mHandler.post { callback.onResponse(response) }
            }
        })
    }

    /**
     * 异步加载文件
     * @param url: String
     * @param callback: OkHttpCallback
     */
    open fun asyncLoadFile(url: String, callback: OkHttpCallback) {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.post { callback.onError(e) }

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                mHandler.post {
                    //下载文件进度条可以直接在onResponse中实现
                    val `is` = response.body!!.byteStream()
                    //文件的总大小(单位字节)
                    val contentLength = response.body!!.contentLength()

                    var sum: Long = 0//当前下载到的字节量
                    val file = File(Environment.getExternalStorageDirectory(), "girl.png")
                    val fos: FileOutputStream
                    try {
                        fos = FileOutputStream(file)
                        //数组越小进度的密度越高
                        val bytes = ByteArray(128)
                        var len = `is`.read(bytes)
                        while (len != -1) {
                            fos.write(bytes, 0, len)
                            sum += len.toLong()
                            len = `is`.read(bytes)
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    /**
     * 异步文件参数混合上传
     * @param  url: String
     * @param params: Map<String, String>?
     * @param  file: File
     * @param  callback: OkHttpCallback 响应回调
     * @param   progressListener: ProgressRequestBody 进度回调
     */
    open fun asyncUploadFileAndParams(
        url: String, params: Map<String, String>?, file: File, callback: OkHttpCallback,
        progressListener: ProgressRequestBody.ProgressListener
    ) {
        //表示任意二进制流
        val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        //因为是文件参数混合上传，所以要分开构建
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if (params != null) {
            for ((key, value) in params) {
                builder.addFormDataPart(key, value)
            }

        }
        val multipartBody = builder
            //key需要服务器提供，相当于键值对的键
            .addFormDataPart("image", file.name, requestBody)
            .build()
        val countingRequestBody = ProgressRequestBody(multipartBody, progressListener)
        val request = Request.Builder()
            .url(url)
            .post(countingRequestBody)
            .build()
        val call = mOkHttpClient.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.post { callback.onError(e) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                mHandler.post { callback.onResponse(response) }
            }
        })

    }

    /**
     * 参数添加到表单中
     * @param param: Map<String, String>?
     * @return RequestBody
     */
    private fun buildParams(param: Map<String, String>?): RequestBody {
        var params: Map<String, String>? = param
        if (params == null) {
            params = HashMap()
        }
        val builder = FormBody.Builder()
        for (entry in params.entries) {
            builder.add(entry.key, entry.value)
        }
        return builder.build()
    }

    /**
     * 根据tag取消单个请求
     * 最终的取消时通过拦截器RetryAndFollowUpInterceptor进行的
     * @param call: Call
     */
    open fun cancel(call: Call) {
        //queuedCalls()代表所有准备运行的异步任务
        for (dispatcherCal1 in mOkHttpClient.dispatcher.queuedCalls()) {
            if (call.request().tag()!! == call.request().tag()) {
                call.cancel()
            }
        }
        //runningCalls()代表所有正在运行的任务(包括同步和异步)
        for (dispatcherCal1 in mOkHttpClient.dispatcher.runningCalls()) {
            if (call.request().tag()!! == call.request().tag()) {
                call.cancel()
            }
        }
    }

    /**
     * 取消全部请求
     */
    open fun cancelAll() {
        mOkHttpClient.dispatcher.cancelAll()
    }

    interface OkHttpCallback {
        fun onResponse(response: Response)
        fun onError(e: IOException)
    }


    /*文件加载监听*/
    class ProgressRequestBody(
        body: RequestBody,
        listener: ProgressListener
    ) : RequestBody() {
        private var mListener: ProgressListener? = listener
        private var mBody: RequestBody? = body
        private var mProgressSink: ProgressSink? = null
        private var mBufferedSink: BufferedSink? = null

        override fun contentType(): MediaType? {
            return mBody!!.contentType()
        }

        override fun writeTo(sink: BufferedSink) {
            //将Sink重新构造
            mProgressSink = ProgressSink(sink)
            if (mBufferedSink == null) {
                //创建输出流体系  mBufferedSink = Okio.buffer(mProgressSink);
                mBufferedSink = mProgressSink!!.buffer()
            }
            //进行流输出操作
            mBody!!.writeTo(mBufferedSink!!)
            mBufferedSink!!.flush(); }

        override fun contentLength(): Long {
            return try {
                mBody!!.contentLength()
            } catch (e: IOException) {
                -1
            }

        }

        internal inner class ProgressSink(delegate: Sink) : ForwardingSink(delegate) {
            private var byteWrite: Long = 0//当前写入的字节

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                //必须执行父类方法，否则无法上传
                super.write(source, byteCount)
                byteWrite += byteCount
                if (mListener != null) {
                    //更新进度
                    mListener!!.onProgress(byteWrite, contentLength())
                }
            }
        }

        interface ProgressListener {
            fun onProgress(byteWrite: Long, contentLength: Long)
        }
    }

}

