package com.phuonghn.pkm.service.crawl;

import com.phuonghn.pkm.service.dto.NewsItemDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsCrawlerService {

    public List<NewsItemDTO> crawlNews() {
        List<NewsItemDTO> newsList = new ArrayList<>();

        try {
            for (int i = 1; i <= 10; i++) {
                Document doc = Jsoup.connect("https://pokemondb.net/news/page/" + i).get();
                Element article = doc.select("div.grid-row div.span-md-8").get(0);
                NewsItemDTO newsItem = new NewsItemDTO();
                for (Element child : article.children()) {
                    if ("h2".equals(child.tag().getName())) {
                        newsItem.setTitle(child.select("h2").text());
                        newsItem.setUrl("https://pokemondb.net" + child.select("a").attr("href"));
                    } else if ("p".equals(child.tag().getName())) {
                        if (child.hasClass("pull-up")) {
                            newsItem.setDate(child.select("p").text());
                        } else {
                            newsItem.setContent((newsItem.getContent() == null ? "" : newsItem.getContent()) + child.select("p").text() + " \n ");
                        }
                    } else if ("hr".equals(child.tag().getName())) {
                        newsList.add(newsItem);
                        newsItem = new NewsItemDTO();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsList;
    }
}
