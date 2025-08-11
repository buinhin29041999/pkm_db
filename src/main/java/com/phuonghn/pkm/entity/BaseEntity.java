package com.phuonghn.pkm.entity;

import com.phuonghn.pkm.common.Constants;
import com.phuonghn.pkm.common.utils.ColumnDisplayInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {
    @CreatedDate
    @Column(name = "created_date")
    @ColumnDisplayInfo(displayName = Constants.CREATED_DATE)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @ColumnDisplayInfo(displayName = Constants.LAST_MODIFIED_DATE)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by")
    @ColumnDisplayInfo(displayName = Constants.CREATED_BY)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    @ColumnDisplayInfo(displayName = Constants.LAST_MODIFIED_BY)
    private String lastModifiedBy;
}
