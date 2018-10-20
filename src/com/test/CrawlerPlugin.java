package com.test;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.discovery.solutions.pim.omnifind.OmniFindDocument;
import com.ibm.discovery.solutions.pim.omnifind.OmniFindField;

public class CrawlerPlugin implements com.ibm.swgecm.rssreader.plugin.custom.CrawlerPlugin {

	@Override
	public void init(Logger arg0, Properties arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateDocument(OmniFindDocument doc) {
		final Date documentDate = doc.getDate();
		final OmniFindField mappedField = new OmniFindField("articledate",documentDate.toString());
		final List<OmniFindField> fields = doc.getFields();
		fields.add(mappedField);
		return true;
	}

}
