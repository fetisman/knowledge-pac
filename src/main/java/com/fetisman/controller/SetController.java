package com.fetisman.controller;

import com.fetisman.dao.KpacDao;
import com.fetisman.dao.KpacSetDao;
import com.fetisman.model.KpacSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/sets")
@Slf4j
public class SetController {

    @Autowired
    private KpacSetDao kpacSetDao;

    @Autowired
    private KpacDao kpacDao;

    @GetMapping
    public String getAllSets(Model model) {
        model.addAttribute("sets", kpacSetDao.getAllSets());
        model.addAttribute("kpacs", kpacDao.getAllKpacs());
        return "sets";
    }

    @GetMapping( path= "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<KpacSet> getAllSets() {
        return kpacSetDao.getAllSets();
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> addSet(@RequestParam String title, @RequestParam List<Long> kpacIds) {
        kpacSetDao.createSet(title, kpacIds);
        return ResponseEntity.ok(title);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteSet(@PathVariable Long id) {
        kpacSetDao.deleteSet(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public String getSetDetails(@PathVariable Long id, Model model) {
        KpacSet set = kpacSetDao.getSetById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "K-PAC Set not found"));
        model.addAttribute("set", set);
        return "set-details";
    }
}

