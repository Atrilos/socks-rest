package com.atrilos.socksrest.controller;

import com.atrilos.socksrest.model.ChangeSocksDTO;
import com.atrilos.socksrest.model.FilterSocks;
import com.atrilos.socksrest.service.SocksService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/socks")
public class SocksController {

    private final SocksService socksService;

    /**
     * Method for addition of socks
     *
     * @param changeSocksDTO dto-class that contains info about incoming socks
     * @return 200 - request successful, 400 - invalid parameters
     */
    @PostMapping("/income")
    public ResponseEntity<?> addSocks(@RequestBody @Valid ChangeSocksDTO changeSocksDTO) {
        socksService.changeSocks(changeSocksDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Method for removing socks from db
     *
     * @param changeSocksDTO dto-class that contains info about socks to remove
     * @return 200 - request successful, 400 - invalid parameters, negative amount of socks after remove operation
     */
    @PostMapping("/outcome")
    public ResponseEntity<?> removeSocks(@RequestBody @Valid ChangeSocksDTO changeSocksDTO) {
        changeSocksDTO.setQuantity(-changeSocksDTO.getQuantity());
        socksService.changeSocks(changeSocksDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Method for getting socks with filter
     *
     * @param filterSocks filter parameters
     * @return 200 - request successful, 400 - no parameters in filterSocks,
     * non-existent operation, impossible cottonPart
     */
    @GetMapping
    public ResponseEntity<Long> getSocks(@Valid FilterSocks filterSocks) {
        return ResponseEntity.ok(socksService.getSocksFiltered(filterSocks));
    }
}
