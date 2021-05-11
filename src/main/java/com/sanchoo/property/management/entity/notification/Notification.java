package com.sanchoo.property.management.entity.notification;

import com.sanchoo.property.management.entity.user.User;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private int id;

	@OneToOne
	@JoinColumn(name = "user_from_id")
	private User userFrom;

	@Column(name = "message")
	private String message;

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "date_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime dateTime;
}
