package com.kosta.catdog.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kosta.catdog.entity.QShop;
import com.kosta.catdog.entity.Shop;
import com.querydsl.jpa.impl.JPAQueryFactory;


@Repository
public class ShopDslRepository {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    EntityManager entityManager;

    // Shop
    public List<Shop> findById(String id){
        QShop shop = QShop.shop;
        return jpaQueryFactory.selectFrom(shop)
                .where(shop.id.eq(id)).fetch();
    }

    public Shop fidnByNum(Integer num) {
        QShop shop = QShop.shop;
        return jpaQueryFactory.selectFrom(shop)
                .where(shop.num.eq(num)).fetchOne();
    }
}
