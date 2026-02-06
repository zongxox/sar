package com.example.demo.mapper;

import com.example.demo.dao.OrderShipmentUpd0205DAO;
import com.example.demo.dao.TicketDel0206DAO;
import com.example.demo.dao.TicketInit0206DAO;
import com.example.demo.dao.TicketQuery0206DAO;
import com.example.demo.entity.OrderShipment;
import com.example.demo.entity.Ticket;
import com.example.demo.req.TicketQuery0206Req;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Ticket0206Repository {

    @PersistenceContext
    private EntityManager em;
    // 注入 EntityManager，用來建立 Criteria 查詢並執行


    //初始化查詢
    public List<TicketInit0206DAO> init() {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<TicketInit0206DAO> cq = cb.createQuery(TicketInit0206DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<Ticket> m = cq.from(Ticket.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                TicketInit0206DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("title"),
                m.get("category"),
                m.get("status")

        ));

        return em.createQuery(cq).getResultList();
    }

    //查詢按鈕
    public List<TicketQuery0206DAO> query(TicketQuery0206Req req) {
        // 查詢方法：依評估日期 + 公司代號查詢歷程資料，回傳 DTO List

        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 取得 CriteriaBuilder，用來建立條件（WHERE、ORDER BY 等）

        CriteriaQuery<TicketQuery0206DAO> cq = cb.createQuery(TicketQuery0206DAO.class);
        // 建立查詢，指定回傳型別為 ApplyMainHisJpaDAO（不是 Entity）

        Root<Ticket> m = cq.from(Ticket.class);
        // FROM ApplyMainHisJpa m（主表）


        cq.select(cb.construct(
                TicketQuery0206DAO.class,
                // 指定用建構式方式建立 DAO（等同 SELECT new xxx(...)）
                m.get("id"),
                m.get("userName"),
                m.get("title"),
                m.get("content"),
                m.get("category"),
                m.get("priority"),
                m.get("status"),
                m.get("contact"),
                m.get("createdAt"),
                m.get("updatedAt")

        ));
        List<Predicate> predicates = new ArrayList<>();


        if (req.getUserName() != null && !req.getUserName().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(m.get("userName")),
                    "%" + req.getUserName().trim().toLowerCase() + "%"
            ));
        }


        if (req.getTitle() != null && !req.getTitle().isEmpty()) {
            predicates.add(cb.equal(m.get("title"), req.getTitle()));
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
    public int deleteById(TicketDel0206DAO dao) {
        int rows = 0; // 影響筆數
        Ticket entity = em.find(Ticket.class, dao.getId());
        if (entity == null) {
            return rows; // 0：找不到資料
        }
        em.remove(entity);
        rows = 1; // 成功刪除一筆
        return rows;
    }



    //新增
    @Transactional
    public int insert(Ticket ticket) {
        em.persist(ticket);
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




