# MyLibrary
自己写的mvp框架，工具类，自定义控件，以及某些第三方库的用法
# 使用方法
tep 1. Add the JitPack repository to your build file 
gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Copy
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.qq2364121253:MyLibrary:v1.1'
	}

