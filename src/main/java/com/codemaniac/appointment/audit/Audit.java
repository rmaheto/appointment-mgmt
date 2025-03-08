package com.codemaniac.appointment.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
public class Audit implements Serializable {
  @Serial
  private static final long serialVersionUID = 9048986408898460455L;
  public static final String PROGRAM = "WEB-APP";
  public static final String SYSTEM = "SYSTEM";
  public static final String RECORD_STATUS_DELETED = "D";
  public static final String RECORD_STATUS_ACTIVE = "A";

  @Column(name = "CREATED_BY")
  private String createdBy = "";

  @Column(name = "CREATE_MODULE")
  private String createModule = "";

  @Column(name = "CREATE_TIMESTAMP")
  private LocalDateTime createTimestamp;

  @Column(name = "RECORD_STATUS")
  private String recordStatus = RECORD_STATUS_ACTIVE;

  @Column(name = "UPDATED_BY")
  private String updatedBy = "";

  @Column(name = "UPDATE_MODULE")
  private String updateModule = "";

  @Column(name = "UPDATE_TIMESTAMP")
  private LocalDateTime updateTimestamp;

  public Audit() {
    // Default.
  }

  public Audit(String newCreatedBy, String newCreateModule) {
    createdBy = newCreatedBy;
    createModule = newCreateModule;
    createTimestamp = LocalDateTime.now();
    recordStatus = RECORD_STATUS_ACTIVE;
  }

  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("createdBy", this.createdBy);
    builder.append("createModule", this.createModule);
    builder.append("createTimestamp", (this.createTimestamp != null) ? this.createTimestamp : null);

    builder.append("updatedBy", this.updatedBy);
    builder.append("updateModule", this.updateModule);
    builder.append("updateTimestamp", (this.updateTimestamp != null) ? this.updateTimestamp : null);

    builder.append("recordStatus", this.recordStatus);

    return builder.toString();
  }

  public String getLastModifiedBy() {
    if ((updatedBy != null) && (!updatedBy.isEmpty())) {
      return updatedBy;
    }

    return createdBy;
  }

  public LocalDateTime getLastModifiedByTimestamp() {
    if (updateTimestamp != null) {
      return updateTimestamp;
    }

    return createTimestamp;
  }

  public boolean isRecordActive() {
    return RECORD_STATUS_ACTIVE.equals(recordStatus);
  }

  public void setCreates(String newCreatedBy, String newCreateModule) {
    createdBy = newCreatedBy;
    createModule = newCreateModule;
    createTimestamp = LocalDateTime.now();
  }

  public void setUpdates(String newUpdatedBy, String newUpdateModule) {
    this.updatedBy = newUpdatedBy;
    this.updateModule = newUpdateModule;
    this.updateTimestamp = LocalDateTime.now();
  }
}
