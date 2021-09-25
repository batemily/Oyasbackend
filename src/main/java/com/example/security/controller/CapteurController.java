package com.example.security.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.model.Capteur;
import com.example.security.repository.CapteurRepository;
import com.example.security.service.impl.CapteurService;

import exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/capteurs")
@CrossOrigin("*")
public class CapteurController {
	
	
	@Autowired
	private CapteurService capteurService;
	

    @GetMapping("/getAllCapteurs")
    public List<Capteur> findAllCapteurs() {
        return capteurService.getAll();
    }
    
    @GetMapping("/findCapteur/{id}")
    public Capteur findCapteur(@PathVariable Long id) {
        return capteurService.getById(id);
    }
    
    @DeleteMapping("/cancel/{id}")
    public void cancelCapteur(@PathVariable int id) {
    }
    
    @PostMapping("/capteurs")
	public Capteur createCapteur(@Validated @RequestBody Capteur capteur) {
		return capteurService.add(capteur);
	}
    
//    @DeleteMapping("/capteurs/delete")
//	  public ResponseEntity<String> deleteAllCapteurs() {
//	    System.out.println("Delete All Sensors...");
//	    capteurService.deleteAll();
//	    return new ResponseEntity<>("All Sensors have been deleted!", HttpStatus.OK);
//	  }
//    
//	@PutMapping("/capteurs/{id}")
//	public ResponseEntity<Capteur> updateCapteur(@PathVariable(value = "id") Long id,
//			@Validated @RequestBody Capteur capteur) throws ResourceNotFoundException {
//		Capteur capt = capteurService.findById(id)
//		.orElseThrow(() -> new ResourceNotFoundException("Sensor not found for this id :: " + id));
//		capt.setName(capteur.getName());
//		
//		final Capteur updateCapteur = capteurService.save(capt);
//		return ResponseEntity.ok(updateCapteur);
//		}
//	
//	@DeleteMapping("/capteurs/{id}")
//	public Map<String, Boolean> deleteCapteur(@PathVariable(value = "id") Long id)
//			throws ResourceNotFoundException {
//		Capteur capt = capteurService.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Sensor not found for this id :: " + id));
//		capteurService.delete(capt);
//		Map<String, Boolean> response = new HashMap<>();
//		response.put("deleted", Boolean.TRUE);
//		return response;
//	}
//	

}
