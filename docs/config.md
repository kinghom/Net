全局配置建议在Application的onCreate函数中配置

## 初始配置

=== "普通初始化"

    ```kotlin
    class App : Application() {
        override fun onCreate() {
            super.onCreate()
    
            // https://github.com/liangjingkanji/Net/  这是接口全局域名, 可以使用NetConfig.host进行单独的修改
            NetConfig.initialize("https://github.com/liangjingkanji/Net/", this) {
                // 超时配置, 默认是10秒, 设置太长时间会导致用户等待过久
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
                setDebug(BuildConfig.DEBUG)
                setConverter(SerializationConverter())
            }
        }
    }
    ```

=== "OkHttpClient.Builder"

    ```kotlin
    class App : Application() {
        override fun onCreate() {
            super.onCreate()
            // https://github.com/liangjingkanji/Net/  这是接口全局域名, 可以使用NetConfig.host进行单独的修改
            val okHttpClientBuilder = OkHttpClient.Builder()
                .setDebug(BuildConfig.DEBUG)
                .setConverter(SerializationConverter())
                .addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
    
            NetConfig.initialize("https://github.com/liangjingkanji/Net/", this, okHttpClientBuilder)
        }
    }
    ```

> 配置都是可选项, 不是不初始化就不能使用. 如果你是Xposed或者多进程项目中必须初始化传入上下文或者赋值 `NetConfig.app = this`

在initNet函数作用域中的this属于`OkHttpClient.Builder()`, 可以配置任何OkHttpClient.Builder的属性以外还支持以下Net独有配置

| 函数 | 描述 |
|-|-|
| setDebug | 是否输出网络日志, 和`LogRecordInterceptor`互不影响  |
| setSSLCertificate | 配置Https证书 |
| trustSSLCertificate | 信任所有Https证书 |
| setConverter | [配置数据转换器](converter.md), 将网络返回的数据转换成你想要的数据结构 |
| setRequestInterceptor | [配置请求拦截器](interceptor.md), 适用于添加全局请求头/参数 |
| setErrorHandler | [配置全局错误处理](error-global.md) |
| setDialogFactory | [配置全局对话框](auto-dialog.md) |

## 重试次数

默认情况下你设置超时时间即可, OkHttp内部也有重试机制.

但是个别开发者需求指定重试次数则可以添加`RetryInterceptor`拦截器即可实现失败以后会重试指定次数

```kotlin
NetConfig.initialize("https://github.com/liangjingkanji/Net/", this) {
    // ... 其他配置
    addInterceptor(RetryInterceptor(3)) // 如果全部失败会重试三次
}
```


## 修改配置

[NetConfig](api/-net/com.drake.net/-net-config/index.html)的字段即为全局配置, 可随时修改

```kotlin
NetConfig.converter = MyNewConverter() // 修改全局数据转换器
NetConfig.okHttpClient // 修改全局默认客户端
```

更多请访问源码查看

## BaseUrl

这个概念来源于Retrofit, 因为Retrofit无法动态修改Host, 但是这个Net支持随时修改. 以下介绍三种修改方式

1) 直接修改

```kotlin
NetConfig.host = "新的BaseUrl"
```


2) 传入路径
如传入参数为路径(例如`/api/index`)会自动和`host`拼接组成完成URL, 但如果传入的以`http/https`开头的全路径则会直接作为请求URL

```kotlin
scopeNetLife {
    val data = Get<String>("https://github.com/liangjingkanji/Net/").await()
}
```

3) 使用拦截器

或者通过指定`tag`, 然后拦截器(interceptor)中根据tag动态修改host, 因为拦截器能修改一切请求参数

```kotlin
scopeNetLife {
    val data = Get<String>("/api/index", "User").await() // User即为tag
}
// 拦截器修改请求URL不做介绍
```

## 多域名

```kotlin
scopeNetLife {
    Get<String>("https://github.com/liangjingkanji/Net/").await() // 传入域名就会使用当前域名
    Get<String>("/liangjingkanji/Net/").await() // 自动和NetConfig.host拼接
    Get<String>(Api.Host2 + "/liangjingkanji/Net/").await() // 自己手动拼接
}
```

和BaseUrl一样你还可以在拦截器里面统一处理, 在拦截器里面判断tag或者path来拼接域名


