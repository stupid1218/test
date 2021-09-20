package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean save(Activity activity) {
        boolean flag = true;
        int count = activityDao.save(activity);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO pageList(Map<String, Object> map) {
        //  取得total
        int total = activityDao.getTotalByCondition(map);

        //  取的dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //  将total和dataList封装到Vo中
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //  将Vo返回
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        //  先查询出需要删除的备注数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        //  删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByAids(ids);

        if (count1 != count2) {
            flag = false;
        }

        //  删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length) {
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        //   取uList
        List<User> userList = userDao.getUserList();
        //  取activity
        Activity activity = activityDao.getById(id);
        //  将取uList和取activity打包
        Map<String, Object> map = new HashMap<>();
        map.put("uList", userList);
        map.put("a", activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;
        int count = activityDao.update(activity);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);

        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> aList = activityRemarkDao.getRemarkListByAid(activityId);

        return aList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemark(id);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
        boolean flag = true;

        int count = activityRemarkDao.saveRemark(activityRemark);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(activityRemark);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }
}

