package com.rdfpath.rdfentitypath;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class RdfPathController {
	@GetMapping("/")
	public String index(Model model) {
		return "index";
	}
}
