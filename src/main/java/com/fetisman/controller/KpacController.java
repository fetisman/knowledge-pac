package com.fetisman.controller;

import com.fetisman.dao.KpacDao;
import com.fetisman.model.Kpac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;

@Controller
@RequestMapping("/kpacs")
public class KpacController {

    @Autowired
    private KpacDao kpacDao;

    @GetMapping
    public String getAllKpacs(Model model) {
        model.addAttribute("kpacs", kpacDao.getAllKpacs());
        return "kpac-list";
    }

    @PostMapping("/add")
    public String addKpac(@RequestParam String title,
                          @RequestParam String description,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate creationDate) {
        Kpac kpac = new Kpac();
        kpac.setTitle(title);
        kpac.setDescription(description);
        kpac.setCreationDate(creationDate);
        kpacDao.addKpac(kpac);
        return "redirect:/kpacs";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteKpac(@PathVariable int id) {
        kpacDao.deleteKpac(id);
        return ResponseEntity.ok().build();
    }
}

