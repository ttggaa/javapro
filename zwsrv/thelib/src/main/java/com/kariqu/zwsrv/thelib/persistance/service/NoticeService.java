package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.NoticeDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by simon on 18/04/17.
 */
@Service
public class NoticeService extends BaseService<Notice> {

    @Autowired
    private NoticeDAO noticeDAO;

    @Override
    protected JpaRepository<Notice, Integer> getDao() {
        return noticeDAO;
    }


    public List<Notice> findNotices(int userId, int page, int size) {
        return noticeDAO.findNotices(userId,new PageRequest(page, size, Sort.Direction.DESC, "createTime"));
    }

}
