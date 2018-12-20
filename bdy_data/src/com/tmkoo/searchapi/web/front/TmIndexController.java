package com.tmkoo.searchapi.web.front;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 首页
 * 
 * @author tmkoo
 */
@Controller
@RequestMapping(value = "/tmindex")
public class TmIndexController {

	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model,ServletRequest request) {
		
		model.addAttribute("isIndex", true);
		return "tmindex";
	}
}
