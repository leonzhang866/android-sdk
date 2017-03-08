package com.yiche.ycanalytics.db;

import java.util.ArrayList;
import java.util.List;

import com.yiche.ycanalytics.bean.EventBean;



/**
 * 数据库操作接口类
 * 
 * @author wanglirong
 * 
 */

public interface IUserDao
{

    /**
     * 根据id来删除
     * @param list
     */
    void deleteEventById(ArrayList<EventBean> list);
    
    /**
     * 所有列表 最多500条
     * @return
     */
    ArrayList<EventBean> getAllEventList();
    
    /**
     * 增加一条事件
     * @param eventBean
     * @return
     */
    boolean addoneEvents(EventBean eventBean);
    
    
    /**
     * 获取数据总数
     * @return
     */
    int geteventTotalNumber();
    
}
