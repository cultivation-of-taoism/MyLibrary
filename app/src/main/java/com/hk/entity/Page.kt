package rundi.investmentadviser.entity

/**
 * Created by Administrator on 2017/2/13.
 */

data class Page(
        var begin: String,//	起始条数
        var end: String,//	结束条数
        var length: String,//	页面大小
        var total: Int,//	数据总条数
        var pages: String,//	总页数
        var pageNum: String//	当前页数
)
