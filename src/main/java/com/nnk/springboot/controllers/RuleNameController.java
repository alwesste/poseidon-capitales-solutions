package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.impl.RuleNameService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;


@Controller
public class RuleNameController {

    private static final Logger logger = LogManager.getLogger(RuleNameController.class);

    @Autowired
    private RuleNameService ruleNameService;

    /**
     *
     * @param model
     * @param authentication
     * @return la vue ruleName/list
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("ruleNames", ruleNameService.findAll());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "ruleName/list";
    }

    /**
     *
     * @param bid
     * @return la vue ruleName/add
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        return "ruleName/add";
    }

    /**
     *
     * @param ruleName
     * @param result
     * @param model
     * @return la vue ruleName/list apres validation du ruleName
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Erreurs lors de la validation de RuleName {}: {}",
                    ruleName.getName(), result.getAllErrors());
            return "ruleName/add";
        }
        ruleNameService.save(ruleName);
        return "redirect:/ruleName/list";
    }

    /**
     *
     * @param id
     * @param model
     * @return la vue ruleName/update via l'Id de ruleName
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName updateRuleName = ruleNameService.findById(id);
        model.addAttribute("ruleName", updateRuleName);
        return "ruleName/update";
    }

    /**
     *
     * @param id
     * @param ruleName
     * @param result
     * @param model
     * @return la vue ruleName/list apres mise a jour du rulename
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {

        if (result.hasErrors()) {
            logger.warn("Erreurs dans la mise a jour du rulename {} : {}",
                    ruleName.getName(), result.getAllErrors());
            return "ruleName/update";
        }

        RuleName newRuleName = ruleNameService.findById(id);
        newRuleName.setName(ruleName.getName());
        newRuleName.setDescription(ruleName.getDescription());
        newRuleName.setJson(ruleName.getJson());
        newRuleName.setTemplate(ruleName.getTemplate());
        newRuleName.setSqlStr(ruleName.getSqlStr());
        newRuleName.setSqlPart(ruleName.getSqlPart());
        ruleNameService.save(newRuleName);
        return "redirect:/ruleName/list";
    }

    /**
     *
     * @param id
     * @param model
     * @return la vue ruleName/list apres suppression du ruleName via son Id
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        ruleNameService.delete(id);
        return "redirect:/ruleName/list";
    }
}
