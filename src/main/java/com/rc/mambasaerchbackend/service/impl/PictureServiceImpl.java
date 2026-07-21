package com.rc.mambasaerchbackend.service.impl;

import cn.hutool.json.JSONUtil;
import com.rc.mambasaerchbackend.model.entity.Picture;
import com.rc.mambasaerchbackend.model.vo.PictureVO;
import com.rc.mambasaerchbackend.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public List<PictureVO> fetchPicture(String searchText) {
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=1",searchText);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = doc.select(".iuscp.isv");
        List<PictureVO> pictures = new ArrayList<>();
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
            PictureVO pictureVO = new PictureVO();
            BeanUtils.copyProperties(picture,pictureVO);
            pictures.add(pictureVO);
            System.out.println(pictures);
        }
        return pictures;
    }
}
