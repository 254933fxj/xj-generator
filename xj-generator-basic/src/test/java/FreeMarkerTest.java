
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FreeMarkerTest {
    @Test
    public void config() throws IOException, TemplateException {
        //new出Configuration对象，参数为FreeMarker版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        //指定模板文件所在路径
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
        //设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");
        configuration.setNumberFormat("0.######");
        //创建模板对象，加载指定模板引擎
        Template template = configuration.getTemplate("myweb.html.ftl");
        //创建数据模型
        Map<String,Object> dataModel = new HashMap<>();
        dataModel.put("currentYear",2024);

        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("url","http://codefather.cn");
        map1.put("label","编程导航");

        Map<String,Object> map2 = new HashMap<>();
        map2.put("url","http://laoyujianli.com");
        map2.put("label","老鱼简历");
        list.add(map1);
        list.add(map2);
        dataModel.put("menuItems",list);

        Writer writer = new FileWriter("myweb.html");
        template.process(dataModel,writer);
        writer.close();
    }
}
