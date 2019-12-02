package com.by.blcu.resource.service;

import com.by.blcu.resource.dto.LiveTelecast;
import com.by.blcu.resource.model.LiveDetailInfo;
import com.by.blcu.resource.model.LiveModel;
import com.by.blcu.resource.model.LiveRetModel;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface ILiveService {

    LiveRetModel createRoom(LiveModel liveModel, HttpServletRequest request) throws Exception;

    String getPlaybackUrl(String roomId,HttpServletRequest request) throws Exception;

    LiveRetModel updateRoom(LiveModel liveModel,HttpServletRequest request) throws Exception;

    LiveDetailInfo searchRoom(String roomId, HttpServletRequest request) throws Exception;

    String getRoomImg(String roomId, HttpServletRequest request) throws Exception;

    String closeRoom(String roomId) throws Exception;
}
