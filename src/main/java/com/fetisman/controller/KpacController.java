package com.fetisman.controller;

import com.fetisman.dao.KpacDao;
import com.fetisman.model.Kpac;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/kpacs")
@Slf4j
public class KpacController {

    @Autowired
    private KpacDao kpacDao;

    @GetMapping
    public String getAllKpacs(Model model) {
        model.addAttribute("kpacs", kpacDao.getAllKpacs());
        return "kpacs";
    }

    @GetMapping("/json")
    @ResponseBody
    public List<Kpac> getKpacsJson() {
        return kpacDao.getAllKpacs();
    }


    @PostMapping
    @ResponseBody
    public ResponseEntity<?> addKpac(@Valid @ModelAttribute Kpac kpac, BindingResult result) {
        if (result.hasErrors()) {
            log.error("!!! log result.getAllErrors: " + result.getAllErrors());
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
//        log.info("!!! log kpac.getCreationDate(): " + kpac.getCreationDate().toString());
//        System.out.println("!!! sout kpac.getCreationDate(): " + kpac.getCreationDate().toString());
        kpacDao.addKpac(kpac);
        return ResponseEntity.ok(kpac);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteKpac(@PathVariable int id) {
        kpacDao.deleteKpac(id);
        return ResponseEntity.ok().build();
    }
}

