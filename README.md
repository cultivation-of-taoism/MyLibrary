# MyLibrary
自己写的mvp框架，工具类，自定义控件，以及某些第三方库的用法
## 使用方法
step 1. Add the JitPack repository to your build file gradle ,
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.qq2364121253:MyLibrary:v1.1.2'
	}
## mvp框架
1. 先看一下view层的接口

		interface IView {
        		fun showSuccess(`object`: Any, task:Int)
        		fun showError(error: String)
        		fun showError(error: Any, task:Int)
        		val mContext: Context
        		var loadProgressDialog: LoadProgressDialog
        		val contentView:View
		}
	
与普通的view层接口不同的是，这里不用根据具体view的功能去写show*** 方法, 而是提供showSuccess、showError两个方法，
无论展示什么数据，都使用这两个方法来展示数据获取成功或失败的情况。

根据task这个参数我们可以区分获取到的是什么数据或者获取什么数据出错了， 而`object`这个参数就是具体获取到的数据，而获取数据失败时error这个参数则
告诉我们失败的原因，这样我们就可以根据不同的数据请求任务展示不同的数据了

view层还对外提供了加载进度对话框 loadProgressDialog和获取view视图的接口 contentView

2. 接着是presenter层接口

          interface IPresenter {
          	fun onSuccess(rs: Any, task:Int)
          	fun onError(error: String)
                fun onError(error: Any, task:Int)
          }
	  
同样，presenter层接口也没有各种on**** Success,on**** Error方法,当数据获取成功时将rs 任意数据、task 任务标识两个参数传递给view层，数据获取失败时， 将error 错误原因、task 任务标识传给view层，同时负责在请求数据时显示loadProgressDialog，数据请求成功或失败时关闭loadProgressDialog。

3. 最后是model层接口

		open class Model (protected var callback: IPresenter) {
    			protected var gson = Gson()
		}
		
model层就比较简单了，持有presenter层的对象，主要任务是负责请求数据，当数据请求成功时，将请求的数据和请求任务标识传给presenter层，当数据请求失败时，
将错误信息和请求任务标识传给presenter层
       
