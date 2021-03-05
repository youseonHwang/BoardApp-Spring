package com.youseon.jpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPQLQuery;
import com.youseon.jpa.entity.Board;
import com.youseon.jpa.entity.QAccount;
import com.youseon.jpa.entity.QReview;
import com.youseon.jpa.entity.Review;

@Repository
public class ReviewRepositoryImpl extends QuerydslRepositorySupport implements CustomReviewRepository {
	
	//QuerydslRepositorySupport 클래스에는 기본생성자가 없음.
    public ReviewRepositoryImpl() {
        super(Review.class);
    }
    
    @Override
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }
	
	@Override
	public List<Long> findReviewCounts() {
		QReview review = QReview.review;
		QAccount account = QAccount.account;
		
		JPQLQuery<Long> query = 
					from(review)
				.select(review.count())
	    		.join(review.account, account)
	    		.groupBy(review.rating);
		
		if(query.fetch()==null) {
			return null;
		}
		
	    return query.fetch();
	}

}
