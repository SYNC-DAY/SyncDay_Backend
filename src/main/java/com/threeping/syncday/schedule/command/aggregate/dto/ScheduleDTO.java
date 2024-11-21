package com.threeping.syncday.schedule.command.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.threeping.syncday.schedule.command.aggregate.entity.PublicStatus;
import com.threeping.syncday.schedule.command.aggregate.entity.MeetingStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ScheduleDTO {

    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("schedule_id")
    Long scheduleId;

    @JsonProperty("title")
    String title;

    @JsonProperty("content")
    String content;

    @JsonProperty("start_time")
    Timestamp startTime;

    @JsonProperty("end_time")
    Timestamp endTime;

    @JsonProperty("update_time")
    Timestamp updateTime;

    @JsonProperty("public_status")
    PublicStatus publicStatus;

    @JsonProperty("schedule_repeat_id")
    Long scheduleRepeatId;

    @JsonProperty("repeat_order")
    Long repeatOrder;

    @JsonProperty("meeting_status")
    MeetingStatus meetingStatus;

    @JsonProperty("meetingroom_id")
    Long meetingroomId;

    @JsonProperty("attendee_ids")
    List<Long> attendeeIds;
}
