package com.truextend.dao;

import java.util.ArrayList;
import java.util.List;

import com.truextend.exception.BusinessException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class PaginationHelper {

    public static List<Order> parseOrders(String ordersString) {
        List<Order> orderList = new ArrayList<>();
        if (ordersString != null) {
            String[] ordersPartsArray = ordersString.split(",");
            for (String orderString : ordersPartsArray) {
                String[] orderPartArray = orderString.split(":");
                if (orderPartArray.length >= 1) {
                    if (orderPartArray.length >= 2) {
                        if ("asc".equalsIgnoreCase(orderPartArray[1])) {
                            orderList.add(Order.asc(orderPartArray[0]));
                        } else {
                            orderList.add(Order.desc(orderPartArray[0]));
                        }
                    } else {
                        orderList.add(Order.asc(orderPartArray[0]));
                    }
                }
            }
        }
        return orderList;
    }

    public static List<Criterion> parseCriteria(String criteria) {
        List<Criterion> criterionList = new ArrayList<>();
        if (criteria != null) {
            String[] criterionArray = criteria.split(",");
            for (String criterionString : criterionArray) {
                String[] criterionParts = criterionString.split(":");
                if (criterionParts.length >= 3) {
                    if ("eq".equalsIgnoreCase(criterionParts[1])) {
                        criterionList.add(Restrictions.eq(criterionParts[0], criterionParts[2]));
                    }  else if ("like".equalsIgnoreCase(criterionParts[1])) {
                        criterionList.add(Restrictions.like(criterionParts[0], criterionParts[2]));
                    }  else if ("ne".equalsIgnoreCase(criterionParts[1])) {
                        criterionList.add(Restrictions.ne(criterionParts[0], criterionParts[2]));
                    }  else if ("ge".equalsIgnoreCase(criterionParts[1])) {
                        criterionList.add(Restrictions.ge(criterionParts[0], criterionParts[2]));
                    }  else if ("gt".equalsIgnoreCase(criterionParts[1])) {
                        criterionList.add(Restrictions.gt(criterionParts[0], criterionParts[2]));
                    }  else if ("le".equalsIgnoreCase(criterionParts[1])) {
                        criterionList.add(Restrictions.le(criterionParts[0], criterionParts[2]));
                    }  else if ("lt".equalsIgnoreCase(criterionParts[1])) {
                        criterionList.add(Restrictions.lt(criterionParts[0], criterionParts[2]));
                    }  else if ("between".equalsIgnoreCase(criterionParts[1])) {
                        criterionList.add(Restrictions.between(criterionParts[0], criterionParts[2], criterionParts[3]));
                    } else {
                        throw new BusinessException("Not supported operation "+criterionParts[1]);
                    }
                }
            }
        }
        return criterionList;
    }
}
