package com.rc.mambasaerchbackend;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.rc.mambasaerchbackend.model.entity.Article;
import com.rc.mambasaerchbackend.model.entity.Picture;
import com.rc.mambasaerchbackend.service.ArticleService;
import jakarta.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrwalerTest {

    @Resource
    private ArticleService articleService;

    @Test
    void testFetch() {
        String json = "{\"reviewStatus\":2,\"needNotInterests\":true,\"hiddenContent\":true,\"needCursor\":true,\"needFilterVipContent\":true,\"needOnlyRecommend\":true,\"queryType\":\"recommend\",\"cursorList\":[{\"field\":\"recommendTime\",\"asc\":false},{\"field\":\"id\",\"asc\":false}]}";
        String url = "https://api.codefather.cn/api/recommend/list/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute().body();
        System.out.println(result);

        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");

        ArrayList<Article> articleList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Article article = new Article();
            article.setTitle(tempRecord.getStr("title"));
            article.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            article.setTags(JSONUtil.toJsonStr(tagList));
            article.setUserId(1L);
            articleList.add(article);
        }
        System.out.println(articleList);

        boolean b = articleService.saveBatch(articleList);
    }

    @Test
    void testFetchPicture() {
        int current = 1;
        String url = "https://cn.bing.com/images/search?q=小黑子&first=" + current;
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            //取图片地址(murl)
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            System.out.println(murl);
            //取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            System.out.println(title);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
            System.out.println(pictures);
        }

    }
}
