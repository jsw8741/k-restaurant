package com.krestaurant.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.krestaurant.entity.Reservation;

public interface BookRepository extends JpaRepository<Reservation, Long>{
	@Query("select r from Reservation r order by r.bookDate")
	List<Reservation> findReservations(Pageable pageable);
	
	@Query("select count(r) from Reservation r")
	Long countReservation();
	
	@Query("select r from Reservation r where r.member.id = :member_id order by r.bookDate")
	List<Reservation> getMemberBookList(@Param("member_id") Long memberId);
}
