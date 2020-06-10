package main.services;

import lombok.Getter;
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

    @Getter
    private int statIsPublic;

    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
        statIsPublic = settingsRepository.getStatIsPublic();
    }

    public ResponseEntity<List<GlobalSettings>> getSettings(){
        return new ResponseEntity<>(settingsRepository.findAll(), HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity changeSettings(Integer multiuserMode, Integer postPremoderation, Integer statisticsIsPublic){
        if(multiuserMode != null){
            if(multiuserMode == 1){
                settingsRepository.updateMultiuserMode(1);
            }else{
                settingsRepository.updateMultiuserMode(0);
            }
        }
        if(postPremoderation != null){
            if(postPremoderation == 1){
                settingsRepository.updatePostPremoderation(1);
            }else{
                settingsRepository.updatePostPremoderation(0);
            }
        }
        if(statisticsIsPublic != null){
            if(statisticsIsPublic == 1){
                settingsRepository.updateStatistics(1);
            }else{
                settingsRepository.updateStatistics(0);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
