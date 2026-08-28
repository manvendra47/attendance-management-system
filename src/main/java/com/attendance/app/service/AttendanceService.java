package com.attendance.app.service;

import com.attendance.app.model.Attendance;
import com.attendance.app.model.User;
import com.attendance.app.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<Attendance> findAll() {
        return attendanceRepository.findAllByOrderByDateDesc();
    }

    public List<Attendance> findByUser(User user) {
        return attendanceRepository.findByUserOrderByDateDesc(user);
    }

    public List<Attendance> findByUserAndRange(User user, LocalDate start, LocalDate end) {
        return attendanceRepository.findByUserAndDateBetweenOrderByDateDesc(user, start, end);
    }

    public Attendance findById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attendance record not found with id: " + id));
    }

    @Transactional
    public Attendance markAttendance(Attendance attendance) {
        attendanceRepository.findByUserAndDate(attendance.getUser(), attendance.getDate())
                .ifPresent(existing -> attendance.setId(existing.getId()));
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance updateAttendance(Long id, Attendance updated) {
        Attendance existing = findById(id);
        existing.setDate(updated.getDate());
        existing.setStatus(updated.getStatus());
        existing.setRemarks(updated.getRemarks());
        return attendanceRepository.save(existing);
    }

    @Transactional
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    public long countByStatus(List<Attendance> records, com.attendance.app.model.AttendanceStatus status) {
        return records.stream().filter(a -> a.getStatus() == status).count();
    }
}
