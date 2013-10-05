package org.squadra.atenea.parser.connection;

import java.io.IOException;
import java.net.URLEncoder;

import lombok.extern.log4j.Log4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Log4j
public class HttpCG3Connection implements CG3Connection {

	@Override
	public String getPreParsedSentence(String inputSentence) {
		
		String preParsedSentence = "";

		Document doc = null;

		try {

			String sentenceToParse = URLEncoder.encode(inputSentence, "UTF-8");

			doc = Jsoup.connect(
					"http://bartgentoo.no-ip.org/parser/?input="
							+ sentenceToParse).timeout(5000).get();

			Elements content = doc.getElementsByTag("pre");

			if (!content.isEmpty()) {
				preParsedSentence = content.first().attr("name");
				preParsedSentence = preParsedSentence.replace("</s>", "");
			}
			
		} catch (IOException e) {
			log.error("Timeout conectando con la gramatica");
			e.printStackTrace();
		}

		return preParsedSentence;
		
	}

}
