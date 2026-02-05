package com.example.demo.mapper;

import com.example.demo.dao.*;
import com.example.demo.entity.TaskSchedule;
import com.example.demo.req.TaskScheduleQuery0204Req;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskSchedule0204Repository {

    @PersistenceContext
    private EntityManager em;
    // 注入 EntityManager，用來建立 Criteria 查詢並執行


    //初始化查詢
    public List<TaskSchedule0204DAO> init() {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<TaskSchedule0204DAO> cq = cb.createQuery(TaskSchedule0204DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<TaskSchedule> m = cq.from(TaskSchedule.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                TaskSchedule0204DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("status"),
                m.get("type"),
                m.get("location")

        ));

        return em.createQuery(cq).getResultList();
    }

    //查詢按鈕
    public List<TaskScheduleQuery0204DAO> query(TaskScheduleQuery0204Req req) {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<TaskScheduleQuery0204DAO> cq = cb.createQuery(TaskScheduleQuery0204DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<TaskSchedule> m = cq.from(TaskSchedule.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                TaskScheduleQuery0204DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("id"),
                m.get("title"),
                m.get("status"),
                m.get("type"),
                m.get("amount"),
                m.get("priority"),
                m.get("remark"),
                m.get("location"),
                m.get("startTime"),
                m.get("endTime")

        ));
        List<Predicate> predicates = new ArrayList<>();


        if (req.getTitle() != null && !req.getTitle().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(m.get("title")),
                    "%" + req.getTitle().trim().toLowerCase() + "%"
            ));
        }


        if (req.getType() != null && !req.getType().isEmpty()) {
            predicates.add(cb.equal(m.get("type"), req.getType()));
        }

        if (req.getStatus() != null && !req.getStatus().isEmpty()) {
            predicates.add(cb.equal(m.get("status"), req.getStatus()));
        }


        // location IN (...)
        if (req.getLocation() != null && !req.getLocation().isEmpty()) {
            predicates.add(m.get("location").in(req.getLocation()));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return em.createQuery(cq).getResultList();
    }

    //刪除
    @Transactional
    public int deleteById(TaskScheduleDel0204DAO dao) {
        int rows = 0; // 影響筆數
        TaskSchedule entity = em.find(TaskSchedule.class, dao.getId());
        if (entity == null) {
            return rows; // 0：找不到資料
        }
        em.remove(entity);
        rows = 1; // 成功刪除一筆
        return rows;
    }



    //新增
    @Transactional
    public int insert(TaskSchedule taskSchedule) {
        em.persist(taskSchedule);
        return 1;
    }



    //修改
    @Transactional
    public int update(TaskScheduleUpd0204DAO dao) {

        // 取得 CriteriaBuilder，用來建立 CriteriaUpdate 與更新條件
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 建立一個針對 ZauthCompanyJpa Entity 的 CriteriaUpdate（UPDATE 語法）
        CriteriaUpdate<TaskSchedule> cu =
                cb.createCriteriaUpdate(TaskSchedule.class);

        // 指定要更新的 Entity（UPDATE ZauthCompanyJpa）
        Root<TaskSchedule> root = cu.from(TaskSchedule.class);


        cu.set(root.get("title"), dao.getTitle());
        cu.set(root.get("status"), dao.getStatus());
        cu.set(root.get("type"), dao.getType());
        cu.set(root.get("amount"), dao.getAmount());
        cu.set(root.get("priority"), dao.getPriority());
        cu.set(root.get("remark"), dao.getRemark());
        cu.set(root.get("location"), dao.getLocation());
        cu.set(root.get("startTime"), dao.getStartTime());
        cu.set(root.get("endTime"), dao.getEndTime());


        cu.where(cb.equal(root.get("id"), dao.getId()));


        return em.createQuery(cu).executeUpdate();
    }





}




