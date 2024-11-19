package com.threeping.syncday.proj.command.application.controller;

import com.threeping.syncday.common.ResponseDTO;
import com.threeping.syncday.proj.command.aggregate.vo.ProjVO;
import com.threeping.syncday.proj.command.application.service.AppProjService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/proj")
public class AppProjController {
    private final AppProjService appProjService;

    @Autowired
    public AppProjController(AppProjService appProjService){
        this.appProjService = appProjService;
    }

    @PostMapping("/")
    public ResponseDTO<?> createProj(@RequestBody ProjVO newProj){
        log.info("newProj: {}", newProj);
        return ResponseDTO.ok(appProjService.addProj(newProj));
    }

//    @PutMapping("/")
//    public ResponseDTO<?> updateProj(@RequestBody ProjDTO projDTO){
//        log.info("projDTO: {}", projDTO);
//        return ResponseDTO.ok(appProjService.modifyProj(projDTO));
//    }

    @DeleteMapping("/{projId}")
    public ResponseDTO<?> deleteProj(@PathVariable("projId") Long projId){
        return ResponseDTO.ok(appProjService.deleteProj(projId));
    }
}
