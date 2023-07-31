package com.krestaurant.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

import com.krestaurant.constant.MenuSellStatus;
import com.krestaurant.dto.MainMenuDto;
import com.krestaurant.dto.MenuSearchDto;
import com.krestaurant.dto.QMainMenuDto;
import com.krestaurant.entity.Menu;
import com.krestaurant.entity.QMenu;
import com.krestaurant.entity.QMenuImg;

public class MenuRepositoryCustomImpl implements MenuRepositoryCustom{
	private JPAQueryFactory queryFactory;
	
	public MenuRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	private BooleanExpression regDtsAfter(String searcgDateType) {
		LocalDateTime dateTime = LocalDateTime.now(); //현재날짜 , 시간
		
		if (StringUtils.equals("all",searcgDateType) || searcgDateType == null) return null;
		else if(StringUtils.equals("1d",searcgDateType))
			dateTime = dateTime.minusDays(1); // 현재날짜부터 1일전
		else if(StringUtils.equals("1w",searcgDateType))
			dateTime = dateTime.minusWeeks(1); // 현재날짜부터 1주일전
		else if(StringUtils.equals("1m",searcgDateType))
			dateTime = dateTime.minusMonths(1); // 현재날짜부터 1달전
		else if(StringUtils.equals("6m",searcgDateType))
			dateTime = dateTime.minusMonths(6); // 현재날짜부터 6개월전
		
		return QMenu.menu.regTime.after(dateTime);
	}	
	
	private BooleanExpression searchSellStatusEq(MenuSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : QMenu.menu.menuSellStatus.eq(searchSellStatus);
	}
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if (StringUtils.equals("menuNm", searchBy)) {
			return QMenu.menu.menuNm.like("%" + searchQuery + "%");
		}else if(StringUtils.equals("category", searchBy)) {
			return QMenu.menu.category.category.like("%" + searchQuery + "%");
		}
		else if(StringUtils.equals("createdBy",searchBy)) {
			return QMenu.menu.createdBy.like("%" + searchQuery + "%");
		}
		return null;
		
	}
	
	@Override
	public Page<Menu> getAdminMenuPage(MenuSearchDto menuSearchDto, Pageable pageable) {
		List<Menu> content = queryFactory.selectFrom(QMenu.menu)
				 .where(regDtsAfter(menuSearchDto.getSearchDateType()),
				 searchSellStatusEq(menuSearchDto.getSearchSellStatus()),
				 searchByLike(menuSearchDto.getSearchBy(), menuSearchDto.getSearchQuery()))										 
				 .orderBy(QMenu.menu.id.desc())
				 .offset(pageable.getOffset())
				 .limit(pageable.getPageSize())
				 .fetch();

		long total = queryFactory.select(Wildcard.count).from(QMenu.menu)
		.where(regDtsAfter(menuSearchDto.getSearchDateType()),
		searchSellStatusEq(menuSearchDto.getSearchSellStatus()),
		searchByLike(menuSearchDto.getSearchBy(), menuSearchDto.getSearchQuery()))
		.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public List<MainMenuDto> getMainMenuList() {
		QMenu menu = QMenu.menu;
		QMenuImg menuImg = QMenuImg.menuImg;
		
		List<MainMenuDto> content = queryFactory
									.select(new QMainMenuDto
										   (menu.id,
										    menu.menuNm,
										    menu.menuDetail, 
										    menuImg.imgUrl,
										    menu.price, 
										    menu.category)
										   )
									.from(menuImg)
									.join(menuImg.menu, menu)
									.orderBy(menu.menuNm.asc())
									.fetch();
		
		return content;
	}

}
