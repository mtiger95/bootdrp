package com.bootdo.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

import java.io.IOException;

/**
 * xss 非法标签过滤
 *
 * @author L
 * @since 2026-01-14 09:49
 */
public class JsoupUtil {

    /**
     * 使用自带的 basicWithImages 安全列表
     */
    private static final Safelist SAFELIST = Safelist.basicWithImages();

    /**
     * 配置过滤化参数,不对代码进行格式化
     */
    private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

    static {
        // 富文本编辑时一些样式是使用 style来进行实现的：比如红色字体 style="color:red，所以需要给所有标签添加style属性
        SAFELIST.addAttributes(":all", "style");
    }

    public static String clean(String content) {
        return Jsoup.clean(content, "", SAFELIST, OUTPUT_SETTINGS);
    }

    public static void main(String[] args) throws IOException {
        String text = "<a href=\"http://www.baidu.com/a\" onclick=\"alert(1);\">sss</a><script>alert(0);</script>sss";
        System.out.println(clean(text));
    }

}
