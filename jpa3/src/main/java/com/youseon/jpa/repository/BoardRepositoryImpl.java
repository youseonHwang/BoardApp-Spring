package com.youseon.jpa.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.query.criteria.internal.expression.CoalesceExpression;
import org.hibernate.query.criteria.internal.expression.NullifExpression;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction.COUNT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.youseon.jpa.dto.CountBoardByAge;
import com.youseon.jpa.entity.Board;
import com.youseon.jpa.entity.QAccount;
import com.youseon.jpa.entity.QBoard;

import javassist.compiler.ast.NewExpr;

@Repository
public class BoardRepositoryImpl extends QuerydslRepositorySupport implements CustomBoardRepository {
	
	//QuerydslRepositorySupport 클래스에는 기본생성자가 없음.
    public BoardRepositoryImpl() {
        super(Board.class);
    }
    
    @Override
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }
    
    // 연령대별 글 등록 수
	@Override
	public List<Long> findCounts() {
		QBoard board = QBoard.board;
		QAccount account = QAccount.account;
		
		Expression<String> cases = 
				 new CaseBuilder()
	            .when(account.age.between(0, 9)).then("0~9세")
	            .when(account.age.between(10, 29)).then("10대")
	            .when(account.age.between(20, 39)).then("20대")
	            .when(account.age.between(30, 49)).then("30대")
	            .when(account.age.between(40, 59)).then("40대")
	            .when(account.age.between(50, 69)).then("50대")
	            .when(account.age.between(60, 79)).then("60대")
	            .otherwise("외");
		
		System.out.println("cases::::::"+cases);
		JPQLQuery<Long> query = 
					from(board)
				.select(board.count())
	    		.join(board.account, account)
	    		.groupBy(cases);
		
		if(query.fetch()==null) {
			return null;
		}
	    return query.fetch();
	}
	
	// 연령대별 회원 수
	@Override
	public List<Long> findAccountCounts() {
		QAccount account = QAccount.account;
		Expression<String> cases = 
				 new CaseBuilder()
	            .when(account.age.between(0, 9)).then("0~9세")
	            .when(account.age.between(10, 29)).then("10대")
	            .when(account.age.between(20, 39)).then("20대")
	            .when(account.age.between(30, 49)).then("30대")
	            .when(account.age.between(40, 59)).then("40대")
	            .when(account.age.between(50, 69)).then("50대")
	            .when(account.age.between(60, 79)).then("60대")
	            .otherwise("외");
		
		System.out.println("cases::::::"+cases);
		
		JPQLQuery<Long> query = 
					from(account)
				.select(account.id.count())
	    		.groupBy(cases);
		
		if(query.fetch()==null) {
			return null;
		}
		
	    return query.fetch();
	}
	
	// 최근 글 등록 수 
	@Override
	public List<Tuple> findDateBoardCounts() {
		QBoard board = QBoard.board;
		
		LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(50), LocalTime.of(0,0,0)); //50일 전 00:00:00
		LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59)); //오늘 23:59:59
		
		StringTemplate datePath = Expressions.stringTemplate(
			    "to_char({0},'{1s}')", board.regdate, ConstantImpl.create("YYYY-MM-DD"));
		
		System.out.println("datePath:::::::" + datePath);
		
		JPQLQuery<Tuple> query = 
					from(board)
				.select(board.count(), datePath)
				.where(board.regdate.between(startDatetime, endDatetime))
				.groupBy(datePath)
				.orderBy(datePath.asc());
		
		if(query.fetch()==null) {
			return null;
		}
	    return query.fetch();
	}

}
