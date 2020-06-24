package main.services;

import main.DTO.SettingsResponse;
import main.model.GlobalSettings;
import main.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SettingsService {
    private final SettingsRepository settingsRepository;

    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public boolean getStatIsPublic(){
        return settingsRepository.getStatIsPublic();
    }

    public ResponseEntity<SettingsResponse> getSettings(){
        List<GlobalSettings> globalSettings = settingsRepository.findAll();
        SettingsResponse settingsResponse = new SettingsResponse();
        globalSettings.forEach(e->{
            if(e.getCode().equals("MULTIUSER_MODE")){
                settingsResponse.setMultiUserMode(e.getValue());
            }
            if(e.getCode().equals("POST_PREMODERATION")){
                settingsResponse.setPostPremoderation(e.getValue());
            }
            if(e.getCode().equals("STATISTICS_IS_PUBLIC")){
                settingsResponse.setStatisticsIsPublic(e.getValue());
            }
        });
        return new ResponseEntity<>(settingsResponse, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity changeSettings(SettingsResponse settingsResponse){
        Boolean multiuserMode = settingsResponse.getMultiUserMode();
        Boolean postPremoderation = settingsResponse.getPostPremoderation();
        Boolean statisticsIsPublic = settingsResponse.getStatisticsIsPublic();
        if(multiuserMode != null){
            if(multiuserMode.equals(true)){
                settingsRepository.updateMultiuserMode(true);
            }else{
                settingsRepository.updateMultiuserMode(false);
            }
        }
        if(postPremoderation != null){
            if(postPremoderation.equals(true)){
                settingsRepository.updatePostPremoderation(true);
            }else{
                settingsRepository.updatePostPremoderation(false);
            }
        }
        if(statisticsIsPublic != null){
            if(statisticsIsPublic.equals(true)){
                settingsRepository.updateStatistics(true);
            }else{
                settingsRepository.updateStatistics(false);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
