package org.csiro.igsn.entity.postgres;

// Generated 09/01/2017 4:40:37 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * LogDate generated by hbm2java
 */
@Entity
@Table(name = "log_date")
public class LogDate implements java.io.Serializable {

	private int logDateId;
	private String eventType;
	private String logDate;

	public LogDate() {
	}


	public LogDate(String eventType, String logDate) {	
		this.eventType = eventType;
		this.logDate = logDate;
		
	}

	@Id
	@Column(name = "log_date_id", unique = true, nullable = false)
	@SequenceGenerator(name="log_date_id_seq",schema="version30",sequenceName="log_date_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="log_date_id_seq")
	public int getLogDateId() {
		return this.logDateId;
	}

	public void setLogDateId(int logDateId) {
		this.logDateId = logDateId;
	}

	@Column(name = "event_type")
	public String getEventType() {
		return this.eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}


	@Column(name = "log_date", length = 29)
	public String getLogDate() {
		return this.logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	

}
