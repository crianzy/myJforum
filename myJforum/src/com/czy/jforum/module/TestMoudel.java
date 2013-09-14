package com.czy.jforum.module;

import com.czy.jforum.Command;

public class TestMoudel extends Command{

	@Override
	public void list() {
		this.setTemplateName("test");
	}
	
	public void action(){
		this.context.put("test", "test");
	}

}
