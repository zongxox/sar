package com.example.demo.mapper;

import com.example.demo.dao.*;
import com.example.demo.entity.Article;
import com.example.demo.req.ArticleQuery0130Req;
import com.example.demo.req.ArticleUpdQuery0130Req;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Articles0130Repository {

    @PersistenceContext
    private EntityManager em;
    // 注入 EntityManager，用來建立 Criteria 查詢並執行


    //初始化查詢
    public List<ArticleInit0130DAO> init() {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<ArticleInit0130DAO> cq = cb.createQuery(ArticleInit0130DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<Article> m = cq.from(Article.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                ArticleInit0130DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("author"),
                m.get("category"),
                m.get("status")

        ));

        return em.createQuery(cq).getResultList();
    }

    //查詢按鈕
    public List<ArticleQuery0130DAO> query(ArticleQuery0130Req req) {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<ArticleQuery0130DAO> cq = cb.createQuery(ArticleQuery0130DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<Article> m = cq.from(Article.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                ArticleQuery0130DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("id"),
                m.get("title"),
                m.get("content"),
                m.get("summary"),
                m.get("author"),
                m.get("category"),
                m.get("status"),
                m.get("views"),
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


        if (req.getAuthor() != null && !req.getAuthor().isEmpty()) {
            predicates.add(cb.equal(m.get("author"), req.getAuthor()));
        }


        if (req.getStatus() != null && !req.getStatus().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(m.get("status")),
                    "%" + req.getStatus().trim().toLowerCase() + "%"
            ));
        }

        // location IN (...)
        if (req.getCategory() != null && !req.getCategory().isEmpty()) {
            predicates.add(m.get("category").in(req.getCategory()));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return em.createQuery(cq).getResultList();
    }

    //刪除
    @Transactional
    public int deleteById(ArticleDel0130DAO dao) {
        int rows = 0; // 影響筆數
        Article entity = em.find(Article.class, dao.getId());
        if (entity == null) {
            return rows; // 0：找不到資料
        }
        em.remove(entity);
        rows = 1; // 成功刪除一筆
        return rows;
    }



    //新增
    @Transactional
    public int insert(Article article) {
        em.persist(article);
        return 1;
    }

    //跳轉修改畫面id查詢
    public ArticleUpdQuery0130DAO updQuery(ArticleUpdQuery0130Req req) {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<ArticleUpdQuery0130DAO> cq = cb.createQuery(ArticleUpdQuery0130DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<Article> m = cq.from(Article.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                ArticleUpdQuery0130DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("title"),
                m.get("content"),
                m.get("summary"),
                m.get("author"),
                m.get("category"),
                m.get("status"),
                m.get("views"),
                m.get("createdAt"),
                m.get("updatedAt")

        ));
        cq.where(cb.equal(m.get("id"), Long.valueOf(req.getId())));

        return em.createQuery(cq).getSingleResult();
    }



    //修改
    @Transactional
    public int update(ArticleUpd0130DAO  dao) {

        // 取得 CriteriaBuilder，用來建立 CriteriaUpdate 與更新條件
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 建立一個針對 ZauthCompanyJpa Entity 的 CriteriaUpdate（UPDATE 語法）
        CriteriaUpdate<Article> cu =
                cb.createCriteriaUpdate(Article.class);

        // 指定要更新的 Entity（UPDATE ZauthCompanyJpa）
        Root<Article> root = cu.from(Article.class);


        cu.set(root.get("title"), dao.getTitle());
        cu.set(root.get("content"), dao.getContent());
        cu.set(root.get("summary"), dao.getSummary());
        cu.set(root.get("author"), dao.getAuthor());
        cu.set(root.get("category"), dao.getCategory());
        cu.set(root.get("status"), dao.getStatus());
        cu.set(root.get("views"), dao.getViews());
        cu.set(root.get("createdAt"), dao.getCreatedAt());
        cu.set(root.get("updatedAt"), dao.getUpdatedAt());



        cu.where(cb.equal(root.get("id"), dao.getId()));


        return em.createQuery(cu).executeUpdate();
    }


    //新增
    @Transactional
    public int insert1(Article article) {
        em.persist(article);
        return 1;
    }

}




