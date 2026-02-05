package com.example.demo.mapper;

import com.example.demo.dao.OrderShipmentDel0205DAO;
import com.example.demo.dao.OrderShipmentInit0205DAO;
import com.example.demo.dao.OrderShipmentQuery0205DAO;
import com.example.demo.dao.OrderShipmentUpd0205DAO;
import com.example.demo.entity.OrderShipment;
import com.example.demo.req.OrderShipmentQuery0205Req;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderShipment0205Repository {

    @PersistenceContext
    private EntityManager em;
    // 注入 EntityManager，用來建立 Criteria 查詢並執行


    //初始化查詢
    public List<OrderShipmentInit0205DAO> init() {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<OrderShipmentInit0205DAO> cq = cb.createQuery(OrderShipmentInit0205DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<OrderShipment> m = cq.from(OrderShipment.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                OrderShipmentInit0205DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("quantity"),
                m.get("shippingAddress"),
                m.get("status")

        ));

        return em.createQuery(cq).getResultList();
    }

    //查詢按鈕
    public List<OrderShipmentQuery0205DAO> query(OrderShipmentQuery0205Req req) {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<OrderShipmentQuery0205DAO> cq = cb.createQuery(OrderShipmentQuery0205DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<OrderShipment> m = cq.from(OrderShipment.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                OrderShipmentQuery0205DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("id"),
                m.get("orderNo"),
                m.get("customerName"),
                m.get("productName"),
                m.get("quantity"),
                m.get("totalPrice"),
                m.get("shippingAddress"),
                m.get("status"),
                m.get("shippedAt"),
                m.get("createdAt")

        ));
        List<Predicate> predicates = new ArrayList<>();


        if (req.getProductName() != null && !req.getProductName().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(m.get("productName")),
                    "%" + req.getProductName().trim().toLowerCase() + "%"
            ));
        }


        if (req.getShippingAddress() != null && !req.getShippingAddress().isEmpty()) {
            predicates.add(cb.equal(m.get("shippingAddress"), req.getShippingAddress()));
        }

        if (req.getStatus() != null && !req.getStatus().isEmpty()) {
            predicates.add(cb.equal(m.get("status"), req.getStatus()));
        }


        // location IN (...)
        if (req.getQuantity() != null && !req.getQuantity().isEmpty()) {
            predicates.add(m.get("quantity").in(req.getQuantity()));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return em.createQuery(cq).getResultList();
    }

    //刪除
    @Transactional
    public int deleteById(OrderShipmentDel0205DAO dao) {
        int rows = 0; // 影響筆數
        OrderShipment entity = em.find(OrderShipment.class, dao.getId());
        if (entity == null) {
            return rows; // 0：找不到資料
        }
        em.remove(entity);
        rows = 1; // 成功刪除一筆
        return rows;
    }



    //新增
    @Transactional
    public int insert(OrderShipment orderShipment) {
        em.persist(orderShipment);
        return 1;
    }



    //修改
    @Transactional
    public int update(OrderShipmentUpd0205DAO dao) {

        // 取得 CriteriaBuilder，用來建立 CriteriaUpdate 與更新條件
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 建立一個針對 ZauthCompanyJpa Entity 的 CriteriaUpdate（UPDATE 語法）
        CriteriaUpdate<OrderShipment> cu =
                cb.createCriteriaUpdate(OrderShipment.class);

        // 指定要更新的 Entity（UPDATE ZauthCompanyJpa）
        Root<OrderShipment> root = cu.from(OrderShipment.class);


        cu.set(root.get("orderNo"), dao.getOrderNo());
        cu.set(root.get("customerName"), dao.getCustomerName());
        cu.set(root.get("productName"), dao.getProductName());
        cu.set(root.get("quantity"), dao.getQuantity());
        cu.set(root.get("totalPrice"), dao.getTotalPrice());
        cu.set(root.get("shippingAddress"), dao.getShippingAddress());
        cu.set(root.get("status"), dao.getStatus());
        cu.set(root.get("shippedAt"), dao.getShippedAt());
        cu.set(root.get("createdAt"), dao.getCreatedAt());


        cu.where(cb.equal(root.get("id"), dao.getId()));


        return em.createQuery(cu).executeUpdate();
    }


}




