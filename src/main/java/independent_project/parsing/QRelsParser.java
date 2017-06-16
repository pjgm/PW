package independent_project.parsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import independent_project.model.interest_profile.Topic;

public class QRelsParser {
	public static List<Topic> parseFile(String filePath) throws IOException {
		Map<String, Topic> topic = new HashMap<String, Topic>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		while ((line = br.readLine()) != null) {
			String[] split = line.split(" ");
			Topic rate = topic.get(split[0]);
			if (rate != null) {
				rate.interest += Integer.parseInt(split[3]);
			} else {
				rate = new Topic();
				rate.topid = split[0];
				rate.interest = Integer.parseInt(split[3]);
			}
			topic.put(split[0], rate);
		}

		List<Topic> values = new ArrayList<Topic>(topic.values());
		Collections.sort(values, (Topic t1, Topic t2) -> t2.interest - t1.interest);

		return values.subList(0, 19);
	}
}
