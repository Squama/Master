package com.cnpc.framework.base.pojo;

import java.util.List;

public class TreeNode {

	private String text;

	private List<String> tags;

	private String id;

	private String parentId;

	private String rightType;

	private String levelCode;

	private List<TreeNode> nodes;

	private String icon;

	private TreeState state;

	public String getParentId() {

		return parentId;
	}

	public String getIcon() {

		return icon;
	}

	public void setIcon(String icon) {

		this.icon = icon;
	}

	public String getLevelCode() {

		return levelCode;
	}

	public void setLevelCode(String levelCode) {

		this.levelCode = levelCode;
	}

	public void setParentId(String parentId) {

		this.parentId = parentId;
	}

	public String getText() {

		return text;
	}

	public void setText(String text) {

		this.text = text;
	}

	public List<String> getTags() {

		return tags;
	}

	public void setTags(List<String> tags) {

		this.tags = tags;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public List<TreeNode> getNodes() {

		return nodes;
	}

	public void setNodes(List<TreeNode> nodes) {

		this.nodes = nodes;
	}

	public String getRightType() {
		return rightType;
	}

	public void setRightType(String rightType) {
		this.rightType = rightType;
	}

	public TreeState getState() {
		return state;
	}

	public void setState(TreeState state) {
		this.state = state;
	}

}
