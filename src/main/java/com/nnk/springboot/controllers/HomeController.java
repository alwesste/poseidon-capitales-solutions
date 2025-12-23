package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controleur gerant les redirection et l'affichage des pages d'acueil
 */
@Controller
public class HomeController
{
	/**
	 * Affiche la page d'accueil principale de l'application.
	 * @param model
	 * @return la vue home depuis l url racine
	 */
	@RequestMapping("/")
	public String home(Model model)
	{
		return "home";
	}

	/**
	 * Redirige les administrateurs connectés vers la liste des offres (BidList).
	 * @param model
	 * @return la vue bidList/list
	 */
	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "redirect:/bidList/list";
	}


}
