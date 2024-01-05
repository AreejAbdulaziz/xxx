package com.example.makhzan.Controller;

import com.example.makhzan.Model.Storage;
import com.example.makhzan.Model.User;
import com.example.makhzan.Service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/makhzan/storage")
public class StorageController {
    private final StorageService storageService;

    @GetMapping("/get")
    public ResponseEntity getStorages(){
        return ResponseEntity.status(200).body(storageService.getStorages());
    }

    @PostMapping("/add")
    public ResponseEntity addStorage(@RequestBody@Valid Storage storage, @AuthenticationPrincipal User user){
        storageService.addStorage(storage, user.getId());
        return ResponseEntity.status(200).body("Storage added");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateStorage(@RequestBody @Valid Storage storage, @PathVariable Integer id, @AuthenticationPrincipal User user){
        storageService.updateStorage(id, user.getId(), storage);
        return ResponseEntity.status(200).body("Storage updated");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteStorage(@PathVariable Integer id,@AuthenticationPrincipal User user){
        storageService.deleteStorage(id, user.getId());
        return ResponseEntity.status(200).body("Storage deleted");
    }

    //
    @GetMapping("/getB")
    public ResponseEntity rentTimes(){
        return ResponseEntity.status(200).body(storageService.findsStoragesByRentTimes());
    }

}
