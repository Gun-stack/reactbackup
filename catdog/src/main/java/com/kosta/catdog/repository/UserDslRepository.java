package com.kosta.catdog.repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kosta.catdog.entity.DesGallery;
import com.kosta.catdog.entity.DesGalleryLike;
import com.kosta.catdog.entity.Designer;
import com.kosta.catdog.entity.Pet;
import com.kosta.catdog.entity.QDesGallery;
import com.kosta.catdog.entity.QDesGalleryLike;
import com.kosta.catdog.entity.QDesigner;
import com.kosta.catdog.entity.QPet;
import com.kosta.catdog.entity.QReservation;
import com.kosta.catdog.entity.QReview;
import com.kosta.catdog.entity.QShop;
import com.kosta.catdog.entity.QUser;
import com.kosta.catdog.entity.QUserGallery;
import com.kosta.catdog.entity.QUserGalleryComment;
import com.kosta.catdog.entity.QUserGalleryLike;
import com.kosta.catdog.entity.Reservation;
import com.kosta.catdog.entity.Review;
import com.kosta.catdog.entity.Shop;
import com.kosta.catdog.entity.User;
import com.kosta.catdog.entity.UserGallery;
import com.kosta.catdog.entity.UserGalleryComment;
import com.kosta.catdog.entity.UserGalleryLike;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class UserDslRepository {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    EntityManager entityManager;

    // User
    public User findByName(String name) {
        QUser user = QUser.user;
        return jpaQueryFactory.selectFrom(user)
                .where(user.name.eq(name)).fetchOne();
    }

    public User findById(String id) {
        QUser user = QUser.user;
        return jpaQueryFactory.selectFrom(user)
                .where(user.id.eq(id)).fetchOne();
    }

    public User findByNickname(String nickname) {
        QUser user = QUser.user;
        return jpaQueryFactory.selectFrom(user)
                .where(user.nickname.eq(nickname)).fetchOne();
    }

    public User findByNum(Integer num) {
        QUser user = QUser.user;
        return jpaQueryFactory.selectFrom(user)
                .where(user.num.eq(num)).fetchOne();
    }
    
    public String findByEmail(String email) {
    	QUser user = QUser.user;
    	return jpaQueryFactory.select(user.email)
    			.from(user)
    			.where(user.email.eq(email))
    			.fetchOne();
    }

    public User findById_AndPassword(String id, String password) {
        QUser user = QUser.user;
        
        System.out.println(id);
        System.out.println(password);
        return jpaQueryFactory.selectFrom(user)
                .where(user.id.eq(id).and(user.password.eq(password))).fetchOne();
    }
    
    public User findById_AndEmail(String id, String email) {
        QUser user = QUser.user;
        return jpaQueryFactory.selectFrom(user)
                .where(user.id.eq(id).and(user.email.eq(email))).fetchOne();
    }

    public String findIdByEmail(String email) {
        QUser user = QUser.user;
        return jpaQueryFactory.select(user.id)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();
    }
    
    public String findId(String email) {
    	QUser user = QUser.user;
    	return jpaQueryFactory.select(user.id)
    			.from(user)
    			.where(user.email.eq(email))
    			.fetchOne();
    }

    @Transactional
    public void modifyNickname(Integer num, String nickname) {
        QUser user = QUser.user;
        jpaQueryFactory.update(user)
                .set(user.nickname, nickname)
                .where(user.num.eq(num))
                .execute();
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void modifyTel(Integer num, String tel) {
        QUser user = QUser.user;
        jpaQueryFactory.update(user)
                .set(user.tel, tel)
                .where(user.num.eq(num))
                .execute();
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void modifyPassword(Integer num, String password) {
        QUser user = QUser.user;
        jpaQueryFactory.update(user)
                .set(user.password, password)
                .where(user.num.eq(num))
                .execute();
        entityManager.flush();
        entityManager.clear();
    }

    public List<Designer> findDesListBySId(String sId) {
        QDesigner des = QDesigner.designer;
        return jpaQueryFactory.selectFrom(des)
                .where(des.sId.eq(sId))
                .fetch();


    }


    @Transactional
    public void updateReservationIsReview(Integer resNum) {
        QReservation reservation = QReservation.reservation;
        jpaQueryFactory.update(reservation).set(reservation.isReview, 1)
                .where(reservation.num.eq(resNum))
                .execute();
        entityManager.flush();
        entityManager.clear();


    }

    @Transactional
    public void modifyRole(String id) {
        QUser user = QUser.user;
        jpaQueryFactory.update(user)
                .set(user.roles, user.roles)
                .where(user.id.eq(id))
                .execute();
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void withdrawalUser(User u){
        QUser user = QUser.user;
        jpaQueryFactory.update(user)
                .set(user.state, "false")
                .where(user.id.eq(u.getId()))
                .execute();
        entityManager.flush();
        entityManager.clear();
    }

    public UserGallery findUserGallery(Integer num) {
        QUserGallery userGallery = QUserGallery.userGallery;
        return jpaQueryFactory.selectFrom(userGallery)
                .where(userGallery.num.eq(num))
                .fetchOne();
    }


    // DesGallery
    public DesGallery findDesGallery(Integer num) {
        QDesGallery desGallery = QDesGallery.desGallery;
        return jpaQueryFactory.selectFrom(desGallery)
                .where(desGallery.num.eq(num))
                .fetchOne();
    }

    public List<DesGallery> findDesGalleryListShopPage(Integer num, int offset, int limit) {
        QDesGallery desGallery = QDesGallery.desGallery;
        QDesigner designer = QDesigner.designer;
        QShop shop = QShop.shop;

        return jpaQueryFactory.selectFrom(desGallery)
                .from(desGallery)
                .join(designer)
                .on(desGallery.desId.eq(designer.id))
                .join(shop)
                .on(designer.sId.eq(shop.sId))
                .where(shop.num.eq(num))
                .orderBy(desGallery.date.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    public List<DesGallery> findDesGalleryListDesignerPage(Integer num, int offset, int limit) {
        QDesGallery desGallery = QDesGallery.desGallery;
        QDesigner designer = QDesigner.designer;
        return jpaQueryFactory.selectFrom(desGallery)
                .from(desGallery)
                .join(designer)
                .on(desGallery.desId.eq(designer.id))
                .where(designer.num.eq(num))
                .orderBy(desGallery.date.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
    
  
    // Review
    public Review findReview(Integer num) {
        QReview review = QReview.review;
        return jpaQueryFactory.selectFrom(review)
                .where(review.num.eq(num))
                .fetchOne();
    }

    public List<Review> findReviewListByShopOrderByDateDesc(Integer num, int offset, int limit) {
        QReview review = QReview.review;
        QDesigner designer = QDesigner.designer;
        QShop shop = QShop.shop;
        return jpaQueryFactory.selectFrom(review)
                .from(review)
                .join(designer)
                .on(review.desId.eq(designer.id))
                .join(shop)
                .on(designer.sId.eq(shop.sId))
                .where(shop.num.eq(num))
                .orderBy(review.date.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    public List<Review> findReviewListByDesignerOrderByDateDesc(Integer num, int offset, int limit) {
        QReview review = QReview.review;
        QDesigner designer = QDesigner.designer;
        return jpaQueryFactory.selectFrom(review)
                .from(review)
                .join(designer)
                .on(review.desId.eq(designer.id))
                .where(designer.num.eq(num))
                .orderBy(review.date.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    // Reservation
    public List<Reservation> findReservationListByDesigner_AndDate(Integer num, Date date) {
        QReservation reservation = QReservation.reservation;
        QDesigner designer = QDesigner.designer;
        return jpaQueryFactory.selectFrom(reservation)
                .join(designer)
                .on(reservation.desId.eq(designer.id))
                .where(designer.num.eq(num).and(reservation.date.eq(date)))
                .fetch();

    }

    public List<Reservation> findReservationListByUserId(String userId) {
        QReservation reservation = QReservation.reservation;
        return jpaQueryFactory.selectFrom(reservation)
                .where(reservation.userId.eq(userId))
                .fetch();
    }

    //pet
    public List<Pet> findPetsByUserID(String userId) {
        QUser user = QUser.user;
        QPet pet = QPet.pet;
        return jpaQueryFactory.selectFrom(pet)
                .join(user)
                .on(pet.userNum.eq(user.num))
                .where(user.id.eq(userId))
                .fetch();
    }


    // Designer
    public Double findAvgStarCountByDesigner(Integer num) {
        QDesigner designer = QDesigner.designer;
        QReview review = QReview.review;

        List<Integer> starList = jpaQueryFactory.select(review.star)
                .from(review)
                .join(designer)
                .on(review.desId.eq(designer.id))
                .where(designer.num.eq(num))
                .fetch();

        Double avgStarCount = starList.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0); // 由ъ뒪�듃媛� 鍮꾩뼱 �엳�뒗 寃쎌슦 湲곕낯媛� 0.0 諛섑솚

        return avgStarCount
                ;
    }
    
    
    
    
    
    //由щ럭�벑濡앹떆 �꺏�쓽 �룊洹좊퀎�젏
    @Transactional
    public void updateStarBySId(String sId) {
        QDesigner designer = QDesigner.designer;
        QShop shop = QShop.shop;
        QReview review = QReview.review;

        Long reviewCnt = jpaQueryFactory
                .select(review)
                .from(review)
                .join(designer).on(review.desId.eq(designer.id))
                .join(shop).on(designer.sId.eq(shop.sId))
                .where(shop.sId.eq(sId))
                .fetchCount();

        System.out.println("由щ럭移댁슫�듃 " + reviewCnt);

        if (reviewCnt != 0) {
            List<Integer> starList = jpaQueryFactory
                    .select(review.star)
                    .from(review)
                    .join(designer).on(review.desId.eq(designer.id))
                    .join(shop).on(designer.sId.eq(shop.sId))
                    .where(shop.sId.eq(sId))
                    .fetch();

            System.out.println(starList);

            if (!starList.isEmpty()) {
                int sum = starList.stream().mapToInt(Integer::intValue).sum();
                BigDecimal shopStar = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(reviewCnt), 2, RoundingMode.HALF_UP);
                jpaQueryFactory.update(shop).set(shop.star, shopStar).where(shop.sId.eq(sId)).execute();
            } else {
                System.out.println("蹂� 紐⑸줉�씠 鍮꾩뼱 �엳�쓬");
                // 蹂꾩젏�쓣 怨꾩궛�븷 蹂꾩씠 �뾾�뒗 寃쎌슦 泥섎━
            }
        } else {
            System.out.println("由щ럭 移댁슫�듃媛� 0�엯�땲�떎");
            // 由щ럭媛� �뾾�뒗 寃쎌슦 泥섎━
        }
    }
    
    
    //由щ럭 �벑濡앹떆 �룊洹좊퀎�젏
    @Transactional
    public void UpdateStarByDesNumAndReviewStar(Integer desNum, Review review) {
        QDesigner designer = QDesigner.designer;

        BigDecimal desStar = jpaQueryFactory.select(designer.star)
                .from(designer)
                .where(designer.num.eq(desNum))
                .fetchOne();
        
        Integer revCnt = jpaQueryFactory.select(designer.reviewCnt)
                .from(designer)
                .where(designer.num.eq(desNum))
                .fetchOne();

        desStar = (desStar.multiply(BigDecimal.valueOf(revCnt)).add(BigDecimal.valueOf(review.getStar())))
                .divide(BigDecimal.valueOf(revCnt + 1), 2, RoundingMode.HALF_UP);

        // �씠�젣 蹂�寃쎈맂 蹂꾩젏 諛� 由щ럭 移댁슫�듃瑜� �뜲�씠�꽣踰좎씠�뒪�뿉 ���옣�빐�빞 �빀�땲�떎.
        // (�뵒�옄�씠�꼫�뿉 ���븳 �냽�꽦�씤 star 諛� reviewCnt媛� �엳�뒗 媛��젙�븯�뿉 �옉�꽦�맂 肄붾뱶�엯�땲�떎)
        jpaQueryFactory.update(designer)
                .set(designer.star, desStar)
                .set(designer.reviewCnt, revCnt + 1)
                .where(designer.num.eq(desNum))
                .execute();
    }

    //由щ럭 �닔�젙�떆 �룊洹좊퀎�젏 諛섏쁺
    @Transactional
    public void UpdateStarByDesNumAndReviewStarModi(Integer desNum, Review review) {
        QDesigner designer = QDesigner.designer;
        QReview review1 = QReview.review;

        Designer des = jpaQueryFactory.selectFrom(designer).where(designer.num.eq(desNum)).fetchOne();

        List<Review> allReviews = jpaQueryFactory.selectFrom(review1)
                .where(review1.desId.eq(des.getId())).fetch();


        // 由щ럭�쓽 紐⑤뱺 蹂꾩젏�쓣 �뜑�븳 媛�
        BigDecimal totalStars = allReviews.stream()
                .map(reviewInList -> BigDecimal.valueOf(reviewInList.getStar()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // �깉濡쒖슫 由щ럭�쓽 蹂꾩젏�쓣 異붽�
        totalStars = totalStars.add(BigDecimal.valueOf(review.getStar()));

        // 由щ럭�쓽 媛쒖닔
        int reviewCount = allReviews.size() + 1;

        // �룊洹� 蹂꾩젏 怨꾩궛
        BigDecimal averageStars = totalStars.divide(BigDecimal.valueOf(reviewCount), 2, RoundingMode.HALF_UP);

        // �뵒�옄�씠�꼫 �뾽�뜲�씠�듃
        jpaQueryFactory.update(designer)
                .set(designer.star, averageStars)
                .set(designer.reviewCnt, reviewCount)
                .where(designer.num.eq(desNum))
                .execute();

    }


    //designer by Id

    public Designer FindDesignerById(String desId) {
        QDesigner designer = QDesigner.designer;
        return jpaQueryFactory.selectFrom(designer)
                .where(designer.id.eq(desId))
                .fetchOne();
    }

    public Tuple findDesById(String desId) {
		QUser user = QUser.user;
        QDesigner designer = QDesigner.designer;
        return (Tuple) jpaQueryFactory.select(user.roles, user.name)
                .from(user)
                .join(designer)
                .on(user.id.eq(designer.id))
                .where(user.id.eq(desId))
                .fetchOne();
    }


    //shop
    public Shop FindShopBySid(String sid) {
        QShop shop = QShop.shop;
        return jpaQueryFactory.selectFrom(shop)
                .where(shop.sId.eq(sid))
                .fetchOne();

    }


    //review by resnum
    public Review FindReviewByResNum(Integer resNum) {
        QReview review = QReview.review;
        return jpaQueryFactory.selectFrom(review)
                .where(review.resNum.eq(resNum))
                .fetchOne();
    }


    //pet
    public Pet FindPetByuserIdAndPetName(Integer userNum, String petName) {
        QUser user = QUser.user;
        QPet pet = QPet.pet;
        return jpaQueryFactory.selectFrom(pet)
                .where(pet.userNum.eq(userNum).and(pet.name.eq(petName)))
                .fetchOne();

    }
    
  


	
	// UserGalleryComment
	public List<UserGalleryComment> findComment(Integer num) {
		QUserGallery userGallery = QUserGallery.userGallery;
		QUserGalleryComment userGalleryComment = QUserGalleryComment.userGalleryComment;
		
		return jpaQueryFactory.selectFrom(userGalleryComment)
				.where(userGalleryComment.galleryNum.eq(num))
				.fetch();
	}
	
	//desgallery like
	public DesGalleryLike FindDesGalLike(Integer desGalNum , Integer userNum) {
		QDesGalleryLike desGalleryLike = QDesGalleryLike.desGalleryLike;
		
		DesGalleryLike isLike = jpaQueryFactory.selectFrom(desGalleryLike)
				.where(desGalleryLike.desGalNum.eq(desGalNum).and(desGalleryLike.userNum.eq(userNum)))
				.fetchOne();
		if(isLike==null) {
			return null;
		}
		else {
			return isLike;
		}		
	}
	
	//
	public Boolean FindIsDesGalLike(Integer desGalNum,Integer userNum){
		QDesGalleryLike desGalleryLike = QDesGalleryLike.desGalleryLike;
		if(jpaQueryFactory.selectFrom(desGalleryLike)
				.where(desGalleryLike.desGalNum.eq(desGalNum).and(desGalleryLike.userNum.eq(userNum))).fetch().isEmpty()) {
			return false;
		}
		else {
		List<DesGalleryLike> isLike = jpaQueryFactory.selectFrom(desGalleryLike)
				.where(desGalleryLike.desGalNum.eq(desGalNum).and(desGalleryLike.userNum.eq(userNum))).fetch();
			return true;
		}
	}
	
	public Boolean FindIsUserGalLike(Integer userGalNum,Integer userNum){
		QUserGalleryLike userGalleryLike = QUserGalleryLike.userGalleryLike;
		if(jpaQueryFactory.selectFrom(userGalleryLike)
				.where(userGalleryLike.userGalNum.eq(userGalNum).and(userGalleryLike.userNum.eq(userNum))).fetch().isEmpty()) {
			return false;
		}
		else {
		List<UserGalleryLike> isLike = jpaQueryFactory.selectFrom(userGalleryLike)
				.where(userGalleryLike.userGalNum.eq(userGalNum).and(userGalleryLike.userNum.eq(userNum))).fetch();
			return true;
		}
	}
	
	//usergallery like
		public UserGalleryLike FindUserGalLike(Integer userGalNum , Integer userNum) {
			QUserGalleryLike userGalleryLike = QUserGalleryLike.userGalleryLike;
			
			UserGalleryLike isLike = jpaQueryFactory.selectFrom(userGalleryLike)
					.where(userGalleryLike.userGalNum.eq(userGalNum).and(userGalleryLike.userNum.eq(userNum)))
					.fetchOne();
			if(isLike==null) {
				return null;
			}
			else {
				return isLike;
			}		
		}
	

	
	
	
}
