package com.attendance.app.repository;

import com.attendance.app.model.Attendance;
import com.attendance.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByUserOrderByDateDesc(User user);

    List<Attendance> findAllByOrderByDateDesc();

    List<Attendance> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate start, LocalDate end);

    Optional<Attendance> findByUserAndDate(User user, LocalDate date);
}
