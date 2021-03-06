package com.michael.service;

import com.michael.dao.NewsDAO;
import com.michael.model.News;
import com.michael.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by GWC on 2016/7/6.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getlatesNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    public String saveImage(MultipartFile file) throws IOException {
        int doPos = file.getOriginalFilename().lastIndexOf(".");
        if (doPos < 0) {
            return null;
        }

        String fileExt = file.getOriginalFilename().substring(doPos + 1).toLowerCase();// file extentsion name is lowercase
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;//if file extension name is not allowed, return null
        }

        //to filter iilegal words and avoid reduplicated file name, we give a new unique file name here
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

    public int updateLikeCount(int id, int count) {
        return newsDAO.updateLikeCount(id, count);
    }
}
