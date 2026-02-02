package com.example.demo.mapper;

import com.example.demo.dao.CourseDel0202DAO;
import com.example.demo.dao.CourseInit0202DAO;
import com.example.demo.dao.CourseQuery0202DAO;
import com.example.demo.dao.CourseUpd0202DAO;
import com.example.demo.entity.Course;
import com.example.demo.req.CourseQuery0202Req;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Course0202Repository {

    @PersistenceContext
    private EntityManager em;
    // 注入 EntityManager，用來建立 Criteria 查詢並執行


    //初始化查詢
    public List<CourseInit0202DAO> init() {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<CourseInit0202DAO> cq = cb.createQuery(CourseInit0202DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<Course> m = cq.from(Course.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                CourseInit0202DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("level"),
                m.get("maxStudents"),
                m.get("isPublished")

        ));

        return em.createQuery(cq).getResultList();
    }

    //查詢按鈕
    public List<CourseQuery0202DAO> query(CourseQuery0202Req req) {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<CourseQuery0202DAO> cq = cb.createQuery(CourseQuery0202DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<Course> m = cq.from(Course.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                CourseQuery0202DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("id"),
                m.get("title"),
                m.get("teacherName"),
                m.get("description"),
                m.get("price"),
                m.get("level"),
                m.get("maxStudents"),
                m.get("isPublished"),
                m.get("createdAt"),
                m.get("updatedAt")

        ));
        List<Predicate> predicates = new ArrayList<>();


        if (req.getTitle() != null && !req.getTitle().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(m.get("title")),
                    "%" + req.getTitle().trim().toLowerCase() + "%"
            ));
        }


        if (req.getMaxStudents() != null ) {
            predicates.add(cb.equal(m.get("maxStudents"), req.getMaxStudents()));
        }


        if (req.getIsPublished() != null && !req.getIsPublished().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(m.get("isPublished")),
                    "%" + req.getIsPublished().trim().toLowerCase() + "%"
            ));
        }

        // location IN (...)
        if (req.getLevel() != null && !req.getLevel().isEmpty()) {
            predicates.add(m.get("level").in(req.getLevel()));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return em.createQuery(cq).getResultList();
    }

    //刪除
    @Transactional
    public int deleteById(CourseDel0202DAO dao) {
        int rows = 0; // 影響筆數
        Course entity = em.find(Course.class, dao.getId());
        if (entity == null) {
            return rows; // 0：找不到資料
        }
        em.remove(entity);
        rows = 1; // 成功刪除一筆
        return rows;
    }



    //新增
    @Transactional
    public int insert(Course course) {
        em.persist(course);
        return 1;
    }



    //修改
    @Transactional
    public int update(CourseUpd0202DAO dao) {

        // 取得 CriteriaBuilder，用來建立 CriteriaUpdate 與更新條件
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 建立一個針對 ZauthCompanyJpa Entity 的 CriteriaUpdate（UPDATE 語法）
        CriteriaUpdate<Course> cu =
                cb.createCriteriaUpdate(Course.class);

        // 指定要更新的 Entity（UPDATE ZauthCompanyJpa）
        Root<Course> root = cu.from(Course.class);


        cu.set(root.get("title"), dao.getTitle());
        cu.set(root.get("teacherName"), dao.getTeacherName());
        cu.set(root.get("description"), dao.getDescription());
        cu.set(root.get("price"), dao.getPrice());
        cu.set(root.get("level"), dao.getLevel());
        cu.set(root.get("maxStudents"), dao.getMaxStudents());
        cu.set(root.get("isPublished"), dao.getIsPublished());
        cu.set(root.get("createdAt"), dao.getCreatedAt());
        cu.set(root.get("updatedAt"), dao.getUpdatedAt());


        cu.where(cb.equal(root.get("id"), dao.getId()));


        return em.createQuery(cu).executeUpdate();
    }


}




