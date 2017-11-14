package rundi.investmentadviser.entity

/**
 * Created by Administrator on 2017/2/8.
 */
class Token {
    var serial: String? = null
    var token: String? = null
    var publickey: String? = null

    override fun toString(): String {
        return "Token{" +
                "serial='" + serial + '\'' +
                ", token='" + token + '\'' +
                ", publickey='" + publickey + '\'' +
                '}'
    }
}
