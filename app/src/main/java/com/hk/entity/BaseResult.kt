package rundi.investmentadviser.entity

/**
 * Created by 23641 on 2017/6/28.
 */
open class BaseResult {
    var code = 0
    var page: Page? = null
    var type:Int = 0
    fun isError():Boolean = code != 200
}
