package com.socurites.example.esper.event;

public class CommentLog {
	private String houseId;
	private String commentId;
	private String userId;

	public CommentLog(String houseId, String commentId, String userId) {
		super();
		this.houseId = houseId;
		this.commentId = commentId;
		this.userId = userId;
	}
	
	public String getHouseId(){
		return houseId;
	}
	
	public void setHouseId(String houseId){
		this.houseId = houseId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
}
