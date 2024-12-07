package com.threeping.syncday.projmember.command.application.service;

import com.threeping.syncday.projmember.command.aggregate.entity.BookmarkStatus;
import com.threeping.syncday.projmember.command.aggregate.vo.UpdateProjRequest;
import com.threeping.syncday.projmember.command.aggregate.vo.UpdateProjResponse;

public interface AppProjMemberService {
    Boolean addProjOwner(Long projId, Long userId);

    BookmarkStatus updateProjBookmark(Long projMemberId);

    UpdateProjResponse addProj(UpdateProjRequest req);

    UpdateProjResponse updateProj(UpdateProjRequest req);
}
