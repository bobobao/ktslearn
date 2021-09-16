import com.bao.learn.script.kotlin.vo.*
import java.nio.charset.Charset

msg(0x0200) {

    name = "报文名称"
    description = "报文描述"

    fields {
        byte("第一个字节", 0)
        word("第一个字", 1)
        dword("第一个双字", 3)
        val length = byte("字符串长度标志位", 4)
        string("字符串", 5, length, Charset.forName("GBK"))
    }
}