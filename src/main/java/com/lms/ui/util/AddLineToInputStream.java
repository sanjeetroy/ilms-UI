package com.lms.ui.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class AddLineToInputStream {
	static Logger log = Logger.getLogger(AddLineToInputStream.class);
	
	public InputStream addLine(String beginning,InputStream middle){
		log.info("Adding noOps in InputStream");
		
		List<InputStream> streams = Arrays.asList(
			    new ByteArrayInputStream(beginning.getBytes()),
			    middle);
		InputStream story = new SequenceInputStream(Collections.enumeration(streams));
		
		return story;
	}
}
