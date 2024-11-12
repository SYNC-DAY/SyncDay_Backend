package com.threeping.syncday.projmember.command.aggregate.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="TBL_PROJ_MEMBER")
@IdClass(ProjMemberPK.class)
public class ProjMember {
    @Id
    @Column(name="proj_id")
    private Long projId;

    @Id
    @Column(name="user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name="bookmark_status")
    private BookmarkStatus bookmarkStatus;

    @Enumerated(EnumType.STRING)
    @Column(name="participation_status")
    private ParticipationStatus participationStatus;


    @PrePersist
    public void prePersist() {
        this.bookmarkStatus = BookmarkStatus.NONE;
    }
}