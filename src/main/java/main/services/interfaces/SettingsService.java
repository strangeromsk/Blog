package main.services.interfaces;

import main.DTO.SettingsResponse;
import org.springframework.http.ResponseEntity;

public interface SettingsService {
    ResponseEntity<SettingsResponse> getSettings();
    ResponseEntity changeSettings(SettingsResponse settingsResponse);
}
