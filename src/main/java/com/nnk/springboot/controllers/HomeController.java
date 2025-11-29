package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	/**
	 *
	 * @param model
	 * @return la vue home depuis l url racine
	 */
	@RequestMapping("/")
	public String home(Model model)
	{
		return "home";
	}

	/**
	 *
	 * @param model
	 * @return la vue bidList/list
	 */
	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "redirect:/bidList/list";
	}


}
