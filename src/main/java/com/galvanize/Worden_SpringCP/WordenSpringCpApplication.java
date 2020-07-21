package com.galvanize.Worden_SpringCP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@SpringBootApplication
@RestController
public class WordenSpringCpApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordenSpringCpApplication.class, args);
	}

	@GetMapping("/camelize")
	public String camelCase(@RequestParam(value = "original", required = true) String original,
						@RequestParam(value = "initialCap", required = false) boolean initialCap) {
		StringBuilder strcc = new StringBuilder(original);

		for (int x=0; x<original.length(); x++){
			if (original.charAt(x) == '_'){
				strcc.setCharAt(x+1, Character.toUpperCase(original.charAt(x+1)));
			}
		}
		for (int x=0; x<strcc.length(); x++){
			if (strcc.charAt(x) == '_'){
				strcc.deleteCharAt(x);
			}
		}
		if (initialCap){
			strcc.setCharAt(0, Character.toUpperCase(original.charAt(0)));
		}
		return strcc.toString();
	}

	@GetMapping("/redact")
	public String redact(@RequestParam String original, @RequestParam MultiValueMap<String,String> badWord){
		String[] splitarr = original.split(" ");
		List<String> newlist = (List<String>) badWord.get("badWord");
		String returnstr = "";
		for (String val : splitarr){
			if (newlist.contains(val)) {
				for (int y=0; y<val.length();y++){
					returnstr += "*";
				}
				returnstr += " ";
			}
			else {
				returnstr += val + " ";
			}
		}
		return returnstr;
	}

	@PostMapping("/encode")
	public String encode(@RequestParam String message, @RequestParam String key){
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		Map <Character,Character> newmap = new HashMap<Character, Character>();
		String encodedmessage = "";

		for (int x=0; x<key.length();x++){
			newmap.put(alphabet.charAt(x), key.charAt(x));
		}
		for (int y=0; y<message.length(); y++){
			if (message.charAt(y) != ' ') {
				encodedmessage += newmap.get(message.charAt(y));
			}
			else{
				encodedmessage += " ";
			}
		}
		return encodedmessage;
	}

	@PostMapping("/s/{find}/{replacement}")
	public String sed(@PathVariable String find, @PathVariable String replacement, @RequestBody String thestr){
		String[] newarrstr = thestr.split(" ");
		ArrayList<String> newlist = new ArrayList<>();
		String returnstr = "";
		for (int x=0; x<newarrstr.length;x++){
			newlist.add(newarrstr[x]);
		}
		for (int y=0; y<newlist.size(); y++){
			if (newlist.get(y).equals(find)){
				newlist.set(y, replacement);
			}
		}
		for (String word : newlist){
			returnstr += word + " ";
		}
		return returnstr;
	}

}
