package org.squadra.atenea.parser.connection;

import java.io.IOException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HttpCG3Connection implements CG3Connection {

	@Override
	public String getPreParsedSentence(String inputSentence) {
		
		String preParsedSentence = "";

		Document doc = null;

		try {

			String sentenceToParse = URLEncoder.encode(inputSentence, "UTF-8");

			doc = Jsoup.connect(
					"http://bartgentoo.no-ip.org/parser/?input="
							+ sentenceToParse).get();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Elements content = doc.getElementsByTag("pre");

		if (!content.isEmpty()) {

			preParsedSentence = content.first().attr("name");

			preParsedSentence = preParsedSentence.replace("</s>", "");

		}

		return preParsedSentence;
		
	}

}
