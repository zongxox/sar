package com.example.demo.mapper;

import com.example.demo.dao.ProductDel0203DAO;
import com.example.demo.dao.ProductInit0203DAO;
import com.example.demo.dao.ProductQuery0203DAO;
import com.example.demo.dao.ProductUpd0203DAO;
import com.example.demo.entity.Product;
import com.example.demo.req.ProducQuery0203Req;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Product0203Repository {

    @PersistenceContext
    private EntityManager em;
    // 注入 EntityManager，用來建立 Criteria 查詢並執行


    //初始化查詢
    public List<ProductInit0203DAO> init() {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<ProductInit0203DAO> cq = cb.createQuery(ProductInit0203DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<Product> m = cq.from(Product.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                ProductInit0203DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("status"),
                m.get("category"),
                m.get("brand")

        ));

        return em.createQuery(cq).getResultList();
    }

    //查詢按鈕
    public List<ProductQuery0203DAO> query(ProducQuery0203Req req) {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<ProductQuery0203DAO> cq = cb.createQuery(ProductQuery0203DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<Product> m = cq.from(Product.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                ProductQuery0203DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("id"),
                m.get("name"),
                m.get("description"),
                m.get("price"),
                m.get("stock"),
                m.get("category"),
                m.get("brand"),
                m.get("sku"),
                m.get("status"),
                m.get("createdTime"),
                m.get("updatedTime")

        ));
        List<Predicate> predicates = new ArrayList<>();


        if (req.getName() != null && !req.getName().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(m.get("name")),
                    "%" + req.getName().trim().toLowerCase() + "%"
            ));
        }


        if (req.getBrand() != null && !req.getBrand().isEmpty()) {
            predicates.add(cb.equal(m.get("brand"), req.getBrand()));
        }

        if (req.getStatus() != null && !req.getStatus().isEmpty()) {
            predicates.add(cb.equal(m.get("status"), req.getStatus()));
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
    public int deleteById(ProductDel0203DAO dao) {
        int rows = 0; // 影響筆數
        Product entity = em.find(Product.class, dao.getId());
        if (entity == null) {
            return rows; // 0：找不到資料
        }
        em.remove(entity);
        rows = 1; // 成功刪除一筆
        return rows;
    }



    //新增
    @Transactional
    public int insert(Product product) {
        em.persist(product);
        return 1;
    }



    //修改
    @Transactional
    public int update(ProductUpd0203DAO dao) {

        // 取得 CriteriaBuilder，用來建立 CriteriaUpdate 與更新條件
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 建立一個針對 ZauthCompanyJpa Entity 的 CriteriaUpdate（UPDATE 語法）
        CriteriaUpdate<Product> cu =
                cb.createCriteriaUpdate(Product.class);

        // 指定要更新的 Entity（UPDATE ZauthCompanyJpa）
        Root<Product> root = cu.from(Product.class);


        cu.set(root.get("name"), dao.getName());
        cu.set(root.get("description"), dao.getDescription());
        cu.set(root.get("price"), dao.getPrice());
        cu.set(root.get("stock"), dao.getStock());
        cu.set(root.get("category"), dao.getCategory());
        cu.set(root.get("brand"), dao.getBrand());
        cu.set(root.get("sku"), dao.getSku());
        cu.set(root.get("status"), dao.getStatus());
        cu.set(root.get("createdTime"), dao.getCreatedTime());
        cu.set(root.get("updatedTime"), dao.getUpdatedTime());


        cu.where(cb.equal(root.get("id"), dao.getId()));


        return em.createQuery(cu).executeUpdate();
    }


}




